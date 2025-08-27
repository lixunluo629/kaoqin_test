package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.build.AbstractStreamBuilder;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UnsynchronizedFilterInputStream.class */
public class UnsynchronizedFilterInputStream extends InputStream {
    protected volatile InputStream inputStream;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UnsynchronizedFilterInputStream$Builder.class */
    public static class Builder extends AbstractStreamBuilder<UnsynchronizedFilterInputStream, Builder> {
        @Override // org.apache.commons.io.function.IOSupplier
        public UnsynchronizedFilterInputStream get() throws IOException {
            return new UnsynchronizedFilterInputStream(getInputStream());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    UnsynchronizedFilterInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.inputStream.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.inputStream.close();
    }

    @Override // java.io.InputStream
    public void mark(int readLimit) {
        this.inputStream.mark(readLimit);
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.inputStream.markSupported();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.inputStream.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer, int offset, int count) throws IOException {
        return this.inputStream.read(buffer, offset, count);
    }

    @Override // java.io.InputStream
    public void reset() throws IOException {
        this.inputStream.reset();
    }

    @Override // java.io.InputStream
    public long skip(long count) throws IOException {
        return this.inputStream.skip(count);
    }
}
