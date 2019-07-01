package uk.gov.hmcts.reform.pdf.service.appinsights;

import com.microsoft.applicationinsights.TelemetryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.pdf.service.appinsights.AppInsightsEventTracker;
import uk.gov.hmcts.reform.pdf.service.appinsights.FileSizeConverter;
import uk.gov.hmcts.reform.pdf.service.domain.matchers.EventMetricsMatcher;
import uk.gov.hmcts.reform.pdf.service.domain.matchers.GbEventPropertiesMatcher;
import uk.gov.hmcts.reform.pdf.service.domain.matchers.KbEventPropertiesMatcher;
import uk.gov.hmcts.reform.pdf.service.domain.matchers.MbEventPropertiesMatcher;
import uk.gov.hmcts.reform.pdf.service.domain.matchers.TbEventPropertiesMatcher;

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

    @Autowired
    private AppInsightsEventTracker tracker;

    private static final String EXPECTED_EVENT_NAME = "PDF File size";

    @Test
    public void shouldTrackEventForFileSizeInKb() {
        tracker = new AppInsightsEventTracker(telemetryClient, fileSizeConverter);

        float kbFileSize = 500;
        when(fileSizeConverter.convertSize(kbFileSize)).thenReturn("0.49 Kb");
        tracker.trackFileSize(kbFileSize);

        verify(telemetryClient).trackEvent(
            argThat((String name) -> EXPECTED_EVENT_NAME.equalsIgnoreCase(name)),
            argThat(new KbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }

    @Test
    public void shouldTrackEventForFileSizeInMb() {
        tracker = new AppInsightsEventTracker(telemetryClient, fileSizeConverter);

        float mbFileSize = 1024.0f * 1024.0f + 100;
        when(fileSizeConverter.convertSize(mbFileSize)).thenReturn("1.00 Mb");
        tracker.trackFileSize(mbFileSize);

        verify(telemetryClient).trackEvent(
            argThat((String name) -> EXPECTED_EVENT_NAME.equalsIgnoreCase(name)),
            argThat(new MbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }

    @Test
    public void shouldTrackEventForFileSizeInGb() {
        tracker = new AppInsightsEventTracker(telemetryClient, fileSizeConverter);

        float gbFileSize = 1024.0f * 1024.0f * 1024.0f + 400;
        when(fileSizeConverter.convertSize(gbFileSize)).thenReturn("1.00 Gb");
        tracker.trackFileSize(gbFileSize);

        verify(telemetryClient).trackEvent(
            argThat((String name) -> EXPECTED_EVENT_NAME.equalsIgnoreCase(name)),
            argThat(new GbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }

    @Test
    public void shouldTrackEventWithEmptyFileSizeWhenFileIsTooBig() {
        tracker = new AppInsightsEventTracker(telemetryClient, fileSizeConverter);

        float tooBigFileSize = 1024.0f * 1024.0f * 1024.0f * 1024.0f;
        when(fileSizeConverter.convertSize(tooBigFileSize)).thenReturn("");
        tracker.trackFileSize(tooBigFileSize);

        verify(telemetryClient).trackEvent(
            argThat((String name) -> EXPECTED_EVENT_NAME.equalsIgnoreCase(name)),
            argThat(new TbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }
}
