package uk.gov.hmcts.reform.pdf.generator.appinsights;

import com.microsoft.applicationinsights.TelemetryClient;

import java.text.DecimalFormat;

import static java.util.Collections.singletonMap;
import static uk.gov.hmcts.reform.pdf.generator.appinsights.AppInsightsEvent.PDF_GENERATOR_FILE_SIZE;

public class AppInsightsEventTracker {

    private static final String FILE_SIZE = "file.size";

    private TelemetryClient telemetry;

    public AppInsightsEventTracker() {
        this.telemetry  = new TelemetryClient();
    }

    public AppInsightsEventTracker(TelemetryClient telemetry) {
        this.telemetry = telemetry;
    }

    public void trackFileSize(float fileSize) {
        telemetry.trackEvent(PDF_GENERATOR_FILE_SIZE.toString(), singletonMap(FILE_SIZE, convertSize(fileSize)), null);
    }

    private String convertSize(float fileSize) {
        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;

        if (fileSize < sizeMb) {
            return df.format(fileSize / sizeKb) + " Kb";
        } else if (fileSize < sizeGb) {
            return df.format(fileSize / sizeMb) + " Mb";
        } else if (fileSize < sizeTerra) {
            return df.format(fileSize / sizeGb) + " Gb";
        }
        return "";
    }
}
