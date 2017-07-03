package uk.gov.hmcts.reform.cmc.pdf.service.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.emptyMap;

@RunWith(MockitoJUnitRunner.class)
public class PDFServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    private PDFServiceClient client;

    @Before
    public void beforeEachTest() {
        client = new PDFServiceClient(restTemplate, "http://this-can-be-anything/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenGivenNullTemplate() {
        client.generateFromHtml(null, emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenGivenEmptyTemplate() {
        client.generateFromHtml(new byte[] { }, emptyMap());
    }

}
