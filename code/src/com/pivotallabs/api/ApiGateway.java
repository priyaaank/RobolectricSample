package com.pivotallabs.api;

import java.io.IOException;
import java.net.URISyntaxException;

public class ApiGateway {
    public void makeRequest(ApiRequest apiRequest, ApiResponseCallbacks responseCallbacks) {
        // TODO: make the http call off the main thread,
        //   create an Api response and call back into the callbacks on the main thread
        try {
            Http.Response response
                    = new Http().get(apiRequest.getUrlString(), apiRequest.getHeaders(), apiRequest.getUsername(), apiRequest.getPassword());
            ApiResponse apiResponse = new ApiResponse(response.getStatusCode(), response.getResponseBody());
            System.out.println("apiResponse = " + apiResponse);
            dispatch(apiResponse, responseCallbacks);
        } catch (IOException e) {
            throw new RuntimeException("error making request", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("error making request", e);
        }
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
