package org.apache.commons.io.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/charset/CharsetDecoders.class */
public final class CharsetDecoders {
    public static CharsetDecoder toCharsetDecoder(CharsetDecoder charsetDecoder) {
        return charsetDecoder != null ? charsetDecoder : Charset.defaultCharset().newDecoder();
    }

    private CharsetDecoders() {
    }
}
