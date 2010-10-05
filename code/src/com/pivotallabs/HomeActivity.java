package com.pivotallabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.pivotallabs.api.ApiGateway;

public class HomeActivity extends Activity {

    public static final String PACKAGE_NAME = "com.pivotallabs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        findViewById(R.id.press_me_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClassName(PACKAGE_NAME, NamesActivity.class.getName());
                startActivity(intent);
            }
        });
    }
}
