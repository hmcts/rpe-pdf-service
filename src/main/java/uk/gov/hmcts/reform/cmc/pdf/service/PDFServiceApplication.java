package uk.gov.hmcts.reform.cmc.pdf.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "uk.gov.hmcts" })
public class PDFServiceApplication {

    public static final String BASE_PACKAGE_NAME = PDFServiceApplication.class.getPackage().getName();

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PDFServiceApplication.class, args);
    }
}

