package com.pivotallabs;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
public class TrackerAuthenticationRequestTest  {
    @Test @Ignore
    public void shouldAddBase64EncodedBasicAuthHeaderToTheRequest() throws Exception {
        //Or instead of encoding header by hand, perhaps just use the preemptive auth libs in HttpClient? 
        String encodedUserColonPass = "c3BvbmdlYm9iOnNxdWlkd2FyZA==";
//        assertThat(true, equalTo(true));
    }
}
