package org.apache.commons.io.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.function.Supplier;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/charset/CharsetEncoders.class */
public final class CharsetEncoders {
    public static CharsetEncoder toCharsetEncoder(CharsetEncoder charsetEncoder) {
        return toCharsetEncoder(charsetEncoder, () -> {
            return Charset.defaultCharset().newEncoder();
        });
    }

    public static CharsetEncoder toCharsetEncoder(CharsetEncoder charsetEncoder, Supplier<CharsetEncoder> defaultSupplier) {
        return charsetEncoder != null ? charsetEncoder : defaultSupplier.get();
    }

    private CharsetEncoders() {
    }
}
