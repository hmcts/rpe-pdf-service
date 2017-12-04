package uk.gov.hmcts.reform.pdf.service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GeneratePdfRequest {

    public final String template;
    public final Map<String, Object> values;

    public GeneratePdfRequest(
        @JsonProperty(value = "template", required = true) String template,
        @JsonProperty(value = "values", required = true) Map<String, Object> values
    ) {
        this.template = template;
        this.values = values;
    }
}
