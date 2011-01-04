package com.pivotallabs.api;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URISyntaxException;

public class ApiGateway {
    public void makeRequest(ApiRequest apiRequest, final ApiResponseCallbacks responseCallbacks) {
        new RemoteCallTask(responseCallbacks).execute(apiRequest);
    }

    protected void dispatch(ApiResponse apiResponse, ApiResponseCallbacks responseCallbacks) {
        if (apiResponse.isSuccess()) {
            responseCallbacks.onSuccess(apiResponse);
        } else {
            responseCallbacks.onFailure(apiResponse);
        }
        responseCallbacks.onComplete();
    }

    private class RemoteCallTask extends AsyncTask<ApiRequest, Void, ApiResponse> {
        private final ApiResponseCallbacks responseCallbacks;

        public RemoteCallTask(ApiResponseCallbacks responseCallbacks) {
            this.responseCallbacks = responseCallbacks;
        }

        @Override
        protected ApiResponse doInBackground(ApiRequest... apiRequests) {
            ApiRequest apiRequest = apiRequests[0];
            try {
                Http.Response response = new Http().get(apiRequest.getUrlString(), apiRequest.getHeaders(), apiRequest.getUsername(), apiRequest.getPassword());
                return new ApiResponse(response.getStatusCode(), response.getResponseBody());
            } catch (IOException e) {
                throw new RuntimeException("error making request", e);
            } catch (URISyntaxException e) {
                throw new RuntimeException("error making request", e);
            }
        }

        @Override
        protected void onPostExecute(ApiResponse apiResponse) {
            dispatch(apiResponse, responseCallbacks);
        }
    }
}
