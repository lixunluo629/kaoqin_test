package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UnsynchronizedReader.class */
public abstract class UnsynchronizedReader extends Reader {
    private static final int MAX_SKIP_BUFFER_SIZE = 8192;
    private boolean closed;
    private char[] skipBuffer;

    void checkOpen() throws IOException {
        Input.checkOpen(!isClosed());
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.closed = true;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override // java.io.Reader
    public long skip(long n) throws IOException {
        long remaining;
        int countOrEof;
        if (n < 0) {
            throw new IllegalArgumentException("skip value < 0");
        }
        int bufSize = (int) Math.min(n, 8192L);
        if (this.skipBuffer == null || this.skipBuffer.length < bufSize) {
            this.skipBuffer = new char[bufSize];
        }
        long j = n;
        while (true) {
            remaining = j;
            if (remaining <= 0 || (countOrEof = read(this.skipBuffer, 0, (int) Math.min(remaining, bufSize))) == -1) {
                break;
            }
            j = remaining - countOrEof;
        }
        return n - remaining;
    }
}
