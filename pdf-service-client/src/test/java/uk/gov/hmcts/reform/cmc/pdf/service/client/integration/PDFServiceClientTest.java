package uk.gov.hmcts.reform.cmc.pdf.service.client.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.cmc.pdf.service.client.PDFServiceClient;
import uk.gov.hmcts.reform.cmc.pdf.service.client.exception.PDFServiceClientException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PDFServiceClientTest {

    @LocalServerPort
    private Integer runningPort;
    private static final String content = "Nguyễn Tấn Dũng Ą ą Ć ć Ę ę Ł ł Ń ń Ó ó Ś ś Ż ż Ź ź";

    private final byte[] template = ("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html>"
        + " <head><meta charset=\"utf-8\"/>"
        + " <style> "
        + " html, body, table, td, th, span { "
        + " font-family: Open Sans; "
        + " }"
        + " </style></head>"
        + " <body><span>{{ hello }} </span></body></html>")
        .getBytes(Charset.defaultCharset());

    private Map<String, Object> placeholders = singletonMap("hello", content);

    private PDFServiceClient client;

    @Before
    public void beforeEachTest() {
        client = new PDFServiceClient(testInstanceUrl(), "v1");
    }

    private URI testInstanceUrl() {
        try {
            return new URI("http://localhost:" + runningPort);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void shouldGeneratePdfFromValidTemplateAndParameters() throws Exception {
        byte[] pdf = client.generateFromHtml(template, placeholders);
        assertThat(textContentOf(pdf)).contains(content);
    }

    @Test(expected = PDFServiceClientException.class)
    public void shouldThrowClientExceptionWhenEndpointReturnsError() throws Exception {
        byte[] malformedTemplate = "<html malformed html".getBytes(Charset.defaultCharset());
        client.generateFromHtml(malformedTemplate, placeholders);
    }

    private static String textContentOf(byte[] pdfData) throws IOException {
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfData));
        try {
            return new PDFTextStripper().getText(pdfDocument);
        } finally {
            pdfDocument.close();
        }
    }

}
