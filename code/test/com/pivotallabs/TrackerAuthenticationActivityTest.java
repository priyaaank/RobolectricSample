package com.pivotallabs;

import android.view.View;
import android.widget.TextView;
import com.pivotallabs.api.ApiRequest;
import com.pivotallabs.api.TestApiGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(FastAndroidTestRunner.class)
public class TrackerAuthenticationActivityTest {

    private TrackerAuthenticationActivity activity;
    private View signInButton;
    private TextView usernameEditText;
    private TextView passwordEditText;
    private TestApiGateway apiGateway;

    @Before
    public void setUp() throws Exception {
        activity = new TrackerAuthenticationActivity();
        activity.onCreate(null);

        apiGateway = new TestApiGateway();
        activity.trackerAuthenticator.apiGateway = apiGateway; // yyyuuuuuuccccccccckkkkkkk!!! Roboguice?

        usernameEditText = (TextView) activity.findViewById(R.id.username);
        passwordEditText = (TextView) activity.findViewById(R.id.password);
        signInButton = activity.findViewById(R.id.sign_in_button);
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
        assertRequestWasMade(apiGateway, new AuthenticationRequest("Sponge Bob", "squidward"));
    }

    @Test
    public void shouldSwapToTrackerAcitvityFeedActivityUponSuccessfulSignIn() throws Exception {
//        FakeActivity fakeActivity = proxyFor(activity);
//        Intent startedIntent = fakeActivity.startedIntent;
//        FakeIntent fakeIntent = proxyFor(startedIntent);
//        Class<NamesActivity> actualStartedActivityClass = (Class<NamesActivity>) fakeIntent.componentClass;
    }

    private void assertRequestWasMade(TestApiGateway apiGateway, ApiRequest request) {
        assertThat(apiGateway.getLatestRequest(), equalTo(request));
    }
}
