package uk.gov.hmcts.reform.cmc.pdf.service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionLogging {

    private static final Logger log = LoggerFactory.getLogger(ExceptionLogging.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public void handleException(Exception e) {
        log.error("Unhandled exception:", e);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public void handleInvalidArgumentException(InvalidArgumentException e) {
        handleException(e);
    }

}
