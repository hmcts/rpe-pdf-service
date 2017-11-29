package uk.gov.hmcts.reform.cmc.pdf.exception;

public class MalformedTemplateException extends RuntimeException {

    public MalformedTemplateException(String message) {
        super(message);
    }

    public MalformedTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

}
