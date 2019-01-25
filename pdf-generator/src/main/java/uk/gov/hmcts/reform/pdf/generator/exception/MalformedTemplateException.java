package uk.gov.hmcts.reform.pdf.generator.exception;

public class MalformedTemplateException extends RuntimeException {

    public MalformedTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
