package uk.gov.hmcts.reform.cmc.pdf.service.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PDFGenerationServiceAPITest {

    public static final String API_URL = "/api/v1/pdf-generator/html";

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
                .file("template", "<html></html>".getBytes()))
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
                .file("template", "<html></html>".getBytes())
                .param("placeholderValues", ""))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenMalformedHTMLTemplateIsSent() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html><not-html".getBytes())
                .param("placeholderValues", "{ }"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenMalformedTwigTemplateIsSent() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html> {% notATag </html>".getBytes())
                .param("placeholderValues", "{ }"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn200WhenCorrectHTMLTemplateIsSent() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html><body>Hello!</body></html>".getBytes())
                .param("placeholderValues", "{ }"))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn200WhenCorrectTwigTemplateIsSent() throws Exception {
        webClient
            .perform(fileUpload(API_URL)
                .file("template", "<html><body>{{ hello }}</body></html>".getBytes())
                .param("placeholderValues", "{ \"hello\": \"world\" }"))
            .andExpect(status().isOk());
    }

}
