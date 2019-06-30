package uk.gov.hmcts.reform.pdf.service.domain.matchers;

import org.mockito.ArgumentMatcher;

import java.util.Map;

public abstract class EventPropertiesMatcher implements ArgumentMatcher<Map<String, String>> {

    @Override
    public String toString() {
        return "[Event Properties ('Name','Value')]";
    }
}
