package com.pivotallabs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_layout);

        findViewById(R.id.press_me_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView pivotalLogo = (ImageView) findViewById(R.id.pivotal_logo);
                pivotalLogo.setImageResource(R.drawable.pivotallabs_logo);
                pivotalLogo.setVisibility(View.VISIBLE);
            }
        });
    }
}
