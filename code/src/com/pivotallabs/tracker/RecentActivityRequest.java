package com.pivotallabs.tracker;

import com.pivotallabs.api.ApiRequest;

import java.util.Map;

public class RecentActivityRequest extends ApiRequest {
    private String token;

    public RecentActivityRequest(String token) {
        super();
        this.token = token;
    }

    @Override
    public String getUrlString() {
        return "http://www.pivotaltracker.com/services/v3/activities";
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = super.getHeaders();
        headers.put("X-TrackerToken", token);
        return headers;
    }
}
