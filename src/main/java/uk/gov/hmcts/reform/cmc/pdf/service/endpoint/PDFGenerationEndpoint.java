package uk.gov.hmcts.reform.cmc.pdf.service.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.cmc.pdf.generator.HtmlToPdf;

import java.io.IOException;
import java.util.Map;

@Api
@RestController
@RequestMapping("/api/v1/pdf-generator")
public class PDFGenerationEndpoint {

    private HtmlToPdf htmlToPdf;

    @Autowired
    public PDFGenerationEndpoint(HtmlToPdf htmlToPdf) {
        this.htmlToPdf = htmlToPdf;
    }

    @ApiOperation("Generates and returns PDF file from provided HTML template and placeholder values")
    @PostMapping(
        value = "/html",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<ByteArrayResource> generate(
        @RequestParam("template") MultipartFile template,
        @RequestParam("placeholderValues") String placeholderValues
    ) {
        byte[] result = htmlToPdf.convert(toBytes(template), toMap(placeholderValues));
        return ResponseEntity
            .ok()
            .contentLength(result.length)
            .body(new ByteArrayResource(result));
    }

    private String toBytes(MultipartFile template) {
        try {
            return new String(template.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> toMap(String placeholderValues) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(placeholderValues, new TypeReference<Map<String, Object>>() { });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
