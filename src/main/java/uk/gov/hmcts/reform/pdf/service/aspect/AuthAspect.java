package uk.gov.hmcts.reform.pdf.service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.hmcts.reform.pdf.service.service.AuthService;

import java.lang.annotation.Annotation;

@Aspect
@Component
public class AuthAspect {

    private boolean stop;

    @Autowired
    private AuthService authService;

    private void parseAndAuthenticate(RequestHeader header, String serviceAuthHeader) {
        if (header.value().equals("ServiceAuthorization")) {
            stop = true;
            authService.authenticate(serviceAuthHeader);
        }
    }

    @Before("execution(* uk.gov.hmcts.reform.pdf.service.endpoint.v2.*.*(" +
        "@org.springframework.web.bind.annotation.RequestHeader (*), ..))")
    public void authenticate(JoinPoint jp) {
        stop = false;
        Object[] args = jp.getArgs();
        Annotation[][] annotations = ((MethodSignature) jp.getSignature()).getMethod().getParameterAnnotations();

        for (int i = 0; i < annotations.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType() == RequestHeader.class) {
                    parseAndAuthenticate((RequestHeader) annotation, (String) args[i]);
                }
            }

            if (stop) {
                break;
            }
        }
    }
}
