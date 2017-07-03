package uk.gov.hmcts.reform.cmc.pdf.service.client;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public class PDFServiceClient {

    private static final String GENERATE_FROM_HTML_ENDPOINT_PATH = "/html";

    private final RestTemplate restTemplate;
    private final String pdfServiceBaseUrl;

    public PDFServiceClient() {
        this(new RestTemplate(), System.getenv("PDF_SERVICE_URL"));
    }

    public PDFServiceClient(RestTemplate restTemplate, String pdfServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.pdfServiceBaseUrl = pdfServiceBaseUrl;
    }

    public byte[] generateFromHtml(byte[] template, Map<String, Object> placeholders) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        formData.add("template", new TemplateResource(template));
        formData.add("placeholderValues", placeholders);
        System.out.println(new HttpEntity<>(formData, headers));
        return restTemplate.postForObject(htmlEndpoint(), new HttpEntity<>(formData, headers), byte[].class);
    }

    private String htmlEndpoint() {
        return pdfServiceBaseUrl + GENERATE_FROM_HTML_ENDPOINT_PATH;
    }

}
