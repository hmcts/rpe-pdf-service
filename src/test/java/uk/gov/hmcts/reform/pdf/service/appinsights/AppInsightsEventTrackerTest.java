package uk.gov.hmcts.reform.pdf.service.appinsights;

import com.microsoft.applicationinsights.TelemetryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.pdf.service.domain.matchers.EventMetricsMatcher;
import uk.gov.hmcts.reform.pdf.service.domain.matchers.KbEventPropertiesMatcher;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AppInsightsEventTrackerTest {
    @MockBean
    private TelemetryClient telemetryClient;

    @MockBean
    private FileSizeConverter fileSizeConverter;

    private static final String EXPECTED_EVENT_NAME = "PDF File size";

    @Test
    public void shouldTrackEventForFileSizeInKb() {
        AppInsightsEventTracker tracker = new AppInsightsEventTracker(telemetryClient, fileSizeConverter);

        float kbFileSize = 500;
        when(fileSizeConverter.convertSize(kbFileSize)).thenReturn("0.49 Kb");
        tracker.trackFileSize(kbFileSize);

        verify(telemetryClient).trackEvent(
            argThat((String name) -> EXPECTED_EVENT_NAME.equalsIgnoreCase(name)),
            argThat(new KbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }
}
