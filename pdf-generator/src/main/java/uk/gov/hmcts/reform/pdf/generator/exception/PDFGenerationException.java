package uk.gov.hmcts.reform.pdf.generator.exception;

public class PDFGenerationException extends RuntimeException {

    public PDFGenerationException(Throwable cause) {
        super("There was an error during PDF generation", cause);
    }

    public PDFGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

}
