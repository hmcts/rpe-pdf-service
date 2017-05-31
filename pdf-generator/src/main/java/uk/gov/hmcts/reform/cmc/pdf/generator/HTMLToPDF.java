package uk.gov.hmcts.reform.cmc.pdf.generator;

import java.util.Map;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class HTMLToPDF {

    private HTMLTemplateProcessor templateProcessor;
    private PDFGenerator pdfGenerator;

    public HTMLToPDF() {
        this(new HTMLTemplateProcessor(), new PDFGenerator());
    }

    public HTMLToPDF(HTMLTemplateProcessor templateProcessor, PDFGenerator pdfGenerator) {
        this.templateProcessor = templateProcessor;
        this.pdfGenerator = pdfGenerator;
    }

    public byte[] generate(byte[] template, Map<String, Object> context) {
        String processedHtml = templateProcessor.process(template, context);
        return pdfGenerator.generateFrom(processedHtml);
    }

}
