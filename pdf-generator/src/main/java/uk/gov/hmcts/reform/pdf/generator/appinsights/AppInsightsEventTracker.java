package uk.gov.hmcts.reform.pdf.generator.appinsights;

import com.microsoft.applicationinsights.TelemetryClient;

import java.text.DecimalFormat;

import static uk.gov.hmcts.reform.pdf.generator.appinsights.AppInsightsEvent.PDF_GENERATOR_FILE_SIZE;

public class AppInsightsEventTracker {

    private TelemetryClient telemetry ;
    private AppInsights appInsights;

    public AppInsightsEventTracker() {
        this.telemetry  = new TelemetryClient();
        this.appInsights = new AppInsights(telemetry);
    }

    public AppInsightsEventTracker(TelemetryClient telemetry, AppInsights appInsights) {
        this.telemetry = telemetry;
        this.appInsights = appInsights;
    }

    public void trackFileSize(float fileSize) {
        appInsights.trackEvent(PDF_GENERATOR_FILE_SIZE, AppInsights.FILE_SIZE, convertSize(fileSize));
    }

    private String convertSize(float fileSize) {
        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;

        if(fileSize < sizeMb)
            return df.format(fileSize / sizeKb) + " Kb";
        else if(fileSize < sizeGb)
            return df.format(fileSize / sizeMb) + " Mb";
        else if(fileSize < sizeTerra)
            return df.format(fileSize / sizeGb) + " Gb";

        return "";
    }
}
