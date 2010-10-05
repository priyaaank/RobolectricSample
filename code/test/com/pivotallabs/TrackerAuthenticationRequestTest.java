package com.pivotallabs;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

public class TrackerAuthenticationRequestTest extends TestCase {

    @Test @Ignore
    public void shouldAddBase64EncodedBasicAuthHeaderToTheRequest() throws Exception {
        //Or instead of encoding header by hand, perhaps just use the preemptive auth libs in HttpClient? 
        String encodedUserColonPass = "c3BvbmdlYm9iOnNxdWlkd2FyZA==";
    }

}
