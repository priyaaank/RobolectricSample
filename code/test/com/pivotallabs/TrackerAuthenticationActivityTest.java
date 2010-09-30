package com.pivotallabs;

import android.view.View;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(FastAndroidTestRunner.class)
public class TrackerAuthenticationActivityTest {

    private TrackerAuthenticationActivity trackerAuthenticationActivity;
    private View signInButton;

    @Before
    public void setUp() throws Exception {
        trackerAuthenticationActivity = new TrackerAuthenticationActivity();
        trackerAuthenticationActivity.onCreate(null);
        signInButton = trackerAuthenticationActivity.findViewById(R.id.sign_in_button);
    }

    @Test
    public void shouldNotEnableTheSignInButtonUntilUsernameAndPasswordFieldsHaveText() throws Exception {
        assertThat(signInButton.isEnabled(), equalTo(false));

    }
}
