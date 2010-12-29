package com.pivotallabs.api;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.util.HttpRequestInfo;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ApiGatewayTest {
    private ApiGateway apiGateway;
    private TestApiResponseCallbacks responseCallbacks;

    @Before
    public void setUp() throws Exception {
        apiGateway = new ApiGateway();
        responseCallbacks = new TestApiResponseCallbacks();
    }

    @Test
    public void dispatch_shouldCallOntoTheSuccessWhenApiResponseIsSuccess() throws Exception {
        ApiResponse apiResponse = new ApiResponse(200, "response body");
        apiGateway.dispatch(apiResponse, responseCallbacks);

        assertThat(responseCallbacks.successResponse, sameInstance(apiResponse));
        assertThat(responseCallbacks.failureResponse, nullValue());
        assertThat(responseCallbacks.onCompleteWasCalled, equalTo(true));
    }

    @Test
    public void dispatch_shouldCallOnFailureWhenApiResponseIsFailure() throws Exception {
        ApiResponse apiResponse = new ApiResponse(500, "response body");
        apiGateway.dispatch(apiResponse, responseCallbacks);

        assertThat(responseCallbacks.failureResponse, sameInstance(apiResponse));
        assertThat(responseCallbacks.successResponse, nullValue());
        assertThat(responseCallbacks.onCompleteWasCalled, equalTo(true));
    }

    @Test
    public void shouldMakeRemoteCalls() {
        Robolectric.getBackgroundScheduler().pause();

        ApiRequest apiRequest = new TestApiRequest();
        apiGateway.makeRequest(apiRequest, responseCallbacks);

        Robolectric.addPendingHttpResponse(200, "response body");

        Robolectric.getBackgroundScheduler().runOneTask();

        HttpRequestInfo sentHttpRequestData = Robolectric.getSentHttpRequestInfo(0);
        HttpRequest sentHttpRequest = sentHttpRequestData.getHttpRequest();
        assertThat(sentHttpRequest.getRequestLine().getUri(), equalTo("www.example.com"));
        assertThat(sentHttpRequest.getRequestLine().getMethod(), equalTo(HttpGet.METHOD_NAME));

        assertThat(sentHttpRequest.getHeaders("foo")[0].getValue(), equalTo("bar"));

        CredentialsProvider credentialsProvider =
                (CredentialsProvider) sentHttpRequestData.getHttpContext().getAttribute(ClientContext.CREDS_PROVIDER);
        assertThat(credentialsProvider.getCredentials(AuthScope.ANY).getUserPrincipal().getName(), CoreMatchers.equalTo("spongebob"));
        assertThat(credentialsProvider.getCredentials(AuthScope.ANY).getPassword(), CoreMatchers.equalTo("squarepants"));
    }

    @Test
    public void shouldDispatchUponReceivingResponse() throws Exception {
        Robolectric.getBackgroundScheduler().pause();
        Robolectric.getUiThreadScheduler().pause();

        Robolectric.addPendingHttpResponse(200, "response body");

        apiGateway.makeRequest(new TestApiRequest(), responseCallbacks);
        Robolectric.getBackgroundScheduler().runOneTask();

        assertThat(responseCallbacks.successResponse, nullValue());

        Robolectric.getUiThreadScheduler().runOneTask();

        assertThat(responseCallbacks.successResponse.getResponseBody(), equalTo("response body\n"));
    }

    private class TestApiRequest extends ApiRequest {
        @Override public String getUrlString() {
            return "www.example.com";
        }

        @Override public Map<String, String> getParameters() {
            Map<String, String> parameters = super.getParameters();
            parameters.put("baz", "bang");
            return parameters;
        }

        @Override public Map<String, String> getHeaders() {
            Map<String, String> headers = super.getHeaders();
            headers.put("foo", "bar");
            return headers;
        }

        @Override public String getPostBody() {
            return super.getPostBody();
        }

        @Override public String getUsername() {
            return "spongebob";
        }

        @Override public String getPassword() {
            return "squarepants";
        }
    }
}
