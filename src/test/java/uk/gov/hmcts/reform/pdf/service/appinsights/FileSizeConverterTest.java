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
    public void shouldConvertForBigFileSizeInKb() {
        fileSizeConverter = new FileSizeConverter();

        float bigFileSize = 1024.0f * 1024.0f * 1024.0f - 100;
        String mbResult = fileSizeConverter.convertSize(bigFileSize);

        assertThat(mbResult).isEqualTo("1048575.88 Kb");
    }

}
