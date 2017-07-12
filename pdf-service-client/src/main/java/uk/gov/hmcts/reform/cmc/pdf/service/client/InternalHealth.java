package uk.gov.hmcts.reform.cmc.pdf.service.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.actuate.health.Status;

import java.util.Map;

/**
 * Can't manage to serialise the spring health class.
 * Jackson-java-8 modules constructor parameters won't work either :(
 */
class InternalHealth {
    private final Status status;
    private final Map<String, Object> details;

    @JsonCreator
    public InternalHealth(
        @JsonProperty("status") Status status,
        @JsonProperty("details") Map<String, Object> details
    ) {
        this.status = status;
        this.details = details;
    }

    public Status getStatus() {
        return status;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
