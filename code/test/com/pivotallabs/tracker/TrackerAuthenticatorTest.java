package com.pivotallabs.tracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.pivotallabs.RobolectricSampleTestRunner;
import com.pivotallabs.TestCallbacks;
import com.pivotallabs.TestResponses;
import com.pivotallabs.api.TestApiGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricSampleTestRunner.class)
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
        assertThat(trackerAuthenticator.isAuthenticated(), equalTo(false));
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        apiGateway.simulateResponse(200, TestResponses.AUTH_SUCCESS);
        assertThat(trackerAuthenticator.isAuthenticated(), equalTo(true));
    }

    @Test
    public void signOutShouldRemoveTheSharedPreferences() {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        apiGateway.simulateResponse(200, TestResponses.AUTH_SUCCESS);
        trackerAuthenticator.signOut();
        assertThat(getStoredGuid(), equalTo(""));
    }

    @Test
    public void shouldCallSuccessWhenAuthenticationSucceeds() {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        apiGateway.simulateResponse(200, TestResponses.AUTH_SUCCESS);
        assertThat(callbacks.succcessWasCalled, equalTo(true));
        assertThat(callbacks.failuireWasCalled, equalTo(false));
        assertThat(callbacks.completeWasCalled, equalTo(true));
    }

    @Test
    public void shouldCallFailureWhenAuthenticationFails() {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        apiGateway.simulateResponse(500, "ERROR");
        assertThat(callbacks.failuireWasCalled, equalTo(true));
        assertThat(callbacks.succcessWasCalled, equalTo(false));
        assertThat(callbacks.completeWasCalled, equalTo(true));
    }

    @Test
    public void shouldStoreApiTokenInPrefs() {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        apiGateway.simulateResponse(200, TestResponses.AUTH_SUCCESS);
        assertThat(getStoredGuid(), equalTo("c93f12c71bec27843c1d84b3bdd547f3"));
    }

    @Test
    public void getToken_shouldReturnGuidFromResponse() throws Exception {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        apiGateway.simulateResponse(200, TestResponses.AUTH_SUCCESS);
        assertThat(trackerAuthenticator.getToken(), equalTo("c93f12c71bec27843c1d84b3bdd547f3"));
    }

    private String getStoredGuid() {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(TrackerAuthenticator.TRACKER_AUTH_PREF_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString("guid", "");
    }
}
