package com.pivotallabs;

import com.pivotallabs.api.TestApiGateway;

public class TestResponses {
    public static void simulateSuccessfulAuthentication(TestApiGateway apiGateway) {
        apiGateway.simulateResponse(200,
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<token>\n" +
                        "  <guid>c93f12c71bec27843c1d84b3bdd547f3</guid>\n" +
                        "  <id type=\"integer\">1</id>\n" +
                        "</token>"
        );
    }

    public static void simulateUnauthorizedResponse(TestApiGateway apiGateway) {
        apiGateway.simulateResponse(401, "Access Denied");
    }
}
