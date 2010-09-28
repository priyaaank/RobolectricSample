package com.pivotallabs;

import android.widget.TextView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(FastAndroidTestRunner.class)
public class MyActivityTest {
    private TextView pressMeButton;
    private TextView outputTextView;

    @Before
    public void setUp() throws Exception {
        MyActivity myActivity = new MyActivity();
        myActivity.onCreate(null);
        pressMeButton = (TextView) myActivity.findViewById(R.id.press_me_button_id);
        outputTextView = (TextView) myActivity.findViewById(R.id.output_textview);
    }

    @Test
    public void shouldHaveAButtonThatSaysPressMe() throws Exception {
        assertThat((String) pressMeButton.getText(), equalTo("Press me!"));
    }

    @Test
    public void pressingTheButton_shouldCauseTextToAppear() throws Exception {
        assertThat((String) outputTextView.getText(), equalTo(""));
        pressMeButton.performClick();
        assertThat((String) outputTextView.getText(), equalTo("Wacka Wa!"));
    }
}
