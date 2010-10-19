package com.pivotallabs.api;

import com.pivotallabs.TestResponses;
import org.junit.Test;
import org.w3c.dom.Document;

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

    @Test
    public void getResponseDocument_shouldCreateAnXmlDocumentFromTheResponseBody() throws Exception {
        Document responseDocument = new ApiResponse(666, TestResponses.AUTH_SUCCESS).getResponseDocument();
        assertThat(Xmls.getTextContentOfChild(responseDocument, "guid"), equalTo("c93f12c"));
    }
}
