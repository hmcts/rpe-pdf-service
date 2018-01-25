package uk.gov.hmcts.reform.pdf.service.exception;

public class AuthorisationException extends RuntimeException {

    public AuthorisationException(String message, Throwable cause) {
        super(message, cause);
    }
}
