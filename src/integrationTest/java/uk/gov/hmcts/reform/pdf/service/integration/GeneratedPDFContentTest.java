package uk.gov.hmcts.reform.pdf.service.integration;

import com.microsoft.applicationinsights.TelemetryClient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GeneratedPDFContentTest {

    private static final String API_URL = "/api/v1/pdf-generator/html";

    @MockBean
    protected TelemetryClient telemetry;

    @Autowired
    protected MockMvc webClient;

    @Test
    public void shouldCreateExpectedPdfFromPlainHtmlTemplate() throws Exception {
        byte[] pdf = webClient
            .perform(multipart(API_URL)
                .file("template", "<html><body>Hello!</body></html>".getBytes(Charset.defaultCharset()))
                .param("placeholderValues", "{ }"))
            .andReturn().getResponse().getContentAsByteArray();

        assertThat(textContentOf(pdf)).contains("Hello!");
    }

    @Test
    public void shouldCreateExpectedPdfFromPlainTwigTemplateAndPlaceholders() throws Exception {
        byte[] pdf = webClient
            .perform(multipart(API_URL)
                .file("template", "<html><body>{{ hello }}</body></html>".getBytes(Charset.defaultCharset()))
                .param("placeholderValues", "{ \"hello\": \"World!\" }"))
            .andReturn().getResponse().getContentAsByteArray();

        assertThat(textContentOf(pdf)).contains("World!");
    }

    private static String textContentOf(byte[] pdfData) throws IOException {
        try (PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfData))) {
            return new PDFTextStripper().getText(pdfDocument);
        }
    }

}
