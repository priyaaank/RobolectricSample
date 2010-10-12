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
    private TrackerAuthenticator trackerAuthenticator;
    private TestCallbacks callbacks;
    private Activity context;

    @Before
    public void setUp() throws Exception {
        apiGateway = new TestApiGateway();
        context = new Activity();
        trackerAuthenticator = new TrackerAuthenticator(apiGateway, context);
        callbacks = new TestCallbacks();
        trackerAuthenticator.signIn("spongebob", "squidward", callbacks);
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

    @Test
    public void shouldCallSuccessWhenAuthenticationSucceeds() {
        simulateSuccessfulAuthentication();
        assertThat(callbacks.succcessWasCalled, equalTo(true));
        assertThat(callbacks.failuireWasCalled, equalTo(false));
        assertThat(callbacks.completeWasCalled, equalTo(true));
    }

    @Test
    public void shouldCallFailureWhenAuthenticationFails() {
        apiGateway.simulateResponse(500, "ERROR");
        assertThat(callbacks.failuireWasCalled, equalTo(true));
        assertThat(callbacks.succcessWasCalled, equalTo(false));
        assertThat(callbacks.completeWasCalled, equalTo(true));
    }

    @Test
    public void shouldStoreApiTokenInPrefs() {
        simulateSuccessfulAuthentication();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(TrackerAuthenticator.TRACKER_AUTH_PREF_KEY, Context.MODE_PRIVATE);
        assertThat(sharedPreferences.getString("guid", ""), equalTo("c93f12c71bec27843c1d84b3bdd547f3"));
    }

    private void simulateSuccessfulAuthentication() {
        apiGateway.simulateResponse(200,
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<token>\n" +
                        "  <guid>c93f12c71bec27843c1d84b3bdd547f3</guid>\n" +
                        "  <id type=\"integer\">1</id>\n" +
                        "</token>"
        );
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
