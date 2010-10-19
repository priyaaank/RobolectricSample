package com.pivotallabs.tracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import com.pivotallabs.R;
import com.pivotallabs.RobolectricTestRunner;
import com.pivotallabs.TestResponses;
import com.pivotallabs.api.ApiRequest;
import com.pivotallabs.api.TestApiGateway;
import com.xtremelabs.robolectric.fakes.FakeAlertDialog;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;


@RunWith(RobolectricTestRunner.class)
public class SignInDialogTest {
 private View signInButton;
    private TextView usernameEditText;
    private TextView passwordEditText;
    private TestApiGateway apiGateway;
    private SignInDialog signInDialog;

    @Before
    public void setUp() throws Exception {
        apiGateway = new TestApiGateway();
        signInDialog = new SignInDialog(new Activity(), new TrackerAuthenticator(apiGateway, new Activity()));
        
        signInDialog.show();

        usernameEditText = (TextView) signInDialog.findViewById(R.id.username);
        passwordEditText = (TextView) signInDialog.findViewById(R.id.password);
        signInButton = signInDialog.findViewById(R.id.sign_in_button);
    }

    @Test
    public void shouldNotEnableTheSignInButtonUntilUsernameAndPasswordFieldsHaveText() throws Exception {
        assertThat(signInButton.isEnabled(), equalTo(false));
        usernameEditText.setText("Sponge Bob");
        assertThat(signInButton.isEnabled(), equalTo(false));
        passwordEditText.setText("squidward");
        assertThat(signInButton.isEnabled(), equalTo(true));
    }

    @Test
    public void shouldCallTheTrackerApiUponClickingSignIn() throws Exception {
        usernameEditText.setText("Sponge Bob");
        passwordEditText.setText("squidward");
        signInButton.performClick();
        assertRequestWasMade(apiGateway, new TrackerAuthenticationRequest("Sponge Bob", "squidward"));
    }

    @Test
    public void shouldDisableTheSignInButtonWhileRequestIsOutstanding() {
        usernameEditText.setText("Sponge Bob");
        passwordEditText.setText("squidward");
        signInButton.performClick();
        assertThat(signInButton.isEnabled(), equalTo(false));
    }

    @Test
    public void shouldReEnableTheSignInButtonIfSignInFails() {
        usernameEditText.setText("Sponge Bob");
        passwordEditText.setText("squidward");
        signInButton.performClick();
        apiGateway.simulateResponse(401, "Access Denied");
        assertThat(signInButton.isEnabled(), equalTo(true));
    }

    @Test
    public void shouldDismissWhenSuccessfullySignedIn() throws Exception {
        usernameEditText.setText("Spongebob");
        passwordEditText.setText("squidward");
        signInButton.performClick();

        assertThat(signInDialog.isShowing(), equalTo(true));

        apiGateway.simulateResponse(200, TestResponses.AUTH_SUCCESS);

        assertThat(signInDialog.isShowing(), equalTo(false));
    }

    @Test
    public void shouldNotDismissWhenUnSuccessfullySignedIn() throws Exception {
        usernameEditText.setText("Spongebob");
        passwordEditText.setText("squidward");
        signInButton.performClick();

        assertThat(signInDialog.isShowing(), equalTo(true));

        apiGateway.simulateResponse(401, "Access Denied");

        assertThat(signInDialog.isShowing(), equalTo(true));
    }

    @Test
    public void shouldShowAlertDialogWhenUnSuccessfullySignedIn() throws Exception {
        usernameEditText.setText("Spongebob");
        passwordEditText.setText("squidward");
        signInButton.performClick();

        apiGateway.simulateResponse(401, "Access Denied");

        FakeAlertDialog alertDialog = FakeAlertDialog.latestAlertDialog;
        assertThat(alertDialog.isShowing(), equalTo(true));
        assertThat(alertDialog.title, equalTo("Error"));
        assertThat(alertDialog.message, equalTo("Username/Password combination is not recognized."));
        assertThat(alertDialog.getButton(AlertDialog.BUTTON_POSITIVE), not(nullValue()));
    }

    private void assertRequestWasMade(TestApiGateway apiGateway, ApiRequest request) {
        assertThat(apiGateway.getLatestRequest(), equalTo(request));
    }
}
