package uk.gov.hmcts.reform.cmc.pdf.service.client;

import org.springframework.core.io.ByteArrayResource;

public class TemplateResource extends ByteArrayResource {

    public TemplateResource(byte[] byteArray) {
        super(byteArray);
    }

    @Override
    public String getFilename() {
        return "template.html";
    }

}
