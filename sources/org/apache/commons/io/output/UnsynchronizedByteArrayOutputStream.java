package org.apache.commons.io.output;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Uncheck;
import org.apache.commons.io.input.UnsynchronizedByteArrayInputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/UnsynchronizedByteArrayOutputStream.class */
public final class UnsynchronizedByteArrayOutputStream extends AbstractByteArrayOutputStream {

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/UnsynchronizedByteArrayOutputStream$Builder.class */
    public static class Builder extends AbstractStreamBuilder<UnsynchronizedByteArrayOutputStream, Builder> {
        @Override // org.apache.commons.io.function.IOSupplier
        public UnsynchronizedByteArrayOutputStream get() {
            return new UnsynchronizedByteArrayOutputStream(getBufferSize());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static InputStream toBufferedInputStream(InputStream input) throws IOException {
        return toBufferedInputStream(input, 1024);
    }

    public static InputStream toBufferedInputStream(InputStream input, int size) throws IOException {
        UnsynchronizedByteArrayOutputStream output = builder().setBufferSize(size).get();
        try {
            output.write(input);
            InputStream inputStream = output.toInputStream();
            if (output != null) {
                output.close();
            }
            return inputStream;
        } catch (Throwable th) {
            if (output != null) {
                try {
                    output.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    @Deprecated
    public UnsynchronizedByteArrayOutputStream() {
        this(1024);
    }

    @Deprecated
    public UnsynchronizedByteArrayOutputStream(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        needNewBuffer(size);
    }

    @Override // org.apache.commons.io.output.AbstractByteArrayOutputStream
    public void reset() {
        resetImpl();
    }

    @Override // org.apache.commons.io.output.AbstractByteArrayOutputStream
    public int size() {
        return this.count;
    }

    @Override // org.apache.commons.io.output.AbstractByteArrayOutputStream
    public byte[] toByteArray() {
        return toByteArrayImpl();
    }

    @Override // org.apache.commons.io.output.AbstractByteArrayOutputStream
    public InputStream toInputStream() {
        return toInputStream((buffer, offset, length) -> {
            return (UnsynchronizedByteArrayInputStream) Uncheck.get(() -> {
                return UnsynchronizedByteArrayInputStream.builder().setByteArray(buffer).setOffset(offset).setLength(length).get();
            });
        });
    }

    @Override // org.apache.commons.io.output.AbstractByteArrayOutputStream, java.io.OutputStream
    public void write(byte[] b, int off, int len) {
        if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
            throw new IndexOutOfBoundsException(String.format("offset=%,d, length=%,d", Integer.valueOf(off), Integer.valueOf(len)));
        }
        if (len == 0) {
            return;
        }
        writeImpl(b, off, len);
    }

    @Override // org.apache.commons.io.output.AbstractByteArrayOutputStream
    public int write(InputStream in) throws IOException {
        return writeImpl(in);
    }

    @Override // org.apache.commons.io.output.AbstractByteArrayOutputStream, java.io.OutputStream
    public void write(int b) {
        writeImpl(b);
    }

    @Override // org.apache.commons.io.output.AbstractByteArrayOutputStream
    public void writeTo(OutputStream out) throws IOException {
        writeToImpl(out);
    }
}
