package uk.gov.hmcts.reform.cmc.pdf.service.client.http;

import org.springframework.core.io.ByteArrayResource;

public class FileBytesResource extends ByteArrayResource {

    public static final String DEFAULT_FILE_NAME = "template.html";

    private final String fileName;

    public FileBytesResource(byte[] byteArray) {
        this(byteArray, DEFAULT_FILE_NAME);
    }

    public FileBytesResource(byte[] byteArray, String fileName) {
        super(byteArray);
        this.fileName = fileName;
    }

    @Override
    public String getFilename() {
        return fileName;
    }

}
