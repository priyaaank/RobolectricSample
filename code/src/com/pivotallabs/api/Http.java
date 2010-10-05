package com.pivotallabs.api;

import org.apache.http.HttpResponse;
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
import java.net.*;
import java.util.Map;

public class Http {

    public Response get(String url, Map<String, String> headers, String postBody) throws IOException, URISyntaxException {
        DefaultHttpClient client = null;
        try {
            URI uri = new URI(url);
            HttpRequestBase method;
            if (postBody == null) {
                method = new HttpGet(uri);
            } else {
                method = new HttpPost(uri);
                ((HttpPost) method).setEntity(new StringEntity(postBody, "UTF-8"));
            }
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                method.setHeader(entry.getKey(), entry.getValue());
            }
            client = getPromiscuousDefaultClient();
            HttpResponse response = client.execute(method);
            return new Response(response);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (client != null) {
                try {
                    client.getConnectionManager().shutdown();
                } catch (Exception e) {
                }
            }
        }
    }

    public DefaultHttpClient getPromiscuousDefaultClient() {
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
