package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import org.apache.commons.io.build.AbstractStreamBuilder;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UnsynchronizedByteArrayInputStream.class */
public class UnsynchronizedByteArrayInputStream extends InputStream {
    public static final int END_OF_STREAM = -1;
    private final byte[] data;
    private final int eod;
    private int offset;
    private int markedOffset;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UnsynchronizedByteArrayInputStream$Builder.class */
    public static class Builder extends AbstractStreamBuilder<UnsynchronizedByteArrayInputStream, Builder> {
        private int offset;
        private int length;

        @Override // org.apache.commons.io.function.IOSupplier
        public UnsynchronizedByteArrayInputStream get() throws IOException {
            return new UnsynchronizedByteArrayInputStream(checkOrigin().getByteArray(), this.offset, this.length);
        }

        @Override // org.apache.commons.io.build.AbstractOriginSupplier
        public Builder setByteArray(byte[] origin) {
            this.length = ((byte[]) Objects.requireNonNull(origin, "origin")).length;
            return (Builder) super.setByteArray(origin);
        }

        public Builder setLength(int length) {
            if (length < 0) {
                throw new IllegalArgumentException("length cannot be negative");
            }
            this.length = length;
            return this;
        }

        public Builder setOffset(int offset) {
            if (offset < 0) {
                throw new IllegalArgumentException("offset cannot be negative");
            }
            this.offset = offset;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private static int minPosLen(byte[] data, int defaultValue) {
        requireNonNegative(defaultValue, "defaultValue");
        return Math.min(defaultValue, data.length > 0 ? data.length : defaultValue);
    }

    private static int requireNonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative");
        }
        return value;
    }

    @Deprecated
    public UnsynchronizedByteArrayInputStream(byte[] data) {
        this(data, data.length, 0, 0);
    }

    @Deprecated
    public UnsynchronizedByteArrayInputStream(byte[] data, int offset) {
        this(data, data.length, Math.min(requireNonNegative(offset, "offset"), minPosLen(data, offset)), minPosLen(data, offset));
    }

    @Deprecated
    public UnsynchronizedByteArrayInputStream(byte[] data, int offset, int length) {
        requireNonNegative(offset, "offset");
        requireNonNegative(length, "length");
        this.data = (byte[]) Objects.requireNonNull(data, "data");
        this.eod = Math.min(minPosLen(data, offset) + length, data.length);
        this.offset = minPosLen(data, offset);
        this.markedOffset = minPosLen(data, offset);
    }

    private UnsynchronizedByteArrayInputStream(byte[] data, int eod, int offset, int markedOffset) {
        this.data = (byte[]) Objects.requireNonNull(data, "data");
        this.eod = eod;
        this.offset = offset;
        this.markedOffset = markedOffset;
    }

    @Override // java.io.InputStream
    public int available() {
        if (this.offset < this.eod) {
            return this.eod - this.offset;
        }
        return 0;
    }

    @Override // java.io.InputStream
    public void mark(int readLimit) {
        this.markedOffset = this.offset;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.InputStream
    public int read() {
        if (this.offset >= this.eod) {
            return -1;
        }
        byte[] bArr = this.data;
        int i = this.offset;
        this.offset = i + 1;
        return bArr[i] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] dest) {
        Objects.requireNonNull(dest, "dest");
        return read(dest, 0, dest.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] dest, int off, int len) {
        Objects.requireNonNull(dest, "dest");
        if (off < 0 || len < 0 || off + len > dest.length) {
            throw new IndexOutOfBoundsException();
        }
        if (this.offset >= this.eod) {
            return -1;
        }
        int actualLen = this.eod - this.offset;
        if (len < actualLen) {
            actualLen = len;
        }
        if (actualLen <= 0) {
            return 0;
        }
        System.arraycopy(this.data, this.offset, dest, off, actualLen);
        this.offset += actualLen;
        return actualLen;
    }

    @Override // java.io.InputStream
    public void reset() {
        this.offset = this.markedOffset;
    }

    @Override // java.io.InputStream
    public long skip(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("Skipping backward is not supported");
        }
        long actualSkip = this.eod - this.offset;
        if (n < actualSkip) {
            actualSkip = n;
        }
        this.offset = Math.addExact(this.offset, Math.toIntExact(n));
        return actualSkip;
    }
}
