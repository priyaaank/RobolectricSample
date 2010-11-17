package com.pivotallabs;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.pivotallabs.tracker.RecentActivityActivity;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class HomeActivityTest {
    private HomeActivity activity;
    private Button pressMeButton;
    private Button trackerRecentActivityButton;
    private ImageView pivotalLogo;

    @Before
    public void setUp() throws Exception {
        activity = new HomeActivity();
        activity.onCreate(null);
        pressMeButton = (Button) activity.findViewById(R.id.press_me_button);
        trackerRecentActivityButton = (Button) activity.findViewById(R.id.tracker_recent_activity);
        pivotalLogo = (ImageView) activity.findViewById(R.id.pivotal_logo);
    }

    @Test
    public void shouldHaveAButtonThatSaysPressMe() throws Exception {
        assertThat((String) pressMeButton.getText(), equalTo("Tests Rock!"));
    }

    @Test
    public void pressingTheButtonShouldStartTheListActivity() throws Exception {
        pressMeButton.performClick();

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(NamesActivity.class.getName()));
    }

    @Test
    public void pressingTheButtonShouldStartTheSignInActivity() throws Exception {
        trackerRecentActivityButton.performClick();

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);

        assertThat(shadowIntent.getComponent().getClassName(), equalTo(RecentActivityActivity.class.getName()));
    }

    @Test
    public void shouldHaveALogo() throws Exception {
        assertThat(pivotalLogo.getVisibility(), equalTo(View.VISIBLE));
        assertThat(shadowOf(pivotalLogo).getResourceId(), equalTo(R.drawable.pivotallabs_logo));
    }
}
