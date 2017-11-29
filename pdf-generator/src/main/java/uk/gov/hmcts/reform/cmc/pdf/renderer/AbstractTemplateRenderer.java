package uk.gov.hmcts.reform.cmc.pdf.renderer;

import uk.gov.hmcts.reform.cmc.pdf.Template;

import java.util.Map;

public abstract class AbstractTemplateRenderer {

    public abstract byte[] render(Template template, Map<String, Object> variables);
}
