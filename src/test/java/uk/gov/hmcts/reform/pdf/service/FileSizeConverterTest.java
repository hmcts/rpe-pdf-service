package uk.gov.hmcts.reform.pdf.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class FileSizeConverterTest {

    private FileSizeConverter fileSizeConverter;

    @Test
    public void shouldConvertForFileSizeInKb() {
        fileSizeConverter = new FileSizeConverter();

        float kbFileSize = 500;
        String kbResult = fileSizeConverter.convertSize(kbFileSize);

        assertThat(kbResult).isEqualToIgnoringCase("0.49 Kb");
    }

    @Test
    public void shouldConvertForFileSizeInMb() {
        fileSizeConverter = new FileSizeConverter();

        float mbFileSize = 1024.0f * 1024.0f + 100;
        String mbResult = fileSizeConverter.convertSize(mbFileSize);

        assertThat(mbResult).isEqualToIgnoringCase("1.00 Mb");
    }

    @Test
    public void shouldConvertForFileSizeInGb() {
        fileSizeConverter = new FileSizeConverter();

        float gbFileSize = 1024.0f * 1024.0f * 1024.0f + 400;
        String gbResult = fileSizeConverter.convertSize(gbFileSize);

        assertThat(gbResult).isEqualToIgnoringCase("1.00 Gb");
    }

    @Test
    public void shouldConvertToEmptyStringForFileSizeTooBig() {
        fileSizeConverter = new FileSizeConverter();

        float tooBigFileSize = 1024.0f * 1024.0f * 1024.0f * 1024.0f;
        String tooBigResult = fileSizeConverter.convertSize(tooBigFileSize);

        assertThat(tooBigResult).isEqualToIgnoringCase("");
    }
}
