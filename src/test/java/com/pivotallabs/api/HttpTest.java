package com.pivotallabs.api;

import com.google.inject.internal.Maps;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.util.HttpRequestInfo;
import com.xtremelabs.robolectric.util.Strings;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URI;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class HttpTest {
    @Before
    public void setup() {
        Robolectric.setDefaultHttpResponse(200, "OK");
    }

    @Test
    public void testGet_FormsCorrectRequest_noBasicAuth() throws Exception {
        Robolectric.addPendingHttpResponse(200, "OK");

        new Http().get("www.example.com", Maps.<String, String>newHashMap(), null, null);

        assertThat(((HttpUriRequest) Robolectric.getSentHttpRequest(0)).getURI(), equalTo(URI.create("www.example.com")));
    }

    @Test
    public void testGet_shouldApplyCorrectHeaders() throws Exception {
        HashMap<String,String> headers = Maps.newHashMap();
        headers.put("foo", "bar");
        new Http().get("www.example.com", headers, null, null);

        HttpRequest sentHttpRequest = Robolectric.getSentHttpRequest(0);
        assertThat(sentHttpRequest.getHeaders("foo")[0].getValue(), equalTo("bar"));
    }

    @Test
    public void testGet_ShouldUseCorrectHttpMethod() throws Exception {
        new Http().get("www.example.com", Maps.<String, String>newHashMap(), null, null);
        HttpUriRequest sentHttpRequest = (HttpUriRequest) Robolectric.getSentHttpRequest(0);
        assertThat(sentHttpRequest.getMethod(), equalTo(HttpGet.METHOD_NAME));
    }

    @Test
    public void testGet_FormsCorrectRequest_withBasicAuth() throws Exception {
        Robolectric.addPendingHttpResponse(200, "OK");
        new Http().get("www.example.com", Maps.<String, String>newHashMap(), "username", "password");
        HttpRequestInfo sentHttpRequestData = Robolectric.getSentHttpRequestInfo(0);

        CredentialsProvider credentialsProvider =
                (CredentialsProvider) sentHttpRequestData.getHttpContext().getAttribute(ClientContext.CREDS_PROVIDER);
        assertThat(credentialsProvider.getCredentials(AuthScope.ANY).getUserPrincipal().getName(), equalTo("username"));
        assertThat(credentialsProvider.getCredentials(AuthScope.ANY).getPassword(), equalTo("password"));
    }

    @Test
    public void shouldReturnCorrectResponse() throws Exception {
        Robolectric.addPendingHttpResponse(666, "it's all cool");

        Http.Response response = new Http().get("www.example.com", Maps.<String, String>newHashMap(), null, null);

        assertThat(response.getResponseBody(), equalTo("it's all cool\n"));
        assertThat(response.getStatusCode(), equalTo(666));
    }

    @Test
    public void testPost_ShouldUseCorrectMethod() throws Exception {
        new Http().post("www.example.com", Maps.<String, String>newHashMap(), "a post body", null, null);

        HttpUriRequest sentHttpRequest = (HttpUriRequest) Robolectric.getSentHttpRequest(0);
        assertThat(sentHttpRequest.getMethod(), equalTo(HttpPost.METHOD_NAME));
    }

    @Test
    public void testPost_ShouldIncludePostBody() throws Exception {
        new Http().post("www.example.com", Maps.<String, String>newHashMap(), "a post body", null, null);

        HttpPost sentHttpRequest = (HttpPost) Robolectric.getSentHttpRequest(0);
        StringEntity entity = (StringEntity) sentHttpRequest.getEntity();
        String sentPostBody = Strings.fromStream(entity.getContent());
        assertThat(sentPostBody, equalTo("a post body"));
        assertThat(entity.getContentType().getValue(), equalTo("text/plain; charset=UTF-8"));
    }
}
