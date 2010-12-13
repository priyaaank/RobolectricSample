package com.pivotallabs.api;

public class TestApiResponseCallbacks implements ApiResponseCallbacks {

    public boolean onCompleteWasCalled;
    public ApiResponse successResponse;
    public ApiResponse failureResponse;

    @Override
    public void onSuccess(ApiResponse successResponse) {
        this.successResponse = successResponse;
    }

    @Override
    public void onFailure(ApiResponse failureResponse) {
        this.failureResponse = failureResponse;
    }

    @Override
    public void onComplete() {
        onCompleteWasCalled = true;
    }
}
