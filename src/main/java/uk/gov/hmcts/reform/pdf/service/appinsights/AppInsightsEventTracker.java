package uk.gov.hmcts.reform.pdf.service.appinsights;

import com.microsoft.applicationinsights.TelemetryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonMap;
import static uk.gov.hmcts.reform.pdf.service.appinsights.AppInsightsEvent.PDF_GENERATOR_FILE_SIZE;

@Component
public class AppInsightsEventTracker {

    private static final String FILE_SIZE = "file.size";

    private final TelemetryClient telemetry;

    private final FileSizeConverter fileSizeConverter;

    @Autowired
    public AppInsightsEventTracker(TelemetryClient telemetry, FileSizeConverter fileSizeConverter) {
        this.telemetry = telemetry;
        this.fileSizeConverter = fileSizeConverter;
    }

    public void trackFileSize(float fileSize) {
        telemetry.trackEvent(PDF_GENERATOR_FILE_SIZE.toString(),
            singletonMap(FILE_SIZE, fileSizeConverter.convertSize(fileSize)),
            null);
    }
}
