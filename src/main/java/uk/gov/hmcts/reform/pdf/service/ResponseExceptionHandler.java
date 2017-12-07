package uk.gov.hmcts.reform.pdf.service;

import feign.FeignException;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.hmcts.reform.pdf.service.exception.AuthException;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotImplementedException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    protected void handleNotImplemented() {
    }

    @ExceptionHandler(FeignException.class)
    protected ResponseEntity handleFeignException(FeignException exc, WebRequest req) {
        return handleExceptionInternal(exc, null, null, HttpStatus.valueOf(exc.status()), req);
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected void handleAuthException() {
    }
}
