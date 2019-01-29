package uk.gov.hmcts.reform.pdf.service.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import uk.gov.hmcts.reform.pdf.generator.HTMLToPDFConverter;
import uk.gov.hmcts.reform.pdf.service.domain.GeneratePdfRequest;
import uk.gov.hmcts.reform.pdf.service.endpoint.v2.PDFGenerationEndpointV2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    private static String textContentOf(byte[] pdfData) throws IOException {
        PDDocument pdfDocument = PDDocument.load(new ByteArrayInputStream(pdfData));
        try {
            return new PDFTextStripper().getText(pdfDocument);
        } finally {
            pdfDocument.close();
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCreateExpectedPdfFromPlainHtmlTemplate() throws Exception {
        MockHttpServletResponse response = getResponse(getRequest(
            "<html><body>Hello!</body></html>",
            Collections.emptyMap()
        ));

        assertThat(textContentOf(response.getContentAsByteArray())).contains("Hello!");
        verify(converter, times(1)).convert(any(), anyMap());
    }

    @Test
    public void shouldCreateExpectedPdfFromPlainTwigTemplateAndPlaceholders() throws Exception {
        MockHttpServletResponse response = getResponse(getRequest(
            "<html>{{ hello }}</html>",
            ImmutableMap.of("hello", "World!")
        ));

        assertThat(textContentOf(response.getContentAsByteArray())).contains("World!");
        verify(converter, times(1)).convert(any(), anyMap());
    }


    private MockHttpServletRequestBuilder getRequest(String template, Map<String, Object> values)
        throws JsonProcessingException {

        GeneratePdfRequest request = new GeneratePdfRequest(template, values);
        String json = objectMapper.writeValueAsString(request);

        return post(API_URL)
            .accept(MediaType.APPLICATION_PDF_VALUE)
            .contentType(PDFGenerationEndpointV2.MEDIA_TYPE)
            .content(json);
    }

    private MockHttpServletResponse getResponse(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return webClient.perform(requestBuilder).andReturn().getResponse();
    }
}
