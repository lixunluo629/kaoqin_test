package org.apache.commons.io.input;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/InfiniteCircularInputStream.class */
public class InfiniteCircularInputStream extends CircularInputStream {
    public InfiniteCircularInputStream(byte[] repeatContent) {
        super(repeatContent, -1L);
    }
}
