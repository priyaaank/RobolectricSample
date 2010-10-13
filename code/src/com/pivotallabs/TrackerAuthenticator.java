package com.pivotallabs;

import android.content.Context;
import android.content.SharedPreferences;
import com.pivotallabs.api.ApiGateway;
import com.pivotallabs.api.ApiResponse;
import com.pivotallabs.api.ApiResponseCallbacks;
import com.pivotallabs.util.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrackerAuthenticator {
    static final String TRACKER_AUTH_PREF_KEY = "tracker-auth";
    private static final String GUID_KEY = "guid";
    public ApiGateway apiGateway;
    private SharedPreferences sharedPreferences;

    public TrackerAuthenticator(ApiGateway apiGateway, Context context) {
        this.apiGateway = apiGateway;
        sharedPreferences = context.getSharedPreferences(TRACKER_AUTH_PREF_KEY, Context.MODE_PRIVATE);
    }

    public void signIn(String username, String password, Callbacks responseCallbacks) {
        apiGateway.makeRequest(new TrackerAuthenticationRequest(username, password), new AuthenticationApiResponseCallbacks(responseCallbacks, sharedPreferences));
    }

    public boolean authenticated() {
        return !Strings.isEmptyOrWhitespace(sharedPreferences.getString(GUID_KEY, ""));
    }

    private static class AuthenticationApiResponseCallbacks implements ApiResponseCallbacks {
        private Callbacks responseCallbacks;
        private SharedPreferences preferences;

        public AuthenticationApiResponseCallbacks(Callbacks responseCallbacks, SharedPreferences preferences) {
            this.responseCallbacks = responseCallbacks;
            this.preferences = preferences;
        }

        @Override
        public void onSuccess(ApiResponse response) {
            Matcher matcher = Pattern.compile("<guid>(.*?)</guid>").matcher(response.getResponseBody());
            matcher.find();
            preferences.edit().putString(GUID_KEY, matcher.group(1)).commit();
            responseCallbacks.onSuccess();
        }

        @Override
        public void onFailure(ApiResponse response) {
            responseCallbacks.onFailure();
        }

        @Override
        public void onComplete() {
            responseCallbacks.onComplete();
        }
    }
}
