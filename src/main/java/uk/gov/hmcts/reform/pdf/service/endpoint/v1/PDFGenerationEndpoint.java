package uk.gov.hmcts.reform.pdf.service.endpoint.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.api.deprecated.APIDeprecated;
import uk.gov.hmcts.reform.pdf.generator.HTMLToPDFConverter;
import uk.gov.hmcts.reform.pdf.service.exception.InvalidArgumentException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pdf-generator")
public class PDFGenerationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFGenerationEndpoint.class);

    private final HTMLToPDFConverter htmlToPdf;
    private final ObjectMapper objectMapper;

    @Autowired
    public PDFGenerationEndpoint(HTMLToPDFConverter htmlToPdf, ObjectMapper objectMapper) {
        this.htmlToPdf = htmlToPdf;
        this.objectMapper = objectMapper;
    }

    @APIDeprecated(
        name = "/api/v1/pdf-generator/html",
        docLink = "https://github.com/hmcts/cmc-pdf-service#standard-api",
        expiryDate = "2018-02-08",
        note = "Please use `/pdfs` instead.")
    @Operation(summary = "Returns a PDF file generated from provided HTML/Twig template and placeholder values")
    @PostMapping(
        value = "/html",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<ByteArrayResource> generateFromHtml(
        @Parameter(
            description = "A HTML/Twig file. CSS should be embedded, images should be embedded using Data URI scheme")
        @RequestParam("template") MultipartFile template,
        @Parameter(description = "A JSON structure with values for placeholders used in template file")
        @RequestParam("placeholderValues") String placeholderValues
    ) {
        LOGGER.debug("Received a PDF generation request");
        byte[] pdfDocument = htmlToPdf.convert(
            new String(asBytes(template), StandardCharsets.UTF_8),
            asMap(placeholderValues));
        LOGGER.debug("PDF generated");
        return ResponseEntity
            .ok()
            .contentLength(pdfDocument.length)
            .body(new ByteArrayResource(pdfDocument));
    }

    private byte[] asBytes(MultipartFile template) {
        if (template.isEmpty()) {
            throw new InvalidArgumentException("Received an empty template file");
        }
        try {
            return template.getBytes();
        } catch (IOException e) {
            throw new InvalidArgumentException(e);
        }
    }

    private Map<String, Object> asMap(String placeholderValues) {
        try {
            return objectMapper.readValue(placeholderValues, MapType.REFERENCE);
        } catch (IOException e) {
            throw new InvalidArgumentException("Unable to successfully parse received JSON string", e);
        }
    }

    private static class MapType extends TypeReference<Map<String, Object>> {
        private static final MapType REFERENCE = new MapType();
    }

}
