package uk.gov.hmcts.reform.pdf.generator.matchers;

import org.mockito.ArgumentMatcher;

import java.util.Map;

public abstract class EventPropertiesMatcher implements ArgumentMatcher<Map<String, String>> {

    public String toString() {
        return "[Event Properties ('Name','Value')]";
    }
}
