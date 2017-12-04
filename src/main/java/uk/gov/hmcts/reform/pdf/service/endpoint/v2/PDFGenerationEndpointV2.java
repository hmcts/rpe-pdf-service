package uk.gov.hmcts.reform.pdf.service.endpoint.v2;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.pdf.generator.HTMLToPDFConverter;
import uk.gov.hmcts.reform.pdf.service.domain.GeneratePdfRequest;

@Api
@RestController
@RequestMapping(
    path = "pdfs",
    consumes = PDFGenerationEndpointV2.MEDIA_TYPE,
    produces = MediaType.APPLICATION_PDF_VALUE
)
public class PDFGenerationEndpointV2 {

    public static final String MEDIA_TYPE = "application/vnd.uk.gov.hmcts.pdf-service.v2+json";

    private static final Logger log = LoggerFactory.getLogger(PDFGenerationEndpointV2.class);

    private final HTMLToPDFConverter htmlToPdf;

    @Autowired
    public PDFGenerationEndpointV2(HTMLToPDFConverter htmlToPdf) {
        this.htmlToPdf = htmlToPdf;
    }

    @PostMapping
    public ResponseEntity<ByteArrayResource> generateFromHtml(
        @RequestBody GeneratePdfRequest request
    ) {
        byte[] pdfDocument = htmlToPdf.convert(request.template.getBytes(), request.values);
        log.info("Generated document");
        return ResponseEntity.ok(new ByteArrayResource(pdfDocument));
    }
}
