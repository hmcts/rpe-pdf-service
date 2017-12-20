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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.pdf.generator.HTMLToPDFConverter;
import uk.gov.hmcts.reform.pdf.service.domain.GeneratePdfRequest;
import uk.gov.hmcts.reform.pdf.service.service.AuthorisationService;

@Api
@RestController
@RequestMapping(
    path = "pdfs",
    consumes = PDFGenerationEndpointV2.MEDIA_TYPE,
    produces = MediaType.APPLICATION_PDF_VALUE
)
public class PDFGenerationEndpointV2 {

    public static final String MEDIA_TYPE = "application/vnd.uk.gov.hmcts.pdf-service.v2+json;charset=UTF-8";

    private static final Logger log = LoggerFactory.getLogger(PDFGenerationEndpointV2.class);

    private final HTMLToPDFConverter htmlToPdf;
    private final AuthorisationService authorisationService;

    @Autowired
    public PDFGenerationEndpointV2(HTMLToPDFConverter htmlToPdf, AuthorisationService authorisationService) {
        this.htmlToPdf = htmlToPdf;
        this.authorisationService = authorisationService;
    }

    @PostMapping
    public ResponseEntity<ByteArrayResource> generateFromHtml(
        @RequestHeader(AuthorisationService.SERVICE_AUTHORISATION_HEADER) String serviceAuthHeader,
        @RequestBody GeneratePdfRequest request
    ) {
        authorisationService.authorise(serviceAuthHeader);

        byte[] pdfDocument = htmlToPdf.convert(request.template.getBytes(), request.values);
        log.info("Generated document");
        return ResponseEntity.ok(new ByteArrayResource(pdfDocument));
    }
}
