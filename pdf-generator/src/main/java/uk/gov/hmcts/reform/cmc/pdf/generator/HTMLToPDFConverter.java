package uk.gov.hmcts.reform.cmc.pdf.generator;

import java.util.Map;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class HTMLToPDFConverter {

    private HTMLTemplateProcessor templateProcessor;
    private PDFGenerator pdfGenerator;
    private XMLContentSanitizer xmlContentSanitizer;

    public HTMLToPDFConverter() {
        this(new HTMLTemplateProcessor(), new PDFGenerator(), new XMLContentSanitizer());
    }

    public HTMLToPDFConverter(HTMLTemplateProcessor templateProcessor, PDFGenerator pdfGenerator, XMLContentSanitizer xmlContentSanitizer) {
        this.templateProcessor = templateProcessor;
        this.pdfGenerator = pdfGenerator;
        this.xmlContentSanitizer = xmlContentSanitizer;
    }

    /**
     * Generates a PDF document from provided Twig/HTML template and placeholder values.
     *
     * @param template a byte array which contains the Twig template
     * @param context a map with a structure corresponding to the placeholders used in the template
     * @return a byte array which contains generated PDF output
     */
    public byte[] convert(byte[] template, Map<String, Object> context) {
        String processedHtml = templateProcessor.process(template, context);
        return pdfGenerator.generateFrom(xmlContentSanitizer.stripIllegalCharacters(processedHtml));
    }

}
