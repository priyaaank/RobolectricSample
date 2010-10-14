package com.pivotallabs.tracker;

import com.pivotallabs.api.ApiGateway;
import com.pivotallabs.api.ApiResponse;
import com.pivotallabs.api.ApiResponseCallbacks;

public class RecentActivity {
    private ApiGateway apiGateway;
    private TrackerAuthenticator trackerAuthenticator;

    public RecentActivity(ApiGateway apiGateway, TrackerAuthenticator trackerAuthenticator) {
        this.apiGateway = apiGateway;
        this.trackerAuthenticator = trackerAuthenticator;
    }

    public void update() {
        apiGateway.makeRequest(new RecentActivityRequest(trackerAuthenticator.getToken()), new RecentActivityApiResponseCallbacks());
    }

    private static class RecentActivityApiResponseCallbacks implements ApiResponseCallbacks {
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
