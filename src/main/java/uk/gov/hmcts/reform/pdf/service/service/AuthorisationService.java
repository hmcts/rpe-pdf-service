package uk.gov.hmcts.reform.pdf.service.service;

import feign.FeignException;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.pdf.service.client.S2SClient;
import uk.gov.hmcts.reform.pdf.service.exception.AuthorisationException;

@Service
public class AuthorisationService {

    public static final String SERVICE_AUTHORISATION_HEADER = "ServiceAuthorization";

    private final S2SClient s2SClient;

    public AuthorisationService(S2SClient s2SClient) {
        this.s2SClient = s2SClient;
    }

    /**
     * Authenticates the service.
     * Returns service name on success.
     */
    public void authorise(String serviceAuthHeader) {
        try {
            s2SClient.getServiceName(serviceAuthHeader);
        } catch (FeignException exc) {
            boolean isClientError = exc.status() >= 400 && exc.status() <= 499;

            throw isClientError ? new AuthorisationException(exc.getMessage(), exc) : exc;
        }
    }
}
