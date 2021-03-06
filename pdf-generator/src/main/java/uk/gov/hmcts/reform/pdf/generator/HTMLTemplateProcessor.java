package uk.gov.hmcts.reform.pdf.generator;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.pdf.generator.exception.MalformedTemplateException;
import uk.gov.hmcts.reform.pdf.generator.exception.PDFGenerationException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class HTMLTemplateProcessor {

    private static final Logger log = LoggerFactory.getLogger(HTMLTemplateProcessor.class);

    private PebbleEngine pebble;

    public HTMLTemplateProcessor() {
        this(new PebbleEngine.Builder()
            .strictVariables(true)
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
    public String process(String template, Map<String, Object> context) {
        log.debug("Processing the template file");
        log.trace("Template: {}", template);
        log.trace("Context: {}", context);
        try (Writer writer = new StringWriter()) {
            PebbleTemplate pebbleTemplate = pebble.getTemplate(template);
            pebbleTemplate.evaluate(writer, context);
            log.debug("Template processing finished successfully");
            return writer.toString();
        } catch (PebbleException e) {
            throw new MalformedTemplateException("Malformed Twig syntax in the template", e);
        } catch (IOException e) {
            throw new PDFGenerationException("There was an error during template processing", e);
        }
    }

}
