package org.apache.commons.compress.utils;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/utils/BoundedInputStream.class */
public class BoundedInputStream extends InputStream {
    private final InputStream in;
    private long bytesRemaining;

    public BoundedInputStream(InputStream in, long size) {
        this.in = in;
        this.bytesRemaining = size;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.bytesRemaining > 0) {
            this.bytesRemaining--;
            return this.in.read();
        }
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (this.bytesRemaining == 0) {
            return -1;
        }
        int bytesToRead = len;
        if (bytesToRead > this.bytesRemaining) {
            bytesToRead = (int) this.bytesRemaining;
        }
        int bytesRead = this.in.read(b, off, bytesToRead);
        if (bytesRead >= 0) {
            this.bytesRemaining -= bytesRead;
        }
        return bytesRead;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }
}
