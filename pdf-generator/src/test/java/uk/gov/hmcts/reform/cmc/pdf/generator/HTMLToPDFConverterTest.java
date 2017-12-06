package uk.gov.hmcts.reform.cmc.pdf.generator;

import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

public class HTMLToPDFConverterTest {

    private HTMLToPDFConverter htmlToPDFConverter;

    @Before
    public void beforeEachTest() {
        htmlToPDFConverter = new HTMLToPDFConverter();
    }

    @Test
    public void shouldSuccessfullyProcessContentWithIllegalCharacters() {
        byte[] templateWithIllegalCharacters = ResourceLoader.loadBytes("/illegal-characters.html");

        byte[] output = htmlToPDFConverter.convert(templateWithIllegalCharacters, emptyMap());

        assertThat(output).isNotEmpty();
    }

}
