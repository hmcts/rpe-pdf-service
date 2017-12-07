package uk.gov.hmcts.reform.pdf.service.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import feign.FeignException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import uk.gov.hmcts.reform.pdf.generator.HTMLToPDFConverter;
import uk.gov.hmcts.reform.pdf.service.domain.GeneratePdfRequest;
import uk.gov.hmcts.reform.pdf.service.endpoint.v2.PDFGenerationEndpointV2;
import uk.gov.hmcts.reform.pdf.service.exception.AuthorisationException;
import uk.gov.hmcts.reform.pdf.service.service.AuthorisationService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
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
    private AuthorisationService authorisationService;

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
        MockHttpServletResponse response = getResponse(getRequestWithAuthHeader(
            "<html><body>Hello!</body></html>",
            Collections.emptyMap()
        ));

        assertThat(textContentOf(response.getContentAsByteArray())).contains("Hello!");
        Mockito.verify(converter, Mockito.times(1))
            .convert(Mockito.any(), Mockito.anyMapOf(String.class, Object.class));
    }

    @Test
    public void shouldCreateExpectedPdfFromPlainTwigTemplateAndPlaceholders() throws Exception {
        MockHttpServletResponse response = getResponse(getRequestWithAuthHeader(
            "<html>{{ hello }}</html>",
            ImmutableMap.of("hello", "World!")
        ));

        assertThat(textContentOf(response.getContentAsByteArray())).contains("World!");
        Mockito.verify(converter, Mockito.times(1))
            .convert(Mockito.any(), Mockito.anyMapOf(String.class, Object.class));
    }

    @Test
    public void shouldRespond401WhenAuthorisationFailed() throws Exception {
        throwExceptionWhenAuthing(getAuthorisationException(HttpStatus.UNAUTHORIZED));

        HttpServletResponse response = getResponse(getRequestWithAuthHeader(
            "<html></html>",
            Collections.emptyMap()
        ));

        assertHttpStatus(response, HttpStatus.UNAUTHORIZED);
        Mockito.verify(converter, Mockito.never()).convert(Mockito.any(), Mockito.anyMapOf(String.class, Object.class));
    }

    @Test
    public void shouldRespond401WhenAuthorisationFailedWith4xx() throws Exception {
        throwExceptionWhenAuthing(getAuthorisationException(HttpStatus.NOT_FOUND));

        HttpServletResponse response = getResponse(getRequestWithAuthHeader(
            "<html></html>",
            Collections.emptyMap()
        ));

        assertHttpStatus(response, HttpStatus.UNAUTHORIZED);
        Mockito.verify(converter, Mockito.never()).convert(Mockito.any(), Mockito.anyMapOf(String.class, Object.class));
    }

    @Test
    public void shouldRespondWithFeignExceptionWhenAuthFailedWith5xx() throws Exception {
        throwExceptionWhenAuthing(getFeignException(HttpStatus.SERVICE_UNAVAILABLE));

        HttpServletResponse response = getResponse(getRequestWithAuthHeader(
            "<html></html>",
            Collections.emptyMap()
        ));

        assertHttpStatus(response, HttpStatus.SERVICE_UNAVAILABLE);
        Mockito.verify(converter, Mockito.never()).convert(Mockito.any(), Mockito.anyMapOf(String.class, Object.class));
    }

    @Test
    public void shouldReturn400WhenAuthHeaderIsMissing() throws Exception {
        HttpServletResponse response = getResponse(getRequestWithoutAuthHeader(
            "<html></html>",
            Collections.emptyMap()
        ));

        assertHttpStatus(response, HttpStatus.BAD_REQUEST);
        Mockito.verify(authorisationService, Mockito.never()).authorise(Mockito.anyString());
        Mockito.verify(converter, Mockito.never()).convert(Mockito.any(), Mockito.anyMapOf(String.class, Object.class));
    }

    private MockHttpServletRequestBuilder getRequestWithoutAuthHeader(String template, Map<String, Object> values)
        throws JsonProcessingException {

        GeneratePdfRequest request = new GeneratePdfRequest(template, values);
        String json = objectMapper.writeValueAsString(request);

        return post(API_URL)
            .accept(MediaType.APPLICATION_PDF_VALUE)
            .contentType(PDFGenerationEndpointV2.MEDIA_TYPE)
            .content(json);
    }

    private MockHttpServletRequestBuilder getRequestWithAuthHeader(String template, Map<String, Object> values)
        throws JsonProcessingException {

        return getRequestWithoutAuthHeader(template, values)
            .header(AuthorisationService.SERVICE_AUTHORISATION_HEADER, "some-auth-header");
    }

    private MockHttpServletResponse getResponse(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return webClient.perform(requestBuilder).andReturn().getResponse();
    }

    private void throwExceptionWhenAuthing(Throwable exception) {
        Mockito.doThrow(exception).when(authorisationService).authorise(Mockito.anyString());
    }

    private FeignException getFeignException(HttpStatus status) {
        feign.Response feignResponse = feign.Response.builder()
            .headers(Collections.emptyMap())
            .status(status.value())
            .build();

        return FeignException.errorStatus("oh no", feignResponse);
    }

    private AuthorisationException getAuthorisationException(HttpStatus status) {
        FeignException exception = getFeignException(status);

        return new AuthorisationException(exception.getMessage(), exception);
    }

    private void assertHttpStatus(HttpServletResponse response, HttpStatus status) {
        assertThat(response.getStatus()).isEqualTo(status.value());
    }
}
