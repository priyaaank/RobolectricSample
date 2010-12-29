package com.pivotallabs.api;

import java.util.HashMap;
import java.util.Map;

public abstract class ApiRequest {
    public abstract String getUrlString();

    public Map<String, String> getParameters() {
        return new HashMap<String, String>();
    }

    public Map<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    public String getPostBody() {
        return null;
    }

    public String getUsername() {
        return null;
    }

    public String getPassword() {
        return null;
    }
}
