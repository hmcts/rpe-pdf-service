package uk.gov.hmcts.reform.cmc.pdf.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.util.XRRuntimeException;
import uk.gov.hmcts.reform.cmc.pdf.generator.exception.MalformedTemplateException;
import uk.gov.hmcts.reform.cmc.pdf.generator.exception.PDFGenerationException;

import java.io.ByteArrayOutputStream;

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
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(htmlString);
            renderer.layout();
            renderer.createPDF(outputStream, true);

            log.debug("PDF generation finished successfully");
            return outputStream.toByteArray();
        } catch (XRRuntimeException e) {
            throw new MalformedTemplateException("Malformed HTML document provided", e);
        } catch (Exception e) {
            throw new PDFGenerationException(e);
        }
    }

}
