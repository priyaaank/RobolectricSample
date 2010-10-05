package com.pivotallabs.api;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


public class CertificateIgnoringSSLSocketFactory implements SocketFactory, LayeredSocketFactory {

    private SSLContext sslcontext = null;

    private static SSLContext createContext() throws IOException {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{new TrustingTrustManager()}, null);
            return context;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private SSLContext getSSLContext() throws IOException {
        if (sslcontext == null) {
            sslcontext = createContext();
        }
        return sslcontext;
    }

    @Override
    public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException {
        SSLSocket sslSocket = (SSLSocket) ((sock != null) ? sock : createSocket());
        if ((localAddress != null) || (localPort > 0)) {
            if (localPort < 0) {
                localPort = 0;
            }
            sslSocket.bind(new InetSocketAddress(localAddress, localPort));
        }
        sslSocket.connect(new InetSocketAddress(host, port), HttpConnectionParams.getConnectionTimeout(params));
        sslSocket.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
        return sslSocket;
    }

    @Override
    public Socket createSocket() throws IOException {
        return getSSLContext().getSocketFactory().createSocket();
    }

    @Override
    public boolean isSecure(Socket socket) throws IllegalArgumentException {
        return true;
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public boolean equals(Object obj) {
        return ((obj != null) && obj.getClass().equals(CertificateIgnoringSSLSocketFactory.class));
    }

    public int hashCode() {
        return CertificateIgnoringSSLSocketFactory.class.hashCode();
    }
}