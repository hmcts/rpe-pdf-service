package uk.gov.hmcts.reform.pdf.service;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class FileSizeConverter {

    public String convertSize(float fileSize) {

        if (fileSize < (1024.0f * 1024.0f)) {
            return new DecimalFormat("0.00").format(fileSize / (1024.0f)) + " Kb";
        }

        if (fileSize < (1024.0f * 1024.0f * 1024.0f)) {
            return new DecimalFormat("0.00").format(fileSize / (1024.0f * 1024.0f)) + " Mb";
        }

        if (fileSize < (1024.0f * 1024.0f * 1024.0f * 1024.0f)) {
            return new DecimalFormat("0.00").format(fileSize / (1024.0f * 1024.0f * 1024.0f)) + " Gb";
        }
        return "";
    }
}
