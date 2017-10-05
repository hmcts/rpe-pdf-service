package uk.gov.hmcts.reform.cmc.pdf.service.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ResponseCodesTest {

    private static final String API_URL = "/api/v1/pdf-generator/html";

    @Autowired
    private MockMvc webClient;

    @Test
    public void shouldReturn400WhenTemplateIsNotSent() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .param("placeholderValues", "{ }"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenPlaceholderValuesAreNotSent() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html></html>".getBytes(Charset.defaultCharset())))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenEmptyTemplateIsSent() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", new byte[] { })
                .param("placeholderValues", "{ }"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenEmptyStringIsSentForPlaceholderValues() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html></html>".getBytes(Charset.defaultCharset()))
                .param("placeholderValues", ""))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenMalformedJsonIsSentForPlaceholderValues() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html></html>".getBytes(Charset.defaultCharset()))
                .param("placeholderValues", "{:"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenMalformedHtmlTemplateIsSent() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html><not-html".getBytes(Charset.defaultCharset()))
                .param("placeholderValues", "{ }"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenMalformedTwigTemplateIsSent() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html> {% notATag </html>".getBytes(Charset.defaultCharset()))
                .param("placeholderValues", "{ }"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenMissingPlaceholderValues() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html><body> {{person}} </body></html>".getBytes(Charset.defaultCharset()))
                .param("placeholderValues", "{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn200WhenCorrectHtmlTemplateIsSent() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html><body>Hello!</body></html>".getBytes(Charset.defaultCharset()))
                .param("placeholderValues", "{ }"))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn200WhenCorrectTwigTemplateIsSent() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html><body>{{ hello }}</body></html>".getBytes(Charset.defaultCharset()))
                .param("placeholderValues", "{ \"hello\": \"world\" }"))
            .andExpect(status().isOk());
    }

}
