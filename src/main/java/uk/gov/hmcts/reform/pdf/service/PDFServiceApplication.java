package uk.gov.hmcts.reform.pdf.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class PDFServiceApplication {

    public static final String BASE_PACKAGE_NAME = PDFServiceApplication.class.getPackage().getName();

    public static void main(String[] args) {
        SpringApplication.run(PDFServiceApplication.class, args);
    }
}

