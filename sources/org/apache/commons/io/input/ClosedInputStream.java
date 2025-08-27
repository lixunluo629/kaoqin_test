package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ClosedInputStream.class */
public class ClosedInputStream extends InputStream {
    public static final ClosedInputStream INSTANCE = new ClosedInputStream();

    @Deprecated
    public static final ClosedInputStream CLOSED_INPUT_STREAM = INSTANCE;

    static InputStream ifNull(InputStream in) {
        return in != null ? in : INSTANCE;
    }

    @Override // java.io.InputStream
    public int read() {
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        return -1;
    }
}
