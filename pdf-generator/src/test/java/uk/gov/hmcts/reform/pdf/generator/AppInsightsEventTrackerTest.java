package uk.gov.hmcts.reform.pdf.generator;

import com.microsoft.applicationinsights.TelemetryClient;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import uk.gov.hmcts.reform.pdf.generator.appinsights.AppInsights;
import uk.gov.hmcts.reform.pdf.generator.appinsights.AppInsightsEventTracker;


import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class AppInsightsEventTrackerTest {

    private TelemetryClient telemetryMock = mock(TelemetryClient.class);
    private AppInsights appInsightsMock = mock(AppInsights.class);
    private AppInsightsEventTracker tracker;

    @Test
    public void shouldTrackEventForFileSizeInKb(){
        tracker = new AppInsightsEventTracker(telemetryMock, appInsightsMock);
       //Given
        float kbFileSize = 500;

        //When
        tracker.trackFileSize(kbFileSize);

        //Then
        verify(appInsightsMock).trackEvent(
            argThat(new AppInsightsEventFileSizeMatcher()),
            argThat((String referenceType) -> referenceType.equalsIgnoreCase("file.size")),
            argThat((String sizeValue) -> sizeValue.equalsIgnoreCase("0.49 Kb"))
        );
    }

    @Test
    public void shouldTrackEventForFileSizeInMb(){
        tracker = new AppInsightsEventTracker(telemetryMock, appInsightsMock);
        //Given
        float mbFileSize = (1024.0f*1024.0f) + 100;

        //When
        tracker.trackFileSize(mbFileSize);

        //Then
        verify(appInsightsMock).trackEvent(
            argThat(new AppInsightsEventFileSizeMatcher()),
            argThat((String referenceType) -> referenceType.equalsIgnoreCase("file.size")),
            argThat((String sizeValue) -> sizeValue.equalsIgnoreCase("1.00 Mb"))
        );
    }

    @Test
    public void shouldTrackEventForFileSizeInGb(){
        tracker = new AppInsightsEventTracker(telemetryMock, appInsightsMock);
        //Given
        float gbFileSize = (1024.0f*1024.0f*1024.0f) + 400;

        //When
        tracker.trackFileSize(gbFileSize);

        //Then
        verify(appInsightsMock).trackEvent(
            argThat(new AppInsightsEventFileSizeMatcher()),
            argThat((String referenceType) -> referenceType.equalsIgnoreCase("file.size")),
            argThat((String sizeValue) -> sizeValue.equalsIgnoreCase("1.00 Gb"))
        );
    }
}
