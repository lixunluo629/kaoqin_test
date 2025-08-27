package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ClosedReader.class */
public class ClosedReader extends Reader {
    public static final ClosedReader INSTANCE = new ClosedReader();

    @Deprecated
    public static final ClosedReader CLOSED_READER = INSTANCE;

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    @Override // java.io.Reader
    public int read(char[] cbuf, int off, int len) {
        return -1;
    }
}
