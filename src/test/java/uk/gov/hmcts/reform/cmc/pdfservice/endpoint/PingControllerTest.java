package uk.gov.hmcts.reform.cmc.pdfservice.endpoint;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;


public class PingControllerTest {
    @Test
    public void ping() throws Exception {
        PingController classUnderTest = new PingController();
        assertThat( classUnderTest.ping(), CoreMatchers.is("OK"));
    }

}
