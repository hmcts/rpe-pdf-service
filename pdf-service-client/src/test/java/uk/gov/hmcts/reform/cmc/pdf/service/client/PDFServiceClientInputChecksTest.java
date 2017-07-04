package uk.gov.hmcts.reform.cmc.pdf.service.client;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

import static java.util.Collections.emptyMap;

public class PDFServiceClientInputChecksTest {

    private PDFServiceClient client;

    @Before
    public void beforeEachTest() {
        client = new PDFServiceClient("http://this-can-be-anything/");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIllegalArgumentExceptionWhenGivenNullTemplate() {
        client.generateFromHtml(null, emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenGivenEmptyTemplate() {
        client.generateFromHtml(new byte[] { }, emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIllegalArgumentExceptionWhenGivenNullPlaceholders() {
        client.generateFromHtml("content".getBytes(Charset.defaultCharset()), null);
    }

    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerWWhenGivenNullServiceURLString() {
        new PDFServiceClient(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowIllegalArgumentWWhenGivenInvalidServiceURLString() {
        new PDFServiceClient("this is not a URL");
    }

}
