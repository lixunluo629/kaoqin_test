package org.apache.commons.io.input.buffer;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import org.apache.commons.io.IOUtils;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/buffer/CircularBufferInputStream.class */
public class CircularBufferInputStream extends FilterInputStream {
    protected final CircularByteBuffer buffer;
    protected final int bufferSize;
    private boolean eof;

    public CircularBufferInputStream(InputStream inputStream) {
        this(inputStream, 8192);
    }

    public CircularBufferInputStream(InputStream inputStream, int bufferSize) {
        super((InputStream) Objects.requireNonNull(inputStream, "inputStream"));
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Illegal bufferSize: " + bufferSize);
        }
        this.buffer = new CircularByteBuffer(bufferSize);
        this.bufferSize = bufferSize;
        this.eof = false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.eof = true;
        this.buffer.clear();
    }

    protected void fillBuffer() throws IOException {
        if (this.eof) {
            return;
        }
        int space = this.buffer.getSpace();
        byte[] buf = IOUtils.byteArray(space);
        while (space > 0) {
            int res = this.in.read(buf, 0, space);
            if (res == -1) {
                this.eof = true;
                return;
            } else if (res > 0) {
                this.buffer.add(buf, 0, res);
                space -= res;
            }
        }
    }

    protected boolean haveBytes(int count) throws IOException {
        if (this.buffer.getCurrentNumberOfBytes() < count) {
            fillBuffer();
        }
        return this.buffer.hasBytes();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (!haveBytes(1)) {
            return -1;
        }
        return this.buffer.read() & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] targetBuffer, int offset, int length) throws IOException {
        Objects.requireNonNull(targetBuffer, "targetBuffer");
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must not be negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative");
        }
        if (!haveBytes(length)) {
            return -1;
        }
        int result = Math.min(length, this.buffer.getCurrentNumberOfBytes());
        for (int i = 0; i < result; i++) {
            targetBuffer[offset + i] = this.buffer.read();
        }
        return result;
    }
}
