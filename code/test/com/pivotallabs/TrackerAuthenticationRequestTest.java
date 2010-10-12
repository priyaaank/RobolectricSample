package com.pivotallabs;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(FastAndroidTestRunner.class)
public class TrackerAuthenticationRequestTest  {
    @Test
    public void shouldAddBase64EncodedBasicAuthHeaderToTheRequest() throws Exception {
        TrackerAuthenticationRequest request = new TrackerAuthenticationRequest("spongebob", "squarepants");
        String authorization = request.getHeaders().get("Authorization");
        assertThat(authorization, equalTo("Basic spongebob:squarepants__fake_Base64_encode_string__0"));
    }
}
