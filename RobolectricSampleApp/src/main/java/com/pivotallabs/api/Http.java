package com.pivotallabs.api;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static com.pivotallabs.util.Strings.isEmptyOrWhitespace;

public class Http {

    public Response get(String url, Map<String, String> headers, String username, String password)
            throws IOException, URISyntaxException {
        URI uri = new URI(url);
        return makeRequest(headers, username, password, new HttpGet(uri), uri.getHost());
    }

    public Response post(String url, Map<String, String> headers, String postBody, String username, String password)
            throws IOException, URISyntaxException {
        URI uri = new URI(url);
        HttpPost method = new HttpPost(uri);
        method.setEntity(new StringEntity(postBody, "UTF-8"));
        return makeRequest(headers, username, password, method, uri.getHost());
    }

    private Response makeRequest(Map<String, String> headers, String username, String password, HttpRequestBase method, String host) {
        DefaultHttpClient client = null;
        try {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                method.setHeader(entry.getKey(), entry.getValue());
            }
            client = getPromiscuousDefaultClient();
            addBasicAuthCredentials(client, host, username, password);
            return new Response(client.execute(method));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (client != null) {
                try {
                    client.getConnectionManager().shutdown();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void addBasicAuthCredentials(DefaultHttpClient client, String domainName, String username, String password) {
        if (!isEmptyOrWhitespace(username) || !isEmptyOrWhitespace(password)) {
            AuthScope authScope = new AuthScope(domainName, 443);
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            client.getCredentialsProvider().setCredentials(authScope, credentials);
        }
    }

    private DefaultHttpClient getPromiscuousDefaultClient() {
        HttpParams parameters = new BasicHttpParams();
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", new CertificateIgnoringSSLSocketFactory(), 443));
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        ClientConnectionManager manager = new ThreadSafeClientConnManager(parameters, schemeRegistry);
        return new DefaultHttpClient(manager, parameters);
    }

    public static class Response {
        private static final int BUFFER_SIZE = 2 ^ 12;
        private int statusCode;
        private String responseBody;

        public Response(HttpResponse httpResponse) {
            statusCode = httpResponse.getStatusLine().getStatusCode();
            try {
                responseBody = fromStream(httpResponse.getEntity().getContent());
            } catch (IOException e) {
                throw new RuntimeException("error reading response body", e);
            }
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getResponseBody() {
            return responseBody;
        }

        public String fromStream(InputStream inputStream) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), BUFFER_SIZE);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            } finally {
                inputStream.close();
            }
            return stringBuilder.toString();
        }
    }
}
