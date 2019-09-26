package uk.gov.hmcts.reform.pdf.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.springframework.http.MediaType;
import uk.gov.hmcts.reform.pdf.service.domain.GeneratePdfRequest;
import uk.gov.hmcts.reform.pdf.service.endpoint.v2.PDFGenerationEndpointV2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneratedPDFContentV2Test {

    private static final String API_URL = "/pdfs";
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        RestAssured.baseURI = System.getenv("TEST_URL");
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.filters(new RequestLoggingFilter(), new ErrorLoggingFilter());
    }

    @Test
    @Category(SmokeTest.class)
    public void shouldCreateExpectedPdfFromPlainHtmlTemplate() throws Exception {
        Response response = makeRequest(
            "<html><body>Hello!</body></html>",
            Collections.emptyMap()
        );

        assertThat(textContentOf(response.getBody().asByteArray())).contains("Hello!");
    }

    @Test
    public void shouldCreateExpectedPdfWithUtf8CharactersEncoded() throws Exception {
        Response response = makeRequest(
            "<html><body>&#163;200</body></html>",
            Collections.emptyMap()
        );

        assertThat(textContentOf(response.getBody().asByteArray())).contains("£200");
    }

    @Test
    public void shouldCreateExpectedPdfWithUtf8Characters() throws Exception {
        Response response = makeRequest(
            "<html><body>£200</body></html>",
            Collections.emptyMap()
        );

        assertThat(textContentOf(response.getBody().asByteArray())).contains("£200");
    }

    @Test
    public void shouldCreateExpectedPdfFromPlainTwigTemplateAndPlaceholders() throws Exception {
        Response response = makeRequest(
            "<html>{{ hello }}</html>",
            ImmutableMap.of("hello", "World!")
        );

        assertThat(textContentOf(response.getBody().asByteArray())).contains("World!");
    }

    private static String textContentOf(byte[] pdfData) throws IOException {
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfData));
        try {
            return new PDFTextStripper().getText(pdfDocument);
        } finally {
            pdfDocument.close();
        }
    }

    private Response makeRequest(String template, Map<String, Object> values)
        throws JsonProcessingException {

        GeneratePdfRequest request = new GeneratePdfRequest(template, values);
        String json = objectMapper.writeValueAsString(request);

        return RestAssured
            .given()
            .accept(MediaType.APPLICATION_PDF_VALUE)
            .contentType(PDFGenerationEndpointV2.MEDIA_TYPE)
            .body(json)
            .when()
            .post(API_URL);
    }
}
