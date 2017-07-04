package uk.gov.hmcts.reform.cmc.pdf.service.client.util;

import java.net.MalformedURLException;
import java.net.URL;

public class Preconditions {

    public static void checkNotNull(Object argument) {
        if (argument == null) {
            throw new NullPointerException("Received null input argument");
        }
    }

    public static void checkNotEmpty(byte[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Received array was empty");
        }
    }

    public static void checkValidURL(String urlString) {
        try {
            new URL(urlString);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Received URL string is not a valid URL", e);
        }
    }

}
