package uk.gov.hmcts.reform.pdf.generator.appinsights;

public enum AppInsightsEvent {

    PDF_GENERATOR_FILE_NAME("PDF Filename"),
    PDF_GENERATOR_FILE_SIZE("PDF File size");

    private String displayName;

    AppInsightsEvent(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
