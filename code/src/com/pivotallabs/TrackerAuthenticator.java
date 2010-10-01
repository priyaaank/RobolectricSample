package com.pivotallabs;

import com.pivotallabs.api.ApiGateway;
import com.pivotallabs.api.ApiResponse;
import com.pivotallabs.api.ApiResponseCallbacks;

public class TrackerAuthenticator {
    public ApiGateway apiGateway;

    public TrackerAuthenticator(ApiGateway apiGateway) {
        this.apiGateway = apiGateway;
    }

    public void signIn(String username, String password, AuthenticationCallbacks responseCallbacks) {
        apiGateway.makeRequest(new AuthenticationRequest(username, password), new AuthenticationApiResponseCallbacks(responseCallbacks));
    }

    private static class AuthenticationApiResponseCallbacks implements ApiResponseCallbacks {
        private AuthenticationCallbacks responseCallbacks;

        public AuthenticationApiResponseCallbacks(AuthenticationCallbacks responseCallbacks) {
            this.responseCallbacks = responseCallbacks;
        }

        @Override
        public void onSuccess(ApiResponse response) {

        }

        @Override
        public void onFailure(ApiResponse response) {
        }

        @Override
        public void onComplete() {
        }
    }
}
