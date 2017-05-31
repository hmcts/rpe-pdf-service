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

public class HtmlToPdf {

    public byte[] convert(byte[] html, Map<String, Object> context) {
        try {
            Writer writer = new StringWriter();
            PebbleEngine pebble = new PebbleEngine.Builder().loader(new StringLoader()).build();
            PebbleTemplate template = pebble.getTemplate(new String(html));
            template.evaluate(writer, context);

            final File outputFile = new File(UUID.randomUUID().toString() + ".pdf");
            OutputStream os = new FileOutputStream(outputFile);

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(writer.toString());
            renderer.layout();
            renderer.createPDF(os, false);
            renderer.finishPDF();
            return Files.readAllBytes(Paths.get(outputFile.toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
