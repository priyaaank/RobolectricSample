package com.pivotallabs.api;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ApiGatewayTest {
    private ApiGateway apiGateway;

    @Before
    public void setUp() throws Exception {
        apiGateway = new ApiGateway();
    }

    @Test @Ignore
    public void makeRequestTest() {
        apiGateway.makeRequest(new ApiRequest() {
            @Override
            public String getUrlString() {
                return "https://www.pivotaltracker.com/services/v3/tokens/active";
            }
        }, new ApiResponseCallbacks() {
            @Override
            public void onSuccess(ApiResponse response) {
            }

            @Override
            public void onFailure(ApiResponse response) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Test
    public void dispatch_shouldCallOntoTheSuccessWhenApiResponseIsSuccess() throws Exception {
        TestResponseCallbacks responseCallbacks = new TestResponseCallbacks();
        ApiResponse apiResponse = new ApiResponse(200, "response body");
        apiGateway.dispatch(apiResponse, responseCallbacks);
        
        assertThat(responseCallbacks.successResponse, sameInstance(apiResponse));
        assertThat(responseCallbacks.failureResponse, nullValue());
        assertThat(responseCallbacks.onCompleteWasCalled, equalTo(true));
    }

    @Test
    public void dispatch_shouldCallOnFailureWhenApiResponseIsFailure() throws Exception {
        TestResponseCallbacks responseCallbacks = new TestResponseCallbacks();
        ApiResponse apiResponse = new ApiResponse(500, "response body");
        apiGateway.dispatch(apiResponse, responseCallbacks);

        assertThat(responseCallbacks.failureResponse, sameInstance(apiResponse));
        assertThat(responseCallbacks.successResponse, nullValue());
        assertThat(responseCallbacks.onCompleteWasCalled, equalTo(true));
    }
}
