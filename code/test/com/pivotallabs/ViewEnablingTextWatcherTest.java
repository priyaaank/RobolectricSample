package com.pivotallabs;

import android.view.View;
import android.widget.TextView;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(FastAndroidTestRunner.class)
public class ViewEnablingTextWatcherTest {
    private View toEnable;
    private TextView textView1;
    private TextView textView2;

    @Before
    public void setUp() throws Exception {
        toEnable = new View(null);
        textView1 = new TextView(null);
        textView2 = new TextView(null);
        toEnable.setEnabled(false);
        new ViewEnablingTextWatcher(toEnable, textView1, textView2);
    }

    @Test @Ignore
    public void shouldEnableTheViewWhenAllTextViewsHaveText() throws Exception {
        
    }
}
