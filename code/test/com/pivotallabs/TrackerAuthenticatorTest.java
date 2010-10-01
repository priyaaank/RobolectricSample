package com.pivotallabs;

import com.pivotallabs.api.TestApiGateway;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TrackerAuthenticatorTest {

    @Test
    public void shouldMakeARemoteCallWhenSigningIn() throws Exception {
        TestApiGateway apiGateway = new TestApiGateway();
        TrackerAuthenticator trackerAuthenticator = new TrackerAuthenticator(apiGateway);

        trackerAuthenticator.signIn("spongebob", "squidward", new TestAuthenticationCallbacks());
        assertThat(apiGateway.getLatestRequest().getUrlString(), equalTo("https://www.pivotaltracker.com/services/v3/tokens/active"));
    }

    @Test @Ignore
    public void shouldAddBase64EncodedBasicAuthHeaderToTheRequest() throws Exception {

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
