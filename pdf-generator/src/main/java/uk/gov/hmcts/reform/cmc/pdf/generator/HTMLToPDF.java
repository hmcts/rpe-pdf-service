package uk.gov.hmcts.reform.cmc.pdf.generator;

import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class HTMLToPDF {

    private HTMLTemplateProcessor templateProcessor;

    public HTMLToPDF() {
        this(new HTMLTemplateProcessor());
    }

    public HTMLToPDF(HTMLTemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    public byte[] generate(byte[] template, Map<String, Object> context) {
        String processedHtml = templateProcessor.process(template, context);
        return convertToPDF(processedHtml);
    }

    private byte[] convertToPDF(String htmlString) {
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
