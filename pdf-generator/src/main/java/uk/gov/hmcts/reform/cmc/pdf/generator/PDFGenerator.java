package uk.gov.hmcts.reform.cmc.pdf.generator;

import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class PDFGenerator {

    /**
     * Generates a PDF document from provided HTML.
     *
     * @param htmlString a String containing HTML to convert to PDF
     * @return a byte array which contains generated PDF output
     */
    public byte[] generateFrom(String htmlString) {
        try {
            final File outputFile = File.createTempFile(UUID.randomUUID().toString(), ".pdf");
            OutputStream outputStream = new FileOutputStream(outputFile);
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(htmlString);
            renderer.layout();
            renderer.createPDF(outputStream, true);

            return Files.readAllBytes(Paths.get(outputFile.toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
