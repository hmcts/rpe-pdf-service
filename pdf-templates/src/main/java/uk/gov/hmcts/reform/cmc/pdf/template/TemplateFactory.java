package uk.gov.hmcts.reform.cmc.pdf.template;

import uk.gov.hmcts.reform.cmc.pdf.Template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateFactory {

    public enum TemplateEnum {
        //
    }

    private static Map<TemplateEnum, Template> templates;

    static {
        templates = new ConcurrentHashMap<>();
        // push templates
    }

    public Template getTemplate(TemplateEnum template) {
        return templates.get(template);
    }
}
