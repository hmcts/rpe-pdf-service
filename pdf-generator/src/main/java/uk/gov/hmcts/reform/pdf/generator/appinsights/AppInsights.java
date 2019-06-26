package uk.gov.hmcts.reform.pdf.generator.appinsights;

import com.microsoft.applicationinsights.TelemetryClient;

import static java.util.Collections.singletonMap;

public class AppInsights {

    public static final String FILE_NAME = "file.name";
    public static final String FILE_SIZE = "file.size";

    private final TelemetryClient telemetry;

    public AppInsights(TelemetryClient telemetry) {
        this.telemetry = telemetry;
    }

    public void trackEvent(AppInsightsEvent appInsightsEvent, String referenceType, String value) {
        telemetry.trackEvent(appInsightsEvent.toString(), singletonMap(referenceType, value), null);
    }

}
