package uk.gov.hmcts.reform.pdf.service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import feign.FeignException;
import feign.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.pdf.generator.HTMLToPDFConverter;
import uk.gov.hmcts.reform.pdf.service.domain.GeneratePdfRequest;
import uk.gov.hmcts.reform.pdf.service.endpoint.v2.PDFGenerationEndpointV2;
import uk.gov.hmcts.reform.pdf.service.service.AuthService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GeneratedPDFContentV2Test {

    private static final String API_URL = "/pdfs";
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc webClient;

    @SpyBean
    private HTMLToPDFConverter converter;

    @MockBean
    private AuthService authService;

    private static String textContentOf(byte[] pdfData) throws IOException {
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfData));
        try {
            return new PDFTextStripper().getText(pdfDocument);
        } finally {
            pdfDocument.close();
        }
    }

    @After
    public void verifyAuth() {
        verify(authService).authenticate("some-service-auth-header");
    }

    @Test
    public void shouldCreateExpectedPdfFromPlainHtmlTemplate() throws Exception {
        HttpServletResponse response = getResponse("<html><body>Hello!</body></html>", Collections.emptyMap());
        assertGeneratedPdfContains(response, "Hello!");
    }

    @Test
    public void shouldCreateExpectedPdfFromPlainTwigTemplateAndPlaceholders() throws Exception {
        HttpServletResponse response = getResponse("<html>{{ hello }}</html>", ImmutableMap.of("hello", "World!"));
        assertGeneratedPdfContains(response, "World!");
    }

    @Test
    public void failAuthentication() throws Exception {
        Response feignResponse = Response.builder().headers(Collections.emptyMap()).status(666).build();
        FeignException exception = FeignException.errorStatus("oh no", feignResponse);

        when(authService.authenticate("some-service-auth-header")).thenThrow(exception);

        HttpServletResponse response = getResponse("<html></html>", Collections.emptyMap());

        assertThat(response.getStatus()).isEqualTo(666);
        verify(converter, never()).convert(any(), anyMapOf(String.class, Object.class));
    }

    private HttpServletResponse getResponse(String template, Map<String, Object> values) throws Exception {
        GeneratePdfRequest request = new GeneratePdfRequest(template, values);
        String json = objectMapper.writeValueAsString(request);

        return webClient
            .perform(post(API_URL)
                .accept(MediaType.APPLICATION_PDF_VALUE)
                .contentType(PDFGenerationEndpointV2.MEDIA_TYPE)
                .header("ServiceAuthorization", "some-service-auth-header")
                .content(json))
            .andReturn().getResponse();
    }

    private void assertGeneratedPdfContains(HttpServletResponse response, String expectedText) throws IOException {
        assertThat(textContentOf(((MockHttpServletResponse) response).getContentAsByteArray())).contains(expectedText);
        verify(converter, times(1)).convert(any(), anyMapOf(String.class, Object.class));
    }
}
