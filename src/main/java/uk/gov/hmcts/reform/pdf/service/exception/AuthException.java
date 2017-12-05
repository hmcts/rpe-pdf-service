package uk.gov.hmcts.reform.pdf.service.exception;

public class AuthException extends RuntimeException {

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
