package uk.gov.hmcts.reform.pdf.service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeneratePdfResponse {

    public final byte[] file;

    public GeneratePdfResponse(
        @JsonProperty(value = "file", required = true) byte[] file
    ) {
        this.file = file;
    }
}
