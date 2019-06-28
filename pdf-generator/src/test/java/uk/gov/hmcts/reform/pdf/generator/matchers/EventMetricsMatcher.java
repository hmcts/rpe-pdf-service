package uk.gov.hmcts.reform.pdf.generator.matchers;

import org.mockito.ArgumentMatcher;
import java.util.Map;

public class EventMetricsMatcher implements ArgumentMatcher<Map<String, Double>> {

    public String toString(){
        return "[Event Metrics ('Name','Value')]";
    }

    @Override
    public boolean matches(Map<String, Double> argument){
        return argument == null;
    }
}
