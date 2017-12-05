package uk.gov.hmcts.reform.pdf.service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.pdf.service.service.AuthService;

@Aspect
@Component
public class AuthAspect {

    @Autowired
    private AuthService authService;

    @Before("execution(* uk.gov.hmcts.reform.pdf.service.endpoint.v2.*.*(String, ..))")
    public void authenticate(JoinPoint jp) {
        authService.authenticate((String) jp.getArgs()[0]);
    }
}
