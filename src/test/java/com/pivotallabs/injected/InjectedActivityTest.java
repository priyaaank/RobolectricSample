package com.pivotallabs.injected;

import android.content.Context;
import android.widget.TextView;
import com.google.inject.Inject;
import com.pivotallabs.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(InjectedTestRunner.class)
public class InjectedActivityTest {
    @Inject Context context;

    @Inject InjectedActivity injectedActivity;
    @Inject Counter fieldCounter;
    @Inject FakeDateProvider fakeDateProvider;

    @Before
    public void setUp() {
        fakeDateProvider.setDate("December 8, 2010");
    }

    @Test
    public void shouldAssignStringToTextView() throws Exception {
        injectedActivity.onCreate(null);
        TextView injectedTextView = (TextView) injectedActivity.findViewById(R.id.injected_text_view);
        assertThat(injectedTextView.getText().toString(),
                equalTo("Roboguice Activity tested with Robolectric - December 8, 2010"));
    }

    @Test
    public void shouldInjectSingletons() throws Exception {
        Counter instance = injectedActivity.getInjector().getInstance(Counter.class);
        assertEquals(0, instance.count);

        instance.count++;

        Counter instanceAgain = injectedActivity.getInjector().getInstance(Counter.class);
        assertEquals(1, instanceAgain.count);

        assertSame(fieldCounter, instance);
    }

    @Test
    public void shouldBeAbleToInjectAContext() throws Exception {
        assertNotNull(context);
    }

}
