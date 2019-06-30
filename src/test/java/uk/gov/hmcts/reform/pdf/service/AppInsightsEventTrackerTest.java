package uk.gov.hmcts.reform.pdf.service;

import com.microsoft.applicationinsights.TelemetryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import uk.gov.hmcts.reform.pdf.service.domain.matchers.EventMetricsMatcher;
import uk.gov.hmcts.reform.pdf.service.domain.matchers.GbEventPropertiesMatcher;
import uk.gov.hmcts.reform.pdf.service.domain.matchers.KbEventPropertiesMatcher;
import uk.gov.hmcts.reform.pdf.service.domain.matchers.MbEventPropertiesMatcher;
import uk.gov.hmcts.reform.pdf.service.domain.matchers.TbEventPropertiesMatcher;

import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class AppInsightsEventTrackerTest {

    private final TelemetryClient telemetryMock = mock(TelemetryClient.class);
    private AppInsightsEventTracker tracker;
    private static final String EXPECTED_EVENT_NAME = "PDF File size";

    @Test
    public void shouldTrackEventForFileSizeInKb() {
        tracker = new AppInsightsEventTracker(telemetryMock);

        float kbFileSize = 500;
        tracker.trackFileSize(kbFileSize);

        verify(telemetryMock).trackEvent(
            argThat((String name) -> EXPECTED_EVENT_NAME.equalsIgnoreCase(name)),
            argThat(new KbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }

    @Test
    public void shouldTrackEventForFileSizeInMb() {
        tracker = new AppInsightsEventTracker(telemetryMock);

        float mbFileSize = 1024.0f * 1024.0f + 100;
        tracker.trackFileSize(mbFileSize);

        verify(telemetryMock).trackEvent(
            argThat((String name) -> EXPECTED_EVENT_NAME.equalsIgnoreCase(name)),
            argThat(new MbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }

    @Test
    public void shouldTrackEventForFileSizeInGb() {
        tracker = new AppInsightsEventTracker(telemetryMock);

        float gbFileSize = 1024.0f * 1024.0f * 1024.0f + 400;
        tracker.trackFileSize(gbFileSize);

        verify(telemetryMock).trackEvent(
            argThat((String name) -> EXPECTED_EVENT_NAME.equalsIgnoreCase(name)),
            argThat(new GbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }

    @Test
    public void shouldTrackEventWithEmptyFileSizeWhenFileIsTooBig() {
        tracker = new AppInsightsEventTracker(telemetryMock);

        float gbFileSize = 1024.0f * 1024.0f * 1024.0f * 1024.0f;
        tracker.trackFileSize(gbFileSize);

        verify(telemetryMock).trackEvent(
            argThat((String name) -> EXPECTED_EVENT_NAME.equalsIgnoreCase(name)),
            argThat(new TbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }
}
