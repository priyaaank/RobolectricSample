package com.pivotallabs.tracker;

import android.app.Activity;
import com.pivotallabs.EmptyCallbacks;
import com.pivotallabs.FastAndroidTestRunner;
import com.pivotallabs.TestResponses;
import com.pivotallabs.api.ApiRequest;
import com.pivotallabs.api.TestApiGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(FastAndroidTestRunner.class)
public class RecentActivityTest {
    private TestApiGateway apiGateway;
    private RecentActivity recentActivity;

    @Before
    public void setUp() throws Exception {
        apiGateway = new TestApiGateway();

        TrackerAuthenticator trackerAuthenticator = new TrackerAuthenticator(apiGateway, new Activity());
        trackerAuthenticator.signIn("user", "pass", new EmptyCallbacks());
        TestResponses.simulateSuccessfulAuthentication(apiGateway);

        recentActivity = new RecentActivity(apiGateway, trackerAuthenticator);
    }

    @Test
    public void update_shouldMakeRequest() throws Exception {
        recentActivity.update();
        assertThat(apiGateway.getLatestRequest(),
                equalTo((ApiRequest) new RecentActivityRequest("c93f12c71bec27843c1d84b3bdd547f3")));
    }
}
