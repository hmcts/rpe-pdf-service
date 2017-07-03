package uk.gov.hmcts.reform.cmc.pdf.service.client.http;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FileBytesResourceTest {

    @Test
    public void shouldReturnDefaultFileNameWhenNotInitialisedWithOne() {
        FileBytesResource resource = new FileBytesResource(new byte[] { });
        assertThat(resource.getFilename()).isEqualTo(FileBytesResource.DEFAULT_FILE_NAME);
    }

    @Test
    public void shouldReturnGivenFileNameWhenInitialisedWithOne() {
        FileBytesResource resource = new FileBytesResource(new byte[] { }, "different.txt");
        assertThat(resource.getFilename()).isEqualTo("different.txt");
    }

}
