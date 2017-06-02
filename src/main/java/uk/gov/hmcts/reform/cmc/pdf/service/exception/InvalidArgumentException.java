package uk.gov.hmcts.reform.cmc.pdf.service.exception;

public class InvalidArgumentException extends RuntimeException {

    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArgumentException(Throwable cause) {
        super(cause);
    }

    public InvalidArgumentException(String message) {
        super(message);
    }
}
