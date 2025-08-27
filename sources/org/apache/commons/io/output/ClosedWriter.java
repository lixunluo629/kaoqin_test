package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/ClosedWriter.class */
public class ClosedWriter extends Writer {
    public static final ClosedWriter INSTANCE = new ClosedWriter();

    @Deprecated
    public static final ClosedWriter CLOSED_WRITER = INSTANCE;

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        throw new IOException("flush() failed: stream is closed");
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        throw new IOException("write(" + new String(cbuf) + ", " + off + ", " + len + ") failed: stream is closed");
    }
}
