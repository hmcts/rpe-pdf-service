package uk.gov.hmcts.reform.cmc.pdfservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "uk.gov.hmcts" })
public class PdfServiceApplication {

    public static final String BASE_PACKAGE_NAME = PdfServiceApplication.class.getPackage().getName();

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PdfServiceApplication.class, args);
    }
}

