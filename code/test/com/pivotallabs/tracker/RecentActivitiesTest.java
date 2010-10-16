package com.pivotallabs.tracker;

import android.app.Activity;
import com.pivotallabs.EmptyCallbacks;
import com.pivotallabs.RobolectricSampleTestRunner;
import com.pivotallabs.TestChangeListener;
import com.pivotallabs.TestResponses;
import com.pivotallabs.api.ApiRequest;
import com.pivotallabs.api.TestApiGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricSampleTestRunner.class)
public class RecentActivitiesTest {
    private TestApiGateway apiGateway;
    private RecentActivities recentActivities;

    @Before
    public void setUp() throws Exception {
        apiGateway = new TestApiGateway();

        TrackerAuthenticator trackerAuthenticator = new TrackerAuthenticator(apiGateway, new Activity());
        trackerAuthenticator.signIn("user", "pass", new EmptyCallbacks());
        apiGateway.simulateResponse(200, TestResponses.AUTH_SUCCESS);

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
        apiGateway.simulateResponse(200, TestResponses.RECENT_ACTIVITY);

        assertThat(recentActivities.size(), equalTo(2));

        RecentActivity recentActivity0 = recentActivities.get(0);
        assertThat(recentActivity0.getDescription(), equalTo("I changed the 'request' for squidward. \"Add 'Buyout'\""));

        RecentActivity recentActivity1 = recentActivities.get(1);
        assertThat(recentActivity1.getDescription(),
                equalTo("Spongebob Square edited \"Application tracks listing clicks\""));
    }

    @Test
    public void update_shouldClearExitingItems() throws Exception {
        recentActivities.update();
        apiGateway.simulateResponse(200, TestResponses.RECENT_ACTIVITY);
        assertThat(recentActivities.size(), equalTo(2));

        recentActivities.update();
        apiGateway.simulateResponse(200, TestResponses.RECENT_ACTIVITY);
        assertThat(recentActivities.size(), equalTo(2));
    }

    @Test
    public void shouldFireChangeListenerWhenModified() throws Exception {
        recentActivities.update();

        TestChangeListener listener = new TestChangeListener();
        recentActivities.setOnChangeListener(listener);
        apiGateway.simulateResponse(200, TestResponses.RECENT_ACTIVITY);

        assertThat(listener.changed, equalTo(true));
    }
}
