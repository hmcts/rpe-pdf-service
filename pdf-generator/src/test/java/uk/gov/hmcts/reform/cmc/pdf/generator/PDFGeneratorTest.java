package uk.gov.hmcts.reform.cmc.pdf.generator;

import org.junit.Test;
import org.xml.sax.SAXParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PDFGeneratorTest {

    private PDFGenerator pdfGenerator = new PDFGenerator();
    private XMLContentSanitizer contentSanitizer = new XMLContentSanitizer();

    @Test
    public void shouldThrowSaxParseExceptionWhenGivenHTMLWithIllegalCharacters() {
        String illegalHTML = ResourceLoader.loadString("/illegal-characters.html");

        Throwable thrown = catchThrowable(() -> pdfGenerator.generateFrom(illegalHTML));

        assertThat(thrown.getCause())
            .isInstanceOf(SAXParseException.class)
            .hasMessage("An invalid XML character (Unicode: 0xc) was found in the element content of the document.");
    }

    @Test
    public void shouldProcessSuccessfullyWhenGivenAfterRunningIllegalHTMLThroughSanitizer() {
        String illegalHTML = ResourceLoader.loadString("/illegal-characters.html");

        Throwable thrown = catchThrowable(() -> pdfGenerator.generateFrom(contentSanitizer.stripIllegalCharacters(illegalHTML)));

        assertThat(thrown).isNull();
    }

}
