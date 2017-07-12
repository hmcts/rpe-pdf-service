package uk.gov.hmcts.reform.cmc.pdf.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.cmc.pdf.service.client.exception.PDFServiceClientException;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static uk.gov.hmcts.reform.cmc.pdf.service.client.util.Preconditions.requireNonEmpty;

public class PDFServiceClient {

    private static final String GENERATE_FROM_HTML_ENDPOINT_PATH = "/html";
    private static final Logger LOGGER = LoggerFactory.getLogger(PDFServiceClient.class);

    private final RestTemplate restTemplate;
    private final URI pdfServiceBaseUrl;
    private final String version;

    public PDFServiceClient(URI pdfServiceBaseUrl, String version) {
        this.version = version;
        requireNonNull(pdfServiceBaseUrl);
        requireNonNull(version);

        this.restTemplate = new RestTemplate();
        this.pdfServiceBaseUrl = pdfServiceBaseUrl;
    }

    public byte[] generateFromHtml(byte[] template, Map<String, Object> placeholders) {
        requireNonEmpty(template);
        requireNonNull(placeholders);

        try {
            return restTemplate.postForObject(
                htmlEndpoint(),
                requestEntityFor(template, placeholders),
                byte[].class);
        } catch (HttpClientErrorException e) {
            throw new PDFServiceClientException(e);
        }
    }

    /**
     * Calls the PDF service healthcheck.
     * @return health status
     */
    public Health serviceHealthy() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));

            HttpEntity<?> entity = new HttpEntity<Object>("", httpHeaders);

            ResponseEntity<InternalHealth> exchange = restTemplate.exchange(
                pdfServiceBaseUrl.resolve("/health"),
                HttpMethod.GET,
                entity,
                InternalHealth.class);

            InternalHealth body = exchange.getBody();

            return new Health.Builder(body.getStatus())
                .build();
        } catch (Exception ex) {
            LOGGER.error("Error on pdf service healthcheck", ex);
            return Health.down(ex)
                .build();
        }
    }

    private URI htmlEndpoint() {
        return pdfServiceBaseUrl.resolve(
            String.format("/api/%s/pdf-generator%s", version, GENERATE_FROM_HTML_ENDPOINT_PATH)
        );
    }

    private HttpEntity<MultiValueMap<String, Object>> requestEntityFor(
        byte[] template,
        Map<String, Object> placeholders) {

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("template", new FileBytesResource(template));
        formData.add("placeholderValues", placeholders);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new HttpEntity<>(formData, headers);
    }

    /**
     * The 'filename' attribute is needed in multipart/form-data part's Content-Disposition. Otherwise
     * the endpoint will not treat sent bytes as a MultipartFile.
     */
    private static class FileBytesResource extends ByteArrayResource {

        private static final String DEFAULT_FILE_NAME = "template.html";

        private FileBytesResource(byte[] byteArray) {
            super(byteArray);
        }

        @Override
        public String getFilename() {
            return DEFAULT_FILE_NAME;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

    }

}
