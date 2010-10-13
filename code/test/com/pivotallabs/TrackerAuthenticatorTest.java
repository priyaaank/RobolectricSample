package com.pivotallabs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.pivotallabs.api.TestApiGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(FastAndroidTestRunner.class)
public class TrackerAuthenticatorTest {

    private TestApiGateway apiGateway;
    private TestCallbacks callbacks;
    private Activity context;
    private TrackerAuthenticator trackerAuthenticator;

    @Before
    public void setUp() throws Exception {
        apiGateway = new TestApiGateway();
        context = new Activity();
        callbacks = new TestCallbacks();
        trackerAuthenticator = new TrackerAuthenticator(apiGateway, context);
    }

    @Test
    public void shouldMakeARemoteCallWhenSigningIn() throws Exception {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        assertThat(apiGateway.getLatestRequest().getUrlString(), equalTo("https://www.pivotaltracker.com/services/v3/tokens/active"));
    }

    @Test
    public void shouldSendUsernameAndPassword() {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        TrackerAuthenticationRequest request = (TrackerAuthenticationRequest) apiGateway.getLatestRequest();
        assertThat(request, equalTo(new TrackerAuthenticationRequest("spongebob", "squidward")));
    }

    @Test
    public void authenticated_shouldReturnTrueOnceSignedIn() {
        assertThat(trackerAuthenticator.authenticated(), equalTo(false));
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        TestResponses.simulateSuccessfulAuthentication(apiGateway);
        assertThat(trackerAuthenticator.authenticated(), equalTo(true));
    }

    @Test
    public void signOutShouldRemoveTheSharedPreferences() {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        TestResponses.simulateSuccessfulAuthentication(apiGateway);
        trackerAuthenticator.signOut();
        assertThat(getStoredGuid(), equalTo(""));
    }

    @Test
    public void shouldCallSuccessWhenAuthenticationSucceeds() {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        TestResponses.simulateSuccessfulAuthentication(apiGateway);
        assertThat(callbacks.succcessWasCalled, equalTo(true));
        assertThat(callbacks.failuireWasCalled, equalTo(false));
        assertThat(callbacks.completeWasCalled, equalTo(true));
    }

    @Test
    public void shouldCallFailureWhenAuthenticationFails() {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        simulateSignInFailure();
        assertThat(callbacks.failuireWasCalled, equalTo(true));
        assertThat(callbacks.succcessWasCalled, equalTo(false));
        assertThat(callbacks.completeWasCalled, equalTo(true));
    }

    private void simulateSignInFailure() {
        apiGateway.simulateResponse(500, "ERROR");
    }

    @Test
    public void shouldStoreApiTokenInPrefs() {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        TestResponses.simulateSuccessfulAuthentication(apiGateway);
        assertThat(getStoredGuid(), equalTo("c93f12c71bec27843c1d84b3bdd547f3"));
    }

    private String getStoredGuid() {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(TrackerAuthenticator.TRACKER_AUTH_PREF_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString("guid", "");
    }

    private static class TestCallbacks implements Callbacks {
        public boolean succcessWasCalled;
        public boolean failuireWasCalled;
        public boolean completeWasCalled;

        @Override
        public void onSuccess() {
            succcessWasCalled = true;
        }

        @Override
        public void onFailure() {
            failuireWasCalled = true;
        }

        @Override
        public void onComplete() {
            completeWasCalled = true;
        }
    }
}
