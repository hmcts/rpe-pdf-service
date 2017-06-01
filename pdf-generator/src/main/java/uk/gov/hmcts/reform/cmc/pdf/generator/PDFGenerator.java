package uk.gov.hmcts.reform.cmc.pdf.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class PDFGenerator {

    private static final Logger log = LoggerFactory.getLogger(PDFGenerator.class);

    /**
     * Generates a PDF document from provided HTML.
     *
     * @param htmlString a String containing HTML to convert to PDF
     * @return a byte array which contains generated PDF output
     */
    public byte[] generateFrom(String htmlString) {
        log.debug("Generating PDF from given HTML file");
        log.trace("HTML content: {}", htmlString);
        File outputFile = createTempFile();
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(htmlString);
            renderer.layout();
            renderer.createPDF(outputStream, true);

            log.debug("PDF generation finished successfully");
            return Files.readAllBytes(Paths.get(outputFile.toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File createTempFile() {
        try {
            return File.createTempFile(UUID.randomUUID().toString(), ".pdf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
