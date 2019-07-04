package uk.gov.hmcts.reform.pdf.service.domain.matchers;

import java.util.Map;

public class KbEventPropertiesMatcher extends EventPropertiesMatcher {

    @Override
    public String toString() {
        return "[Event Properties ('Name','Value')]";
    }

    @Override
    public boolean matches(Map<String, String> argument) {
        return argument.containsKey("file.size") && argument.containsValue("0.49 Kb");
    }
}
