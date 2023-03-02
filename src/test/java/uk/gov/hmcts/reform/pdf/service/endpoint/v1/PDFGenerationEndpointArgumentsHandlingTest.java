package uk.gov.hmcts.reform.pdf.service.endpoint.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.pdf.generator.HTMLToPDFConverter;
import uk.gov.hmcts.reform.pdf.service.exception.InvalidArgumentException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings({"deprecation", "removal"})
public class PDFGenerationEndpointArgumentsHandlingTest {

    @Mock
    private HTMLToPDFConverter htmlToPdf;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MultipartFile file;

    private PDFGenerationEndpoint endpoint;

    @Before
    public void beforeEach() {
        when(htmlToPdf.convert(any(), any())).thenReturn(new byte[]{});
        endpoint = new PDFGenerationEndpoint(htmlToPdf, objectMapper);
    }

    @Test
    public void itShouldThrowInvalidArgumentExceptionWhenGivenEmptyTemplate() {
        when(file.isEmpty()).thenReturn(true);
        InvalidArgumentException thrownException = assertThrows(InvalidArgumentException.class, () -> {
            endpoint.generateFromHtml(file, "{ }");
        });
        assertEquals("Invalid exception doesn't match","Received an empty template file", thrownException.getMessage());
    }

    @Test
    public void itCallGenerateWhenGivenValidArguments() throws IOException {
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(new byte[]{0});

        endpoint.generateFromHtml(file, "{ }");

        verify(htmlToPdf).convert(any(), any());
    }

}
