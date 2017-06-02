package uk.gov.hmcts.reform.cmc.pdf.generator.exception;

import org.xhtmlrenderer.util.XRRuntimeException;

public class MalformedTemplateException extends RuntimeException {

    public MalformedTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

}
