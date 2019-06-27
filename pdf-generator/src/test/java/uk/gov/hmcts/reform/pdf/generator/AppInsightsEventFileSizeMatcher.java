package uk.gov.hmcts.reform.pdf.generator;

import org.mockito.ArgumentMatcher;
import uk.gov.hmcts.reform.pdf.generator.appinsights.AppInsightsEvent;

public class AppInsightsEventFileSizeMatcher implements ArgumentMatcher<AppInsightsEvent> {
    @Override
    public boolean matches(AppInsightsEvent argument) {
        return argument.toString().equalsIgnoreCase("PDF File size");
    }

    public String toString() {
          return "[PDF File size]";
    }
}
