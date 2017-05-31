package uk.gov.hmcts.reform.cmc.pdf.generator;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class HTMLToPDF {

    private PebbleEngine pebble;

    public HTMLToPDF() {
        this(new PebbleEngine.Builder()
            .loader(new StringLoader())
            .cacheActive(false)
            .build()
        );
    }

    public HTMLToPDF(PebbleEngine pebble) {
        this.pebble = pebble;
    }

    public byte[] generate(byte[] template, Map<String, Object> context) {
        String processedHtml = processTemplate(template, context);
        return convertToPDF(processedHtml);
    }

    private String processTemplate(byte[] template, Map<String, Object> context) {
        Writer writer = new StringWriter();
        try {
            PebbleTemplate pebbleTemplate = pebble.getTemplate(new String(template));
            pebbleTemplate.evaluate(writer, context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    private byte[] convertToPDF(String htmlString) {
        try {
            final File outputFile = new File(UUID.randomUUID().toString() + ".pdf");
            OutputStream os = new FileOutputStream(outputFile);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlString);
            renderer.layout();
            renderer.createPDF(os, false);
            renderer.finishPDF();
            return Files.readAllBytes(Paths.get(outputFile.toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
