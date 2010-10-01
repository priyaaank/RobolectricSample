package com.pivotallabs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.pivotallabs.api.ApiGateway;

public class TrackerAuthenticationActivity extends Activity {

    TrackerAuthenticator trackerAuthenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker_authentication_layout);

        trackerAuthenticator = new TrackerAuthenticator(new ApiGateway());

        final EditText usernameEditText = (EditText) findViewById(R.id.username);
        final EditText passwordEditText = (EditText) findViewById(R.id.password);

        View signInButton = findViewById(R.id.sign_in_button);

        new ViewEnablingTextWatcher(signInButton, usernameEditText, passwordEditText);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackerAuthenticator.signIn(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        new MyAuthenticationCallbacks());
            }
        });
    }

    private static class MyAuthenticationCallbacks implements AuthenticationCallbacks {
        @Override
        public void onSuccess(String apiToken) {
        }

        @Override
        public void onFailure() {
        }
    }
}
