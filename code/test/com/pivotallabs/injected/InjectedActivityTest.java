package com.pivotallabs.injected;

import android.widget.TextView;
import com.google.inject.Inject;
import com.pivotallabs.R;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(InjectedTestRunner.class)
public class InjectedActivityTest {

    @Inject InjectedActivity injectedActivity;
    @Inject Counter fieldCounter;

    @Test
    public void shouldAssignStringToTextView() throws Exception {
        injectedActivity.onCreate(null);

        TextView injectedTextView = (TextView) injectedActivity.findViewById(R.id.injected_text_view);
        assertEquals(injectedTextView.getText(), "Robolectric Sample");
    }

    @Test
    public void shouldGetNewCounterEveryTestEvenThoughItsBoundSingletonAlso() throws Exception {
        Counter instance = injectedActivity.getInjector().getInstance(Counter.class);
        assertEquals(0, instance.count);

        instance.count++;

        Counter instanceAgain = injectedActivity.getInjector().getInstance(Counter.class);
        assertEquals(1, instanceAgain.count);
        
        assertSame(fieldCounter, instance);
    }
}
