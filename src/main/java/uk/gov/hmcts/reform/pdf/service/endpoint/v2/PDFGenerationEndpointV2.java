package uk.gov.hmcts.reform.pdf.service.endpoint.v2;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.pdf.generator.HTMLToPDFConverter;
import uk.gov.hmcts.reform.pdf.service.domain.GeneratePdfRequest;
import uk.gov.hmcts.reform.pdf.service.domain.GeneratePdfResponse;

@Api
@RestController
@RequestMapping(
    path = "pdf",
    produces = PDFGenerationEndpointV2.MEDIA_TYPE
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
    public ResponseEntity<GeneratePdfResponse> generateFromHtml(
        @RequestBody GeneratePdfRequest request
    ) {
        byte[] pdfDocument = htmlToPdf.convert(request.template, request.values);
        log.info("Generated document");
        return ResponseEntity.ok(new GeneratePdfResponse(pdfDocument));
    }
}
