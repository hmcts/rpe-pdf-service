package uk.gov.hmcts.reform.pdf.service.service;

import feign.FeignException;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.pdf.service.client.S2sClient;
import uk.gov.hmcts.reform.pdf.service.exception.AuthorisationException;

@Service
public class AuthorisationService {

    private final S2sClient s2sClient;

    public AuthorisationService(S2sClient s2sClient) {
        this.s2sClient = s2sClient;
    }

    /**
     * Authenticates the service.
     * Returns service name on success.
     */
    public String authorise(String serviceAuthHeader) {
        try {
            return s2sClient.getServiceName(serviceAuthHeader);
        } catch (FeignException exc) {
            boolean isClientError = exc.status() >= 400 && exc.status() <= 499;

            throw isClientError ? new AuthorisationException(exc.getMessage(), exc) : exc;
        }
    }
}
