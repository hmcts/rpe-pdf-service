package uk.gov.hmcts.reform.cmc.pdf.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.cmc.pdf.generator.HTMLToPDFConverter;

@Configuration
public class PDFServiceConfiguration {

    @Bean
    public HTMLToPDFConverter htmlToPdf() {
        return new HTMLToPDFConverter();
    }

}
