package com.pivotallabs.tracker;

import android.app.Activity;
import android.content.SharedPreferences;
import com.pivotallabs.RobolectricTestRunner;
import com.pivotallabs.TestCallbacks;
import com.pivotallabs.TestResponses;
import com.pivotallabs.api.TestApiGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static com.pivotallabs.tracker.TrackerAuthenticator.TRACKER_AUTH_PREF_KEY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
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
        String urlString = apiGateway.getLatestRequest().getUrlString();
        assertThat(urlString, equalTo("https://www.pivotaltracker.com/services/v3/tokens/active"));
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
        assertThat(callbacks.successWasCalled, equalTo(true));
        assertThat(callbacks.failureWasCalled, equalTo(false));
        assertThat(callbacks.completeWasCalled, equalTo(true));
    }

    @Test
    public void shouldCallFailureWhenAuthenticationFails() {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        apiGateway.simulateResponse(500, "ERROR");
        assertThat(callbacks.failureWasCalled, equalTo(true));
        assertThat(callbacks.successWasCalled, equalTo(false));
        assertThat(callbacks.completeWasCalled, equalTo(true));
    }

    @Test
    public void shouldStoreApiTokenInPrefs() {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        apiGateway.simulateResponse(200, TestResponses.AUTH_SUCCESS);
        assertThat(getStoredGuid(), equalTo("c93f12c"));
    }

    @Test
    public void getToken_shouldReturnGuidFromResponse() throws Exception {
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
        apiGateway.simulateResponse(200, TestResponses.AUTH_SUCCESS);
        assertThat(trackerAuthenticator.getToken(), equalTo("c93f12c"));
    }

    private String getStoredGuid() {
        SharedPreferences preferences = context.getSharedPreferences(TRACKER_AUTH_PREF_KEY, MODE_PRIVATE);
        return preferences.getString("guid", "");
    }
}
