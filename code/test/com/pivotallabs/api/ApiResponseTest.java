package com.pivotallabs.api;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ApiResponseTest {

    @Test
    public void isSuccess_shouldReturnTrueIfResponseCodeIsInThe200Range() throws Exception {
        assertThat(new ApiResponse(200, "responseBody").isSuccess(), equalTo(true));
        assertThat(new ApiResponse(201, "responseBody").isSuccess(), equalTo(true));
        assertThat(new ApiResponse(299, "responseBody").isSuccess(), equalTo(true));
    }

    @Test
    public void isSuccess_shouldReturnFalseIfResponseCodeIsIn500Range() throws Exception {
        assertThat(new ApiResponse(500, "responseBody").isSuccess(), equalTo(false));
        assertThat(new ApiResponse(501, "responseBody").isSuccess(), equalTo(false));
    }

    @Test
    public void isUnauthorized_shouldReturnTrueIfResponseCodeIs401() {
        assertThat(new ApiResponse(401, "Access Denied").isUnauthorized(), equalTo(true));
    }
}
