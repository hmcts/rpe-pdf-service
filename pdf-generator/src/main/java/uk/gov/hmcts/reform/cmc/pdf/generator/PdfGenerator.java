package uk.gov.hmcts.reform.cmc.pdf.generator;

import uk.gov.hmcts.reform.cmc.pdf.Template;
import uk.gov.hmcts.reform.cmc.pdf.exception.UnknownTemplateRendererException;
import uk.gov.hmcts.reform.cmc.pdf.renderer.AbstractTemplateRenderer;
import uk.gov.hmcts.reform.cmc.pdf.renderer.HtmlTemplateRenderer;
import uk.gov.hmcts.reform.cmc.pdf.renderer.PdfTemplateRenderer;

import java.util.Map;

public class PdfGenerator {

    private final AbstractTemplateRenderer htmlRenderer;

    private final AbstractTemplateRenderer pdfRenderer;

    public PdfGenerator(AbstractTemplateRenderer htmlRenderer, AbstractTemplateRenderer pdfRenderer) {
        this.htmlRenderer = htmlRenderer;
        this.pdfRenderer = pdfRenderer;
    }

    private PdfGenerator() {
        this(new HtmlTemplateRenderer(), new PdfTemplateRenderer());
    }

    public boolean isValid(Template template, Map<String, Object> vars) {
        return template.validate(vars);
    }

    public byte[] generate(Template template, Map<String, Object> vars) {
        return chooseRenderer(template).render(template, vars);
    }

    private AbstractTemplateRenderer chooseRenderer(Template template) throws UnknownTemplateRendererException {
        switch (template.getTemplateType()) {
            case HTML:
                return htmlRenderer;
            case PDF:
                return pdfRenderer;
            default:
                throw new UnknownTemplateRendererException();
        }
    }
}
