package uk.gov.hmcts.reform.pdf.service.integration;

import com.microsoft.applicationinsights.TelemetryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
public class ResponseCodesTest {

    private static final String API_URL = "/api/v1/pdf-generator/html";
    private static final String PLACEHOLDER_VALUES = "placeholderValues";
    private static final String NO_VALUES = "{ }";
    private static final String TEMPLATE = "template";

    @MockBean
    protected TelemetryClient telemetry;

    @Autowired
    protected MockMvc webClient;

    @Test
    public void shouldReturn400WhenTemplateIsNotSent() throws Exception {
        webClient
            .perform(multipart(API_URL)
                .param(PLACEHOLDER_VALUES, NO_VALUES))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenPlaceholderValuesAreNotSent() throws Exception {
        webClient
            .perform(multipart(API_URL)
                .file(TEMPLATE, "<html></html>".getBytes(Charset.defaultCharset())))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenEmptyTemplateIsSent() throws Exception {
        webClient
            .perform(multipart(API_URL)
                .file(TEMPLATE, new byte[]{})
                .param(PLACEHOLDER_VALUES, NO_VALUES))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenEmptyStringIsSentForPlaceholderValues() throws Exception {
        webClient
            .perform(multipart(API_URL)
                .file(TEMPLATE, "<html></html>".getBytes(Charset.defaultCharset()))
                .param(PLACEHOLDER_VALUES, ""))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenMalformedJsonIsSentForPlaceholderValues() throws Exception {
        webClient
            .perform(multipart(API_URL)
                .file(TEMPLATE, "<html></html>".getBytes(Charset.defaultCharset()))
                .param(PLACEHOLDER_VALUES, "{:"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenMalformedHtmlTemplateIsSent() throws Exception {
        webClient
            .perform(multipart(API_URL)
                .file(TEMPLATE, "<html><not-html".getBytes(Charset.defaultCharset()))
                .param(PLACEHOLDER_VALUES, NO_VALUES))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenMalformedTwigTemplateIsSent() throws Exception {
        webClient
            .perform(multipart(API_URL)
                .file(TEMPLATE, "<html> {% notATag </html>".getBytes(Charset.defaultCharset()))
                .param(PLACEHOLDER_VALUES, NO_VALUES))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenMissingPlaceholderValues() throws Exception {
        webClient
            .perform(multipart(API_URL)
                .file(TEMPLATE, "<html><body> {{person}} </body></html>".getBytes(Charset.defaultCharset()))
                .param(PLACEHOLDER_VALUES, "{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn200WhenCorrectHtmlTemplateIsSent() throws Exception {
        webClient
            .perform(multipart(API_URL)
                .file(TEMPLATE, "<html><body>Hello!</body></html>".getBytes(Charset.defaultCharset()))
                .param(PLACEHOLDER_VALUES, NO_VALUES))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn200WhenCorrectTwigTemplateIsSent() throws Exception {
        webClient
            .perform(multipart(API_URL)
                .file(TEMPLATE, "<html><body>{{ hello }}</body></html>".getBytes(Charset.defaultCharset()))
                .param(PLACEHOLDER_VALUES, "{ \"hello\": \"world\" }"))
            .andExpect(status().isOk());
    }

}
