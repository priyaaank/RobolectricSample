package com.pivotallabs.api;

public interface ApiResponseCallbacks {
    public void onSuccess(ApiResponse response);
    public void onFailure(ApiResponse response);
    public void onComplete();
}
