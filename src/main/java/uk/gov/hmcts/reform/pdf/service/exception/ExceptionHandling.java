package uk.gov.hmcts.reform.pdf.service.exception;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import uk.gov.hmcts.reform.pdf.generator.exception.MalformedTemplateException;

@ControllerAdvice
public class ExceptionHandling {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandling.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public void handleException(Exception exception) {
        log.error("Unhandled exception:", exception);
    }

    @ExceptionHandler({
        InvalidArgumentException.class,
        MalformedTemplateException.class,
        MissingServletRequestParameterException.class,
        MissingServletRequestPartException.class,
        ServletRequestBindingException.class })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public void handleMissingAndMalformedParametersValues(Exception exception) {
        log.error("Input parameters were missing/malformed:", exception);
    }

    @ExceptionHandler(FeignException.class)
    protected ResponseEntity<Object> handleFeignException(FeignException exc) {
        log.warn("Error communicating with an API", exc);
        return ResponseEntity.status(exc.status()).build();
    }

    @ExceptionHandler(AuthorisationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected void handleAuthorisationException(AuthorisationException exc) {
        log.error("Authorisation error", exc);
        // just respond with unauth status
    }
}
