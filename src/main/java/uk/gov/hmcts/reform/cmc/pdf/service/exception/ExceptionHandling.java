package uk.gov.hmcts.reform.cmc.pdf.service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ControllerAdvice
public class ExceptionHandling {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandling.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public void handleException(Exception exception) {
        log.error("Unhandled exception:", exception);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public void handleInvalidArgumentException(InvalidArgumentException exception) {
        handleException(exception);
    }

    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        MissingServletRequestPartException.class
    })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public void handleMissingServletRequestElements(Exception exception) {
        handleException(exception);
    }

}
