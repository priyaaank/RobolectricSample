package com.pivotallabs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TrackerAuthenticationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker_authentication_layout);

        TextView usernameEditText = (TextView) findViewById(R.id.username);
        TextView passwordEditText = (TextView) findViewById(R.id.password);
        View signInButton = findViewById(R.id.sign_in_button);


        usernameEditText.addTextChangedListener(new ViewEnablingTextWatcher(signInButton, usernameEditText, passwordEditText));
    }


}
