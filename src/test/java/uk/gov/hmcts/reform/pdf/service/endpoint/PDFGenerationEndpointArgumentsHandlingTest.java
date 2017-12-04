package uk.gov.hmcts.reform.pdf.service.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.pdf.generator.HTMLToPDFConverter;
import uk.gov.hmcts.reform.pdf.service.exception.InvalidArgumentException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PDFGenerationEndpointArgumentsHandlingTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private HTMLToPDFConverter htmlToPdf;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MultipartFile file;

    private PDFGenerationEndpoint endpoint;

    @Before
    public void beforeEach() {
        when(htmlToPdf.convert(any(), anyMapOf(String.class, Object.class))).thenReturn(new byte[] { });
        endpoint = new PDFGenerationEndpoint(htmlToPdf, objectMapper);
    }

    @Test
    public void itShouldThrowInvalidArgumentExceptionWhenGivenEmptyTemplate() throws Exception {
        when(file.isEmpty()).thenReturn(true);

        exception.expect(InvalidArgumentException.class);
        exception.expectMessage("Received an empty template file");

        endpoint.generateFromHtml(file, "{ }");
    }

    @Test
    public void itCallGenerateWhenGivenValidArguments() throws Exception {
        when(file.isEmpty()).thenReturn(false);

        endpoint.generateFromHtml(file, "{ }");

        verify(htmlToPdf).convert(any(), anyMapOf(String.class, Object.class));
    }

}
