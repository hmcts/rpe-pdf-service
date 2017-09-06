package uk.gov.hmcts.reform.cmc.pdf.generator;

import com.lowagie.text.pdf.BaseFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.FSEntityResolver;
import org.xhtmlrenderer.util.XRRuntimeException;
import org.xml.sax.SAXParseException;
import uk.gov.hmcts.reform.cmc.pdf.generator.exception.MalformedTemplateException;
import uk.gov.hmcts.reform.cmc.pdf.generator.exception.PDFGenerationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class PDFGenerator {

    private static final Logger log = LoggerFactory.getLogger(PDFGenerator.class);

    /**
     * Generates a PDF document from provided HTML.
     *
     * @param htmlString a String containing HTML to convert to PDF
     * @return a byte array which contains generated PDF output
     */
    public byte[] generateFrom(final String htmlString) {
        log.debug("Generating PDF from given HTML file");
        log.trace("HTML content: {}", htmlString);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            final ITextRenderer renderer = new ITextRenderer();
            renderer.getFontResolver().addFont("OpenSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
            documentBuilderFactory.setValidating(false);
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            builder.setEntityResolver(FSEntityResolver.instance());
            final org.w3c.dom.Document document = builder.parse(new ByteArrayInputStream(htmlString.getBytes("UTF-8")));

            renderer.setDocument(document, null);
            renderer.layout();
            renderer.createPDF(outputStream, true);

            log.debug("PDF generation finished successfully");
            return outputStream.toByteArray();
        } catch (XRRuntimeException | SAXParseException e) {
            throw new MalformedTemplateException("Malformed HTML document provided", e);
        } catch (Exception e) {
            throw new PDFGenerationException(e);
        }
    }
}
