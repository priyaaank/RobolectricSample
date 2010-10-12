package com.pivotallabs;

import com.pivotallabs.api.ApiGateway;
import com.pivotallabs.api.ApiRequest;
import com.pivotallabs.api.TestApiGateway;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TrackerAuthenticatorTest {

    private TestApiGateway apiGateway;
    private TrackerAuthenticator trackerAuthenticator;
    private TestAuthenticationCallbacks responseCallbacks;

    @Before
    public void setUp() throws Exception {
        apiGateway = new TestApiGateway();
        trackerAuthenticator = new TrackerAuthenticator(apiGateway);
        responseCallbacks = new TestAuthenticationCallbacks();
        trackerAuthenticator.signIn("spongebob", "squidward", responseCallbacks);
    }

    @Test
    public void shouldMakeARemoteCallWhenSigningIn() throws Exception {
        assertThat(apiGateway.getLatestRequest().getUrlString(), equalTo("https://www.pivotaltracker.com/services/v3/tokens/active"));
    }

    @Test
    public void shouldSendUsernameAndPassword() {
        TrackerAuthenticationRequest request = (TrackerAuthenticationRequest) apiGateway.getLatestRequest();
        assertThat(request, equalTo(new TrackerAuthenticationRequest("spongebob", "squidward")));
    }

    @Test @Ignore
    public void shouldStoreApiTokenInPrefs() {
        new TrackerAuthenticator(new ApiGateway()).signIn("sponge", "bob", new TestAuthenticationCallbacks());
    }

    private static class TestAuthenticationCallbacks implements AuthenticationCallbacks {
        private String apiToken;

        @Override
        public void onSuccess(String apiToken) {
            this.apiToken = apiToken;
        }

        @Override
        public void onFailure() {
        }
    }
}
