package uk.gov.hmcts.reform.cmc.pdf.generator;

/**
 * <p>
 * Implemented according to <a href="https://www.w3.org/TR/xml/#charsets">https://www.w3.org/TR/xml/#charsets</a>.
 * </p>
 *
 * <p>
 * Decided to strip the <code>[#x10000-#x10FFFF]</code> supplementary plane characters as well since allowing them
 * would make the code much more complex. This is because Java String is UTF-16 and does not support those as literals
 * and I would need to operate on surrogate code pairs. Also it's rather unlikely someone would use them as they
 * essentially are some hieroglyphs.
 * </p>
 *
 *
 * <p>For reference:
 * <ul>
 *   <li><a href="https://en.wikipedia.org/wiki/UTF-16#Description">UTF-16 Surrogate code points</a></li>
 *   <li>
 *       <a href="https://en.wikipedia.org/wiki/Plane_(Unicode)#Supplementary_Multilingual_Plane">
 *           Supplementary Multilingual Plane
 *       </a>
 *   </li>
 *   <li>
 *       <a href="https://stackoverflow.com/questions/36954070/understanding-unicode-surrogate-blocks-noncharacters">
 *           What are Unicode surrogate blocks?
 *       </a>
 *   </li>
 * </ul></p>
 */
@SuppressWarnings("checkstyle:IllegalTokenText")
public class XMLContentSanitizer {

    private static final String INVALID_XML_CHARS = "[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD]";

    public String stripIllegalCharacters(String input) {
        return input.replaceAll(INVALID_XML_CHARS, "");
    }

}
