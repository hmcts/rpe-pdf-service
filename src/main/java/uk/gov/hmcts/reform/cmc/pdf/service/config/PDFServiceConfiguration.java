package uk.gov.hmcts.reform.cmc.pdf.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.cmc.pdf.generator.HTMLToPDF;

@Configuration
public class PDFServiceConfiguration {

    @Bean
    public HTMLToPDF htmlToPdf() {
        return new HTMLToPDF();
    }

}
