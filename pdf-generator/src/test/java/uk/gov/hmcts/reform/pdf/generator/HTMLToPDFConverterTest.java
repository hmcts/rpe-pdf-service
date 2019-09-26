package uk.gov.hmcts.reform.pdf.generator;

import org.junit.Test;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class HTMLToPDFConverterTest {

    private HTMLToPDFConverter htmlToPDFConverter = new HTMLToPDFConverter();

    @Test
    public void shouldSuccessfullyProcessContentWithIllegalCharacters() {
        String templateWithIllegalCharacters = ResourceLoader.loadString("/illegal-characters.html");

        byte[] output = htmlToPDFConverter.convert(templateWithIllegalCharacters, emptyMap());

        assertThat(output).isNotEmpty();
    }

}
