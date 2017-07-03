package uk.gov.hmcts.reform.cmc.pdf.service.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.cmc.pdf.service.client.PDFServiceClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PDFServiceClientTest {

    @LocalServerPort
    private Integer runningPort;

    private byte[] template = "<html><body>{{ hello }}</body></html>".getBytes();
    private Map<String, Object> placeholders = singletonMap("hello", "World!");

    private PDFServiceClient client;

    @Before
    public void beforeEachTest() {
        client = new PDFServiceClient(new RestTemplate(), testInstanceUrl());
    }

    private String testInstanceUrl() {
        return "http://localhost:" + runningPort + "/api/v1/pdf-generator";
    }

    @Test
    public void shouldGeneratePdfFromValidTemplateAndParameters() throws Exception {
        byte[] pdf = client.generateFromHtml(template, placeholders);
        assertThat(textContentOf(pdf)).contains("World!");
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
