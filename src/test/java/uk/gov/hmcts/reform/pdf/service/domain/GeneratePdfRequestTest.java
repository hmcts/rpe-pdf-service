package uk.gov.hmcts.reform.pdf.service.domain;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Confirm Jackson serialisation assumptions.
 */
public class GeneratePdfRequestTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test(expected = JsonMappingException.class)
    public void template_key_is_required() throws IOException {
        String json = "{\"values\":{}}";

        mapper.readValue(json, GeneratePdfRequest.class);
    }

    @Test(expected = JsonMappingException.class)
    public void values_key_is_required() throws IOException {
        String json = "{\"template\":\"\"}";

        mapper.readValue(json, GeneratePdfRequest.class);
    }

    @Test
    public void template_is_string() throws IOException {
        String originalSource = "<html>{{ test }}</html>";
        String json = "{\"template\":\"" + originalSource + "\",\"values\":{}}";

        GeneratePdfRequest pdfRequest = mapper.readValue(json, GeneratePdfRequest.class);

        assertThat(pdfRequest.template).isEqualTo(originalSource);
    }

    @Test
    public void values_is_a_hashmap() throws IOException {
        String json = "{\"template\":\"\",\"values\":{\"foo\":\"bar\"}}";

        GeneratePdfRequest pdfRequest = mapper.readValue(json, GeneratePdfRequest.class);

        assertThat(pdfRequest.values.get("foo")).isEqualTo("bar");
    }
}
