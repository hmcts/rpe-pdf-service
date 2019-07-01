package uk.gov.hmcts.reform.pdf.service.appinsights;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(mbResult).isEqualTo("1.00 Mb");
    }

    @Test
    public void shouldConvertForFileSizeInGb() {
        fileSizeConverter = new FileSizeConverter();

        float gbFileSize = 1024.0f * 1024.0f * 1024.0f + 400;
        String gbResult = fileSizeConverter.convertSize(gbFileSize);

        assertThat(gbResult).isEqualTo("1.00 Gb");
    }

    @Test
    public void shouldConvertToEmptyStringForFileSizeTooBig() {
        fileSizeConverter = new FileSizeConverter();

        float tooBigFileSize = 1024.0f * 1024.0f * 1024.0f * 1024.0f;
        String tooBigResult = fileSizeConverter.convertSize(tooBigFileSize);

        assertThat(tooBigResult).isEqualTo("");
    }
}
