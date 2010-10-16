package com.pivotallabs.tracker;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.pivotallabs.EmptyCallbacks;
import com.pivotallabs.R;
import com.pivotallabs.RobolectricSampleTestRunner;
import com.pivotallabs.TestResponses;
import com.pivotallabs.api.ApiRequest;
import com.pivotallabs.api.TestApiGateway;
import com.xtremelabs.robolectric.fakes.TestMenu;
import com.xtremelabs.robolectric.fakes.TestMenuItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pivotallabs.TestHelper.proxyFor;
import static com.pivotallabs.TestHelper.yieldToUiThread;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricSampleTestRunner.class)
public class RecentActivityActivityTest {

    private RecentActivityActivity activity;
    private TrackerAuthenticator trackerAuthenticator;
    private TestApiGateway apiGateway;

    @Before
    public void setUp() throws Exception {
        apiGateway = new TestApiGateway();
        activity = new RecentActivityActivity();
        activity.apiGateway = apiGateway;
        trackerAuthenticator = new TrackerAuthenticator(apiGateway, activity);
        trackerAuthenticator.signIn("spongebob", "squarepants", new EmptyCallbacks());
        TestResponses.simulateSuccessfulAuthentication(apiGateway);
        activity.onCreate(null);
    }

    @Test
    public void shouldShowTheSignInDialogIfNotCurrentlySignedIn() throws Exception {
        signOutAndReCreateActivity();

        assertThat(trackerAuthenticator.isAuthenticated(), equalTo(false));
        assertThat(activity.signInDialog.isShowing(), equalTo(true));
    }

    @Test
    public void shouldNotShowTheSignInDialogIfSignedIn() {
        assertThat(activity.signInDialog, nullValue());
    }

    @Test
    public void shouldRetrieveRecentActivityUponSuccessfulSignIn() {
        signOutAndReCreateActivity();

        signInThroughDialog();

        assertThat(apiGateway.getLatestRequest(),
                equalTo((ApiRequest) new RecentActivityRequest("c93f12c71bec27843c1d84b3bdd547f3")));
    }

    @Test
    public void onCreate_shouldRetrieveRecentActivityWhenSignedIn() {
        assertThat(apiGateway.getLatestRequest(),
                   equalTo((ApiRequest) new RecentActivityRequest("c93f12c71bec27843c1d84b3bdd547f3")));
    }

    @Test
    public void shouldPopulateViewWithRetrievedRecentActivity() throws Exception {
        ListView activityList = (ListView) activity.findViewById(R.id.recent_activity_list);
        apiGateway.simulateResponse(200, TestResponses.RECENT_ACTIVITY);
        yieldToUiThread();
        assertThat(((TextView) activityList.getChildAt(0)).getText().toString(),
                equalTo("I changed the 'request' for squidward. \"Add 'Buyout'\""));
    }

    @Test
    public void shouldFinishWhenSignInDialogIsDismissedWithoutSuccessfulSignIn() {
        signOutAndReCreateActivity();

        activity.signInDialog.cancel();

        assertThat(proxyFor(activity).finishWasCalled, equalTo(true));
    }

    @Test
    public void shouldSignOutWhenTheSignOutButtonIsClicked() throws Exception {
        TestMenu menu = new TestMenu();
        menu.add("garbage that should be cleared upon onPrepareOptionsMenu");

        activity.onPrepareOptionsMenu(menu);

        TestMenuItem signOutMenuItem = (TestMenuItem) menu.getItem(0);
        assertThat(signOutMenuItem.isEnabled(), equalTo(true));
        assertThat(signOutMenuItem.getTitle().toString(), equalTo("Sign Out"));

        signOutMenuItem.simulateClick();
        assertThat(trackerAuthenticator.isAuthenticated(), equalTo(false));
        assertThat(proxyFor(activity).finishWasCalled, equalTo(true));
    }

    @Test
    public void signOutButtonShouldBeDisabledWhenNotSignedIn() throws Exception {
        trackerAuthenticator.signOut();
        TestMenu menu = new TestMenu();

        activity.onPrepareOptionsMenu(menu);

        TestMenuItem signOutMenuItem = (TestMenuItem) menu.getItem(0);
        assertThat(signOutMenuItem.isEnabled(), equalTo(false));
        assertThat(signOutMenuItem.getTitle().toString(), equalTo("Sign Out"));
    }

    private void signOutAndReCreateActivity() {
        trackerAuthenticator.signOut();
        apiGateway = new TestApiGateway();
        activity = new RecentActivityActivity();
        activity.apiGateway = apiGateway;
        activity.onCreate(null);
    }

    private void signInThroughDialog() {
        assertThat(activity.signInDialog.isShowing(), equalTo(true));
        ((EditText) activity.signInDialog.findViewById(R.id.username)).setText("user");
        ((EditText) activity.signInDialog.findViewById(R.id.password)).setText("pass");
        activity.signInDialog.findViewById(R.id.sign_in_button).performClick();

        TestResponses.simulateSuccessfulAuthentication(apiGateway);
        assertThat(activity.signInDialog.isShowing(), equalTo(false));
    }
}
