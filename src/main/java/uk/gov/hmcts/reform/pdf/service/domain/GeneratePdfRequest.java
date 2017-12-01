package uk.gov.hmcts.reform.pdf.service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GeneratePdfRequest {

    public final byte[] template;
    public final Map<String, Object> values;

    public GeneratePdfRequest(
        @JsonProperty(value = "template", required = true) byte[] template,
        @JsonProperty(value = "values", required = true) Map<String, Object> values
    ) {
        this.template = template;
        this.values = values;
    }
}
