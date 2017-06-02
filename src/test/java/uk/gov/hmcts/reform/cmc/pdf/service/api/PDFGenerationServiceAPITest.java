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

    @Autowired
    private MockMvc webClient;

    @Test
    public void shouldReturn400WhenTemplateIsNotSent() throws Exception {
        webClient
            .perform(fileUpload("/api/v1/pdf-generator/html")
                .file("placeholderValues", "{ }".getBytes()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenPlaceholderValuesAreNotSent() throws Exception {
        webClient
            .perform(fileUpload("/api/v1/pdf-generator/html")
                .file("template", "<html></html>".getBytes()))
            .andExpect(status().isBadRequest());
    }

}
