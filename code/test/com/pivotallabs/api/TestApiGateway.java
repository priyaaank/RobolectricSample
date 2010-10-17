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
        ensurePendingRequests();
        dispatch(new ApiResponse(httpCode, responseBody), unshiftEarliestRequest().b);
    }

    public ApiRequest getLatestRequest() {
        ensurePendingRequests();
        return pendingRequests.get(pendingRequests.size() - 1).a;
    }

    private void ensurePendingRequests() {
        if (pendingRequests.isEmpty()) {
            throw new RuntimeException("No pending requests to simulate response for");
        }
    }

    private Pair<ApiRequest, ApiResponseCallbacks> unshiftEarliestRequest() {
        return pendingRequests.remove(0);
    }
}
