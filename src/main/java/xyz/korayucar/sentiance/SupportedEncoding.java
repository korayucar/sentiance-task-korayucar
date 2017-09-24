package xyz.korayucar.sentiance;

import java.nio.charset.StandardCharsets;

/**
 * Created by koray on 24/09/17.
 */
public enum SupportedEncoding {
    ALPHANUMERIC_UTF_8(StandardCharsets.UTF_8.name());

    private final String charsetName;

    SupportedEncoding(String charsetName) {
        this.charsetName = charsetName;
    }
}
