package uk.gov.hmcts.reform.pdf.generator;

import com.microsoft.applicationinsights.TelemetryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import uk.gov.hmcts.reform.pdf.generator.appinsights.AppInsightsEventTracker;
import uk.gov.hmcts.reform.pdf.generator.matchers.EventMetricsMatcher;
import uk.gov.hmcts.reform.pdf.generator.matchers.FileNameEventPropertiesMatcher;
import uk.gov.hmcts.reform.pdf.generator.matchers.GbEventPropertiesMatcher;
import uk.gov.hmcts.reform.pdf.generator.matchers.KbEventPropertiesMatcher;
import uk.gov.hmcts.reform.pdf.generator.matchers.MbEventPropertiesMatcher;

import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class AppInsightsEventTrackerTest{

    private TelemetryClient telemetryMock = mock(TelemetryClient.class);
    private AppInsightsEventTracker tracker;

    @Test
    public void shouldTrackEventForFileSizeInKb(){
        tracker = new AppInsightsEventTracker(telemetryMock);
    //Given
        float kbFileSize = 500;

    //When
        tracker.trackFileSize(kbFileSize);

    //Then
        verify(telemetryMock).trackEvent(
            argThat((String name) -> name.equalsIgnoreCase("PDF File size")),
            argThat(new KbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }

    @Test
    public void shouldTrackEventForFileSizeInMb(){
        tracker = new AppInsightsEventTracker(telemetryMock);
    //Given
        float mbFileSize = (1024.0f*1024.0f) + 100;

    //When
        tracker.trackFileSize(mbFileSize);

    //Then
        verify(telemetryMock).trackEvent(
            argThat((String name) -> name.equalsIgnoreCase("PDF File size")),
            argThat(new MbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }

    @Test
    public void shouldTrackEventForFileSizeInGb(){
        tracker = new AppInsightsEventTracker(telemetryMock);
    //Given
        float gbFileSize = (1024.0f*1024.0f*1024.0f) + 400;

    //When
        tracker.trackFileSize(gbFileSize);

    //Then
        verify(telemetryMock).trackEvent(
            argThat((String name) -> name.equalsIgnoreCase("PDF File size")),
            argThat(new GbEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }

    @Test
    public void shouldTrackEventForFileName(){
        tracker = new AppInsightsEventTracker(telemetryMock);
    //Given
        String fileName = "testFile.txt";

    //When
        tracker.trackFileName(fileName);

    //Then
        verify(telemetryMock).trackEvent(
            argThat((String name) -> name.equalsIgnoreCase("PDF Filename")),
            argThat(new FileNameEventPropertiesMatcher()),
            argThat(new EventMetricsMatcher())
        );
    }

}
