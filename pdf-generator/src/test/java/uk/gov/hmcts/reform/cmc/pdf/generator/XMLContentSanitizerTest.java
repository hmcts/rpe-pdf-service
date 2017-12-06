package uk.gov.hmcts.reform.cmc.pdf.generator;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({
    "checkstyle:AvoidEscapedUnicodeCharacters", "checkstyle:AbbreviationAsWordInName", "checkstyle:IllegalTokenText" })
public class XMLContentSanitizerTest {

    private XMLContentSanitizer contentSanitizer = new XMLContentSanitizer();

    @Test
    public void shouldReturnValidStringUnmodified() {
        String input = "Valid string";
        String output = contentSanitizer.stripIllegalCharacters(input);
        assertThat(output).isEqualTo(input);
    }

    @Test
    public void shouldStripAnIllegalFormFeedCharacterAndKeepTheRestOfInput() {
        String input = "abc\u000Cdef";
        String expected = "abcdef";
        String output = contentSanitizer.stripIllegalCharacters(input);
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldKeepAllowedControlCharacters() {
        String input = "\u0009\n\r";
        String output = contentSanitizer.stripIllegalCharacters(input);
        assertThat(output).isEqualTo(input);
    }

    @Test
    public void shouldKeepAllowedCharacterFromBasicMultilingualPlaneEdgesBeforeSurrogateBlocks() {
        String input = "\u0020\uD7FF";
        String output = contentSanitizer.stripIllegalCharacters(input);
        assertThat(output).isEqualTo(input);
    }

    @Test
    public void shouldKeepAllowedCharacterFromBasicMultilingualPlaneEdgesAfterSurrogateBlocks() {
        String input = "\uE000\uFFFD";
        String output = contentSanitizer.stripIllegalCharacters(input);
        assertThat(output).isEqualTo(input);
    }

    @Test
    public void shouldStripSurrogateBlockCodePoint() {
        String input = "\uD800";
        String output = contentSanitizer.stripIllegalCharacters(input);
        assertThat(output).isEqualTo("");
    }

    @Test
    public void shouldStripFFFEAndFFFFSpecialCharacters() {
        String input = "\uFFFE\uFFFF";
        String output = contentSanitizer.stripIllegalCharacters(input);
        assertThat(output).isEqualTo("");
    }

}
