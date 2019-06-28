package uk.gov.hmcts.reform.pdf.generator.matchers;

import java.util.Map;

public class FileNameEventPropertiesMatcher extends EventPropertiesMatcher{

    public String toString(){
        return "[Event Properties ('Name','Value')]";
    }

    @Override
    public boolean matches(Map<String, String> argument){
        return (argument.containsKey("file.name") && argument.containsValue("testFile.txt"));
    }
}
