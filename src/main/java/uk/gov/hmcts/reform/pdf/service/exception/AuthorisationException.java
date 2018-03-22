package uk.gov.hmcts.reform.pdf.service.exception;

import uk.gov.hmcts.reform.logging.exception.AlertLevel;
import uk.gov.hmcts.reform.logging.exception.UnknownErrorCodeException;

/**
 * SonarQube reports as error. Max allowed - 5 parents
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class AuthorisationException extends UnknownErrorCodeException {

    public AuthorisationException(String message, Throwable cause) {
        super(AlertLevel.P4, message, cause);
    }
}
