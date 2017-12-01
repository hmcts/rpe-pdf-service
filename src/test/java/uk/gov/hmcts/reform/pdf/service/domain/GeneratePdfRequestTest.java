package uk.gov.hmcts.reform.pdf.service.domain;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Base64;

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
    public void template_is_byte_array_encoded_as_base64_string() throws IOException {
        String originalSource = "<html>{{ test }}</html>";
        String encodedSource = Base64.getEncoder().encodeToString(originalSource.getBytes());
        String json = "{\"template\":\"" + encodedSource + "\",\"values\":{}}";

        GeneratePdfRequest pdfRequest = mapper.readValue(json, GeneratePdfRequest.class);

        assertThat(new String(pdfRequest.template)).isEqualTo(originalSource);
    }

    @Test
    public void values_is_a_hashmap() throws IOException {
        String json = "{\"template\":\"\",\"values\":{\"foo\":\"bar\"}}";

        GeneratePdfRequest pdfRequest = mapper.readValue(json, GeneratePdfRequest.class);

        assertThat(pdfRequest.values.get("foo")).isEqualTo("bar");
    }
}
