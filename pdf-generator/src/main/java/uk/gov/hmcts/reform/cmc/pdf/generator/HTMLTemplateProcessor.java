package uk.gov.hmcts.reform.cmc.pdf.generator;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class HTMLTemplateProcessor {

    private PebbleEngine pebble;

    public HTMLTemplateProcessor() {
        this(new PebbleEngine.Builder()
            .loader(new StringLoader())
            .cacheActive(false)
            .build()
        );
    }

    public HTMLTemplateProcessor(PebbleEngine pebble) {
        this.pebble = pebble;
    }

    /**
     * Processes a Twig template.
     *
     * @param template a byte array which contains the Twig template
     * @param context a map with a structure corresponding to the placeholders used in the template
     * @return a String containing processed HTML output
     */
    public String process(byte[] template, Map<String, Object> context) {
        Writer writer = new StringWriter();
        try {
            PebbleTemplate pebbleTemplate = pebble.getTemplate(new String(template));
            pebbleTemplate.evaluate(writer, context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

}
