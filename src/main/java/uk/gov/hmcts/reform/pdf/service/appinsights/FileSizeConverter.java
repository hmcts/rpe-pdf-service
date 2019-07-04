package uk.gov.hmcts.reform.pdf.service.appinsights;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class FileSizeConverter {

    public String convertSize(float fileSize) {
        return new DecimalFormat("0.00").format(fileSize / (1024.0f)) + " Kb";
    }
}
