package uk.gov.hmcts.reform.pdf.service.endpoint.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.Map;

@Api
@RestController
@RequestMapping("/api/v1/pdf-generator")
public class PDFGenerationEndpoint {

    private static final Logger log = LoggerFactory.getLogger(PDFGenerationEndpoint.class);

    private HTMLToPDFConverter htmlToPdf;
    private ObjectMapper objectMapper;

    @Autowired
    public PDFGenerationEndpoint(HTMLToPDFConverter htmlToPdf, ObjectMapper objectMapper) {
        this.htmlToPdf = htmlToPdf;
        this.objectMapper = objectMapper;
    }

    @APIDeprecated(
        name = "/api/v1/pdf-generator/html",
        docLink = "https://github.com/hmcts/cmc-pdf-service#standard-api",
        expiryDate = "2018-02-08",
        note = "Please use `/pdfs` instead."
    )
    @ApiOperation("Returns a PDF file generated from provided HTML/Twig template and placeholder values")
    @PostMapping(
        value = "/html",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<ByteArrayResource> generateFromHtml(
        @ApiParam("A HTML/Twig file. CSS should be embedded, images should be embedded using Data URI scheme")
        @RequestParam("template") MultipartFile template,
        @ApiParam("A JSON structure with values for placeholders used in template file")
        @RequestParam("placeholderValues") String placeholderValues
    ) {
        log.debug("Received a PDF generation request");
        byte[] pdfDocument = htmlToPdf.convert(asBytes(template), asMap(placeholderValues));
        log.debug("PDF generated");
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
