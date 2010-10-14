package com.pivotallabs.tracker;

import com.pivotallabs.EmptyCallbacks;
import com.pivotallabs.FastAndroidTestRunner;
import com.pivotallabs.TestResponses;
import com.pivotallabs.api.ApiRequest;
import com.pivotallabs.api.TestApiGateway;
import com.xtremelabs.droidsugar.fakes.TestMenu;
import com.xtremelabs.droidsugar.fakes.TestMenuItem;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pivotallabs.TestHelper.proxyFor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(FastAndroidTestRunner.class)
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
        signOutAndRunOnCreate();

        assertThat(trackerAuthenticator.isAuthenticated(), equalTo(false));
        assertThat(activity.signInDialog.isShowing(), equalTo(true));
    }

    private void signOutAndRunOnCreate() {
        trackerAuthenticator.signOut();
        activity = new RecentActivityActivity();
        activity.onCreate(null);
    }

    public void shouldNotShowTheSignInDialogIfSignedIn() {
        assertThat(activity.signInDialog, nullValue());
    }

    @Test
    public void shouldRetrieveRecentActivityUponSuccessfulSignIn() {
        assertThat(apiGateway.getLatestRequest(),
                equalTo((ApiRequest) new RecentActivityRequest("c93f12c71bec27843c1d84b3bdd547f3")));
    }

    @Test
    @Ignore
    public void shouldRetrieveRecentActivity() {

    }

    @Test
    public void shouldFinishWhenSignInDialogIsCancelledWithoutSuccessfulSignIn() {
        signOutAndRunOnCreate();

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
}
