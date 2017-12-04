package uk.gov.hmcts.reform.pdf.service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.pdf.service.domain.GeneratePdfRequest;
import uk.gov.hmcts.reform.pdf.service.endpoint.v2.PDFGenerationEndpointV2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GeneratedPDFContentV2Test {

    private static final String API_URL = "/pdf";
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc webClient;

    private static String textContentOf(byte[] pdfData) throws IOException {
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfData));
        try {
            return new PDFTextStripper().getText(pdfDocument);
        } finally {
            pdfDocument.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCreateExpectedPdfFromPlainHtmlTemplate() throws Exception {
        assertThatGeneratedPdfContains("<html><body>Hello!</body></html>", Collections.EMPTY_MAP, "Hello!");
    }

    @Test
    public void shouldCreateExpectedPdfFromPlainTwigTemplateAndPlaceholders() throws Exception {
        Map<String, Object> values = ImmutableMap.of("hello", "World!");
        assertThatGeneratedPdfContains("<html>{{ hello }}</html>", values, "World!");
    }

    private void assertThatGeneratedPdfContains(String template, Map<String, Object> values, String expectedText)
        throws Exception {
        GeneratePdfRequest request = new GeneratePdfRequest(template.getBytes(), values);
        String json = objectMapper.writeValueAsString(request);

        MockHttpServletResponse response = webClient
            .perform(post(API_URL)
                .accept(MediaType.APPLICATION_PDF_VALUE)
                .contentType(PDFGenerationEndpointV2.MEDIA_TYPE)
                .content(json))
            .andReturn().getResponse();

        assertThat(textContentOf(response.getContentAsByteArray())).contains(expectedText);
    }
}
