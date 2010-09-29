package com.pivotallabs;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pivotallabs.TestHelper.proxyFor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(FastAndroidTestRunner.class)
public class MyActivityTest {
    private TextView pressMeButton;
    private ImageView pivotalLogo;

    @Before
    public void setUp() throws Exception {
        MyActivity myActivity = new MyActivity();
        myActivity.onCreate(null);
        pressMeButton = (TextView) myActivity.findViewById(R.id.press_me_button_id);
        pivotalLogo = (ImageView) myActivity.findViewById(R.id.pivotal_logo);
    }

    @Test
    public void shouldHaveAButtonThatSaysPressMe() throws Exception {
        assertThat((String) pressMeButton.getText(), equalTo("Tests Rock!"));
    }

    @Test
    public void pressingTheButton_shouldImageAppear() throws Exception {
        assertThat(proxyFor(pivotalLogo).visibility, equalTo(View.GONE));
        pressMeButton.performClick();
        assertThat(proxyFor(pivotalLogo).visibility, equalTo(View.VISIBLE));
        assertThat(proxyFor(pivotalLogo).resourceId, equalTo(R.drawable.pivotallabs_logo));
    }
}
