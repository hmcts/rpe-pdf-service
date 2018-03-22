package uk.gov.hmcts.reform.pdf.service.exception;

import uk.gov.hmcts.reform.logging.exception.AlertLevel;
import uk.gov.hmcts.reform.logging.exception.UnknownErrorCodeException;

/**
 * SonarQube reports as error. Max allowed - 5 parents
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class InvalidArgumentException extends UnknownErrorCodeException {

    public InvalidArgumentException(String message, Throwable cause) {
        super(AlertLevel.P4, message, cause);
    }

    public InvalidArgumentException(Throwable cause) {
        super(AlertLevel.P4, cause);
    }

    public InvalidArgumentException(String message) {
        super(AlertLevel.P4, message);
    }
}
