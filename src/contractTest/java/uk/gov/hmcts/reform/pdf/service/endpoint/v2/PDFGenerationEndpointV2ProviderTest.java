package uk.gov.hmcts.reform.pdf.service.endpoint.v2;


import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.VersionSelector;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.pdf.generator.HTMLToPDFConverter;
import uk.gov.hmcts.reform.pdf.service.appinsights.AppInsightsEventTracker;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@Provider("rpePdfService_PDFGenerationEndpointV2")
@PactBroker(
    scheme = "${PACT_BROKER_SCHEME:http}",
    host = "${PACT_BROKER_URL:localhost}",
    port = "${PACT_BROKER_PORT:80}",
    consumerVersionSelectors = {
        @VersionSelector(tag = "${PACT_BRANCH_NAME:Dev}")
    })
@IgnoreNoPactsToVerify
public class PDFGenerationEndpointV2ProviderTest {

    PDFGenerationEndpointV2 pdfGenerationEndpointV2;

    @MockBean
    AppInsightsEventTracker appInsightsEventTrackerMock;


    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }


    @BeforeEach
    void before(PactVerificationContext context) {
        System.getProperties().setProperty("pact.verifier.publishResults", "true");
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        pdfGenerationEndpointV2 = new PDFGenerationEndpointV2(new HTMLToPDFConverter(), appInsightsEventTrackerMock);
        testTarget.setControllers(pdfGenerationEndpointV2);
        context.setTarget(testTarget);
    }

    @State({"A request to generate a divorce pdf document"})
    public void toGeneratePdfDivorceDocumentFromTemplate() throws IOException, JSONException {
    }

    @State({"A request to generate a Probate PDF document"})
    public void toGeneratePdfProbateeDocumentFromTemplate() throws IOException, JSONException {
    }

    @State({"A request to generate a pdf document"})
    public void toGeneratePdfDocumentFromTemplate() throws IOException, JSONException {
    }
}
