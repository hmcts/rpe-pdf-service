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

    /**
     * Generates a PDF document from provided Twig/HTML template and placeholder values.
     *
     * @param template a byte array which contains the Twig template
     * @param context a map with a structure corresponding to the placeholders used in the template
     * @return a byte array which contains generated PDF output
     */
    public byte[] generate(byte[] template, Map<String, Object> context) {
        String processedHtml = templateProcessor.process(template, context);
        return pdfGenerator.generateFrom(processedHtml);
    }

}
