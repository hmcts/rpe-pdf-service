package uk.gov.hmcts.reform.cmc.pdf.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PDFServiceApplication {

    public static final String BASE_PACKAGE_NAME = PDFServiceApplication.class.getPackage().getName();

    public static void main(String[] args) {
        SpringApplication.run(PDFServiceApplication.class, args);
    }
}

