package com.pivotallabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignInDialog extends Dialog {

    TrackerAuthenticator trackerAuthenticator;

    public SignInDialog(Context context, TrackerAuthenticator trackerAuthenticator) {
        super(context, android.R.style.Theme_Light_NoTitleBar);
        this.trackerAuthenticator = trackerAuthenticator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_dialog);

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
                        new SignInCallbacks());
            }
        });
    }

    private class SignInCallbacks implements Callbacks {
        @Override
        public void onSuccess() {
            dismiss();
        }

        @Override
        public void onFailure() {
            Resources resources = getContext().getResources();
            new AlertDialog.Builder(getContext())
                    .setTitle(resources.getString(R.string.error))
                    .setMessage(resources.getString(R.string.unknown_user_pass))
                    .setPositiveButton(resources.getString(R.string.ok), new EmptyOnClickListener())
                    .show();
        }

        @Override
        public void onComplete() {
        }
    }
}
