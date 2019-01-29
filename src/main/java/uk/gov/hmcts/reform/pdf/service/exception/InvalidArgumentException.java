package uk.gov.hmcts.reform.pdf.service.exception;

// PMD is complaining about no serial version uid for some reason
public class InvalidArgumentException extends RuntimeException { //NOPMD

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
