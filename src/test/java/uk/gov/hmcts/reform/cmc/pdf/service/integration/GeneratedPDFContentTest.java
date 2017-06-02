package uk.gov.hmcts.reform.cmc.pdf.service.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GeneratedPDFContentTest {

    private static final String API_URL = "/api/v1/pdf-generator/html";

    @Autowired
    private MockMvc webClient;

    @Test
    public void shouldCreateExpectedPdfFromPlainHtmlTemplate() throws Exception {
        byte[] pdf = webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html><body>Hello!</body></html>".getBytes())
                .param("placeholderValues", "{ }"))
            .andReturn().getResponse().getContentAsByteArray();

        assertThat(textContentOf(pdf)).contains("Hello!");
    }

    @Test
    public void shouldCreateExpectedPdfFromPlainTwigTemplateAndPlaceholders() throws Exception {
        byte[] pdf = webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html><body>{{ hello }}</body></html>".getBytes())
                .param("placeholderValues", "{ \"hello\": \"World!\" }"))
            .andReturn().getResponse().getContentAsByteArray();

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
