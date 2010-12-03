package com.pivotallabs.injected;

import android.os.Bundle;
import android.widget.TextView;
import com.pivotallabs.R;
import roboguice.activity.GuiceActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class InjectedActivity extends GuiceActivity {

    @InjectResource(R.string.injected_activity_caption) String caption;
    @InjectView(R.id.injected_text_view) TextView injectedTextView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.injected);

        injectedTextView.setText(caption);
    }
}
