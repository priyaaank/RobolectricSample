package com.pivotallabs.tracker;

import android.content.Context;
import android.content.SharedPreferences;
import com.pivotallabs.Callbacks;
import com.pivotallabs.api.ApiGateway;
import com.pivotallabs.api.ApiResponse;
import com.pivotallabs.api.ApiResponseCallbacks;
import com.pivotallabs.util.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationGateway {
    static final String TRACKER_AUTH_PREF_KEY = "tracker-auth";
    private static final String GUID_KEY = "guid";
    public ApiGateway apiGateway;
    private SharedPreferences sharedPreferences;

    public AuthenticationGateway(ApiGateway apiGateway, Context context) {
        this.apiGateway = apiGateway;
        sharedPreferences = context.getSharedPreferences(TRACKER_AUTH_PREF_KEY, Context.MODE_PRIVATE);
    }

    public void signIn(String username, String password, Callbacks responseCallbacks) {
        TrackerAuthenticationRequest apiRequest = new TrackerAuthenticationRequest(username, password);
        ApiResponseCallbacks remoteCallbacks = new AuthenticationApiResponseCallbacks(responseCallbacks, sharedPreferences);
        apiGateway.makeRequest(apiRequest, remoteCallbacks);
    }

    public boolean isAuthenticated() {
        return !Strings.isEmptyOrWhitespace(getToken());
    }

    public void signOut() {
        sharedPreferences.edit().clear().commit();
    }

    public String getToken() {
        return sharedPreferences.getString(GUID_KEY, "");
    }

    private static class AuthenticationApiResponseCallbacks implements ApiResponseCallbacks {
        private Callbacks callbacks;
        private SharedPreferences sharedPreferences;

        public AuthenticationApiResponseCallbacks(Callbacks callbacks, SharedPreferences sharedPreferences) {
            this.callbacks = callbacks;
            this.sharedPreferences = sharedPreferences;
        }

        @Override
        public void onSuccess(ApiResponse response) {
            Matcher matcher = Pattern.compile("<guid>(.*?)</guid>").matcher(response.getResponseBody());
            matcher.find();
            sharedPreferences.edit().putString(GUID_KEY, matcher.group(1)).commit();
            callbacks.onSuccess();
        }

        @Override
        public void onFailure(ApiResponse response) {
            callbacks.onFailure();
        }

        @Override
        public void onComplete() {
            callbacks.onComplete();
        }
    }
}
