package uk.gov.hmcts.reform.pdf.service.endpoint.v3;

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
import uk.gov.hmcts.reform.pdf.service.appinsights.AppInsightsEventTracker;
import uk.gov.hmcts.reform.pdf.service.domain.GeneratePdfRequest;

import java.nio.charset.StandardCharsets;

@Api
@RestController
@RequestMapping(
    path = "pdfs",
    consumes = PDFGenerationEndpointV3.MEDIA_TYPE,
    produces = MediaType.APPLICATION_PDF_VALUE
)
public class PDFGenerationEndpointV3 {

    public static final String MEDIA_TYPE = "application/vnd.uk.gov.hmcts.pdf-service.v3+json;charset=UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFGenerationEndpointV3.class);

    private final HTMLToPDFConverter htmlToPdf;

    private final AppInsightsEventTracker eventTracker;

    @Autowired
    public PDFGenerationEndpointV3(HTMLToPDFConverter htmlToPdf, AppInsightsEventTracker eventTracker) {
        this.htmlToPdf = htmlToPdf;
        this.eventTracker = eventTracker;
    }

    @PostMapping
    public ResponseEntity<ByteArrayResource> generateFromHtml(
        @RequestBody GeneratePdfRequest request
    ) {
        // PMD doesn't like the public field
        byte[] pdfDocument = htmlToPdf.convert(
            request.template.getBytes(StandardCharsets.UTF_8), request.values); //NOPMD

        LOGGER.info("Generated document");
        eventTracker.trackFileSize(pdfDocument.length);
        return ResponseEntity.ok(new ByteArrayResource(pdfDocument));
    }
}
