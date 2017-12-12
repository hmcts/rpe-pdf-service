package uk.gov.hmcts.reform.pdf.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;
import uk.gov.hmcts.reform.api.deprecated.DeprecatedApiInterceptor;

@Configuration
public class DeprecatedApiConfiguration {

    @Bean
    public MappedInterceptor deprecatedApi() {
        return new MappedInterceptor(null, new DeprecatedApiInterceptor());
    }
}
