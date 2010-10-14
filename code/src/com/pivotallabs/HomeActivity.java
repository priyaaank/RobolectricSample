package com.pivotallabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.pivotallabs.tracker.TrackerRecentActivity;

public class HomeActivity extends Activity {

    public static final String PACKAGE_NAME = "com.pivotallabs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        findViewById(R.id.press_me_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(NamesActivity.class);
            }
        });

        findViewById(R.id.tracker_recent_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TrackerRecentActivity.class);
            }
        });
    }

    private void startActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_NAME, activityClass.getName());
        startActivity(intent);
    }
}
