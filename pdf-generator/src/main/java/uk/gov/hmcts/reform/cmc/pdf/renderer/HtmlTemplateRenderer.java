package uk.gov.hmcts.reform.cmc.pdf.renderer;

import uk.gov.hmcts.reform.cmc.pdf.Template;
import uk.gov.hmcts.reform.cmc.pdf.exception.MalformedTemplateException;

import java.util.Map;

public class HtmlTemplateRenderer extends AbstractTemplateRenderer {

    @Override
    public byte[] render(Template template, Map<String, Object> variables) {
        if (!template.validate(variables)) {
            throw new MalformedTemplateException(
                String.format("Template '%s', with provided arguments is not valid", template.getTemplateUri())
            );
        }

        return new byte[0];
    }
}
