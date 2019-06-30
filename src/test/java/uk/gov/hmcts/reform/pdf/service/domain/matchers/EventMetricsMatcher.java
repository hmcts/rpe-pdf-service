package uk.gov.hmcts.reform.pdf.service.domain.matchers;

import org.mockito.ArgumentMatcher;

import java.util.Map;

public class EventMetricsMatcher implements ArgumentMatcher<Map<String, Double>> {

    @Override
    public String toString() {
        return "[Event Metrics ('Name','Value')]";
    }

    @Override
    public boolean matches(Map<String, Double> argument) {
        return argument == null;
    }
}
