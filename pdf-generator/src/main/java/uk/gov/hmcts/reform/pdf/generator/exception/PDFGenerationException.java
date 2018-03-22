package uk.gov.hmcts.reform.pdf.generator.exception;

import uk.gov.hmcts.reform.logging.exception.AlertLevel;
import uk.gov.hmcts.reform.logging.exception.UnknownErrorCodeException;

/**
 * SonarQube reports as error. Max allowed - 5 parents
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class PDFGenerationException extends UnknownErrorCodeException {

    public PDFGenerationException(Throwable cause) {
        super(AlertLevel.P3, "There was an error during PDF generation", cause);
    }

    public PDFGenerationException(String message, Throwable cause) {
        super(AlertLevel.P3, message, cause);
    }

}
