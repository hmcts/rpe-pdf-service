package uk.gov.hmcts.reform.cmc.pdf.service.client.util;

import java.util.Objects;

public class Preconditions {

    public static void requireNonEmpty(byte[] array) {
        Objects.requireNonNull(array);

        if (array.length == 0) {
            throw new IllegalArgumentException("Received array was empty");
        }
    }
}
