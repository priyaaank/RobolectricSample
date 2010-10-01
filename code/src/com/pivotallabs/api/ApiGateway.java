package com.pivotallabs.api;

public class ApiGateway {
    public void makeRequest(ApiRequest apiRequest, ApiResponseCallbacks responseCallbacks) {
        // make the http call off the main thread,
        // create an Api response and call back into the callbacks on the main thread
    }

    protected void dispatch(ApiResponse apiResponse, ApiResponseCallbacks responseCallbacks) {
        if (apiResponse.isSuccess()) {
            responseCallbacks.onSuccess(apiResponse);
        } else {
            responseCallbacks.onFailure(apiResponse);
        }
        responseCallbacks.onComplete();
    }
}
