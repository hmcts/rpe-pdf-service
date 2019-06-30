package uk.gov.hmcts.reform.pdf.service;

import com.microsoft.applicationinsights.TelemetryClient;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

import static java.util.Collections.singletonMap;
import static uk.gov.hmcts.reform.pdf.service.AppInsightsEvent.PDF_GENERATOR_FILE_SIZE;

@Component
public class AppInsightsEventTracker {

    private static final String FILE_SIZE = "file.size";

    private final TelemetryClient telemetry;

    public AppInsightsEventTracker(TelemetryClient telemetry) {
        this.telemetry = telemetry;
    }

    public void trackFileSize(float fileSize) {
        telemetry.trackEvent(PDF_GENERATOR_FILE_SIZE.toString(), singletonMap(FILE_SIZE, convertSize(fileSize)), null);
    }

    private String convertSize(float fileSize) {

        if (fileSize < (1024.0f * 1024.0f)) {
            return new DecimalFormat("0.00").format(fileSize / (1024.0f)) + " Kb";
        }

        if (fileSize < (1024.0f * 1024.0f * 1024.0f)) {
            return new DecimalFormat("0.00").format(fileSize / (1024.0f * 1024.0f)) + " Mb";
        }

        if (fileSize < (1024.0f * 1024.0f * 1024.0f * 1024.0f)) {
            return new DecimalFormat("0.00").format(fileSize / (1024.0f * 1024.0f * 1024.0f)) + " Gb";
        }
        return "";
    }
}
