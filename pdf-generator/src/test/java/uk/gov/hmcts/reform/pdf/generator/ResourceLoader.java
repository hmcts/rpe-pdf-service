package uk.gov.hmcts.reform.pdf.generator;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ResourceLoader {

    public static String loadString(String resourcePath) {
        return new String(loadBytes(resourcePath), UTF_8);
    }

    public static byte[] loadBytes(String resourcePath) {
        final URL resource = ResourceLoader.class.getResource(resourcePath);
        try {
            return Files.readAllBytes(Paths.get(resource.toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
