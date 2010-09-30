package com.pivotallabs;

import android.view.View;
import android.widget.TextView;
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

    @Before
    public void setUp() throws Exception {
        activity = new TrackerAuthenticationActivity();
        activity.onCreate(null);
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
}
