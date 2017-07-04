package uk.gov.hmcts.reform.cmc.pdf.service.client;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

import static java.util.Collections.emptyMap;

public class PDFServiceClientTest {

    private PDFServiceClient client;

    @Before
    public void beforeEachTest() {
        client = new PDFServiceClient("http://this-can-be-anything/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenGivenNullTemplate() {
        client.generateFromHtml(null, emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenGivenEmptyTemplate() {
        client.generateFromHtml(new byte[] { }, emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenGivenNullPlaceholders() {
        client.generateFromHtml("content".getBytes(Charset.defaultCharset()), null);
    }

}
