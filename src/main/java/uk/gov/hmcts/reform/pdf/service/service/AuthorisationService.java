package uk.gov.hmcts.reform.pdf.service.service;

import feign.FeignException;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.pdf.service.exception.AuthorisationException;

@Service
public class AuthorisationService {

    public static final String SERVICE_AUTHORISATION_HEADER = "ServiceAuthorization";

    private final ServiceAuthorisationApi serviceAuthorisationApi;

    public AuthorisationService(ServiceAuthorisationApi serviceAuthorisationApi) {
        this.serviceAuthorisationApi = serviceAuthorisationApi;
    }

    /**
     * Authenticates the service.
     * Returns service name on success.
     */
    public void authorise(String serviceAuthHeader) {
        try {
            serviceAuthorisationApi.getServiceName(serviceAuthHeader);
        } catch (FeignException exc) {
            boolean isClientError = exc.status() >= 400 && exc.status() <= 499;

            throw isClientError ? new AuthorisationException(exc.getMessage(), exc) : exc;
        }
    }
}
