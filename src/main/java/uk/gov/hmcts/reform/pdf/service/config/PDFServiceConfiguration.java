package uk.gov.hmcts.reform.pdf.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.pdf.generator.HTMLToPDFConverter;
import uk.gov.hmcts.reform.pdf.service.client.S2sClient;
import uk.gov.hmcts.reform.pdf.service.service.AuthorisationService;

@Configuration
public class PDFServiceConfiguration {

    @Autowired
    private S2sClient s2sClient;

    @Bean
    public HTMLToPDFConverter htmlToPdf() {
        return new HTMLToPDFConverter();
    }

    @Bean
    public AuthorisationService authorisationService() {
        return new AuthorisationService(s2sClient);
    }
}
