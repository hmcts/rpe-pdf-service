package uk.gov.hmcts.reform.cmc.pdf.generator;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class HTMLTemplateProcessor {

    private static final Logger log = LoggerFactory.getLogger(HTMLTemplateProcessor.class);

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
        log.debug("Processing the template file");
        String templateString = new String(template);
        log.trace("Template: {}", templateString);
        log.trace("Context: {}", context);
        try (Writer writer = new StringWriter()) {
            PebbleTemplate pebbleTemplate = pebble.getTemplate(templateString);
            pebbleTemplate.evaluate(writer, context);
            log.debug("Template processing finished successfully");
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
