package uk.gov.hmcts.reform.pdf.service.integration;

import com.microsoft.applicationinsights.TelemetryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DeprecatePDFGenerationEndpointTest {

    private static final String API_URL = "/api/v1/pdf-generator/html";

    @MockBean
    protected TelemetryClient telemetry;

    @Autowired
    protected MockMvc webClient;

    @Test
    public void should_add_warning_header_to_the_response() throws Exception {
        MockHttpServletResponse response = webClient
            .perform(multipart(API_URL)
                .file("template", "<html><body>{{ hello }}</body></html>".getBytes(Charset.defaultCharset()))
                .param("placeholderValues", "{ \"hello\": \"World!\" }"))
            .andReturn().getResponse();

        assertThat(response.getHeader("Warning")).contains("deprecated");
    }
}
