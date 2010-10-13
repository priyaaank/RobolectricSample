package com.pivotallabs;

import com.pivotallabs.api.TestApiGateway;
import com.xtremelabs.droidsugar.fakes.TestMenu;
import com.xtremelabs.droidsugar.fakes.TestMenuItem;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(FastAndroidTestRunner.class)
public class TrackerRecentActivityTest {
    
    private TrackerRecentActivity activity;
    private TrackerAuthenticator trackerAuthenticator;

    @Before
    public void setUp() throws Exception {
        activity = new TrackerRecentActivity();

        TestApiGateway apiGateway = new TestApiGateway();
        activity.apiGateway = apiGateway;

        trackerAuthenticator = new TrackerAuthenticator(apiGateway, activity);
        trackerAuthenticator.signIn("spongebob", "squarepants", new EmptyCallbacks());

        TestResponses.simulateSuccessfulAuthentication(apiGateway);

        activity.onCreate(null);
    }

    @Test
    public void shouldShowTheSignInDialogIfNotCurrentlySignedIn() throws Exception {
        trackerAuthenticator.signOut();
        activity = new TrackerRecentActivity();
        activity.onCreate(null);

        assertThat(trackerAuthenticator.authenticated(), equalTo(false));
        assertThat(activity.signInDialog.isShowing(), equalTo(true));
    }

    public void shouldNotShowTheSignInDialogIfSignedIn() {
        assertThat(activity.signInDialog, nullValue());
    }

    @Test
    @Ignore
    public void shouldSignOutWhenSignOutMenuButtonIsPressed() {

    }

    @Test
    @Ignore
    public void shouldRetrieveRecentActivityUponSuccessfulSignIn() {

    }

    @Test
    @Ignore
    public void shouldFinishWhenSignInDialogIsCancelledWithoutSuccessfulSignIn() {

    }

    @Test
    @Ignore
    public void shouldRetrieveRecentActivity() {

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
        assertThat(trackerAuthenticator.authenticated(), equalTo(false));
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
