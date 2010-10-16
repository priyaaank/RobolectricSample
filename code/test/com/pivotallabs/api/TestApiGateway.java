package com.pivotallabs.api;

import com.pivotallabs.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class TestApiGateway extends ApiGateway {

    List<Pair<ApiRequest, ApiResponseCallbacks>> pendingRequests = new ArrayList<Pair<ApiRequest, ApiResponseCallbacks>>();

    @Override
    public void makeRequest(ApiRequest apiRequest, ApiResponseCallbacks responseCallbacks) {
        pendingRequests.add(Pair.of(apiRequest, responseCallbacks));
    }

    public void simulateResponse(int httpCode, String responseBody) {
        if (pendingRequests.isEmpty()) {
            throw new RuntimeException("No pending requests to simulate response for");
        }
        Pair<ApiRequest, ApiResponseCallbacks> earliestRequestAndCallbacks = unshiftEarliestRequest();
        dispatch(new ApiResponse(httpCode, responseBody), earliestRequestAndCallbacks.b);
    }

    public ApiRequest getLatestRequest() {
        if (pendingRequests.isEmpty()) {
            throw new RuntimeException("No pending requests to simulate response for");
        }
        return pendingRequests.get(pendingRequests.size() - 1).a;
    }

    private Pair<ApiRequest, ApiResponseCallbacks> unshiftEarliestRequest() {
        return pendingRequests.remove(0);
    }
}
