package com.pivotallabs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TrackerAuthenticationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker_authentication_layout);
        EditText usernameEditText = (EditText) findViewById(R.id.username);
        EditText passwordEditText = (EditText) findViewById(R.id.password);
        View signInButton = findViewById(R.id.sign_in_button);

        new ViewEnablingTextWatcher(signInButton, usernameEditText, passwordEditText);
    }
}
