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

import static com.pivotallabs.TestResponses.simulateRecentActivityResponse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(FastAndroidTestRunner.class)
public class RecentActivitiesTest {
    private TestApiGateway apiGateway;
    private RecentActivities recentActivities;

    @Before
    public void setUp() throws Exception {
        apiGateway = new TestApiGateway();

        TrackerAuthenticator trackerAuthenticator = new TrackerAuthenticator(apiGateway, new Activity());
        trackerAuthenticator.signIn("user", "pass", new EmptyCallbacks());
        TestResponses.simulateSuccessfulAuthentication(apiGateway);

        recentActivities = new RecentActivities(apiGateway, trackerAuthenticator);
    }

    @Test
    public void update_shouldMakeRequest() throws Exception {
        recentActivities.update();
        assertThat(apiGateway.getLatestRequest(),
                equalTo((ApiRequest) new RecentActivityRequest("c93f12c71bec27843c1d84b3bdd547f3")));
    }

    @Test
    public void update_shouldCreateRecentActivityObjectsFromResponse() throws Exception {
        recentActivities.update();
        simulateRecentActivityResponse(apiGateway);

        assertThat(recentActivities.size(), equalTo(2));

        RecentActivity recentActivity0 = recentActivities.get(0);
        assertThat(recentActivity0.getDescription(), equalTo("I changed the 'request' for squidward."));

        RecentActivity recentActivity1 = recentActivities.get(1);
        assertThat(recentActivity1.getDescription(),
                equalTo("Spongebob Square edited \"Application tracks listing clicks\""));
    }
}
