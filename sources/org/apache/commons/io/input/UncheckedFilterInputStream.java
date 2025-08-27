package org.apache.commons.io.input;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Uncheck;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UncheckedFilterInputStream.class */
public final class UncheckedFilterInputStream extends FilterInputStream {

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UncheckedFilterInputStream$Builder.class */
    public static class Builder extends AbstractStreamBuilder<UncheckedFilterInputStream, Builder> {
        @Override // org.apache.commons.io.function.IOSupplier
        public UncheckedFilterInputStream get() {
            return (UncheckedFilterInputStream) Uncheck.get(() -> {
                return new UncheckedFilterInputStream(getInputStream());
            });
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private UncheckedFilterInputStream(InputStream inputStream) {
        super(inputStream);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws UncheckedIOException {
        return ((Integer) Uncheck.get(() -> {
            return Integer.valueOf(super.available());
        })).intValue();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws UncheckedIOException {
        Uncheck.run(() -> {
            super.close();
        });
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws UncheckedIOException {
        return ((Integer) Uncheck.get(() -> {
            return Integer.valueOf(super.read());
        })).intValue();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b) throws UncheckedIOException {
        return ((Integer) Uncheck.apply(x$0 -> {
            return Integer.valueOf(super.read(x$0));
        }, b)).intValue();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws UncheckedIOException {
        return ((Integer) Uncheck.apply((x$0, x$1, x$2) -> {
            return Integer.valueOf(super.read(x$0, x$1, x$2));
        }, b, Integer.valueOf(off), Integer.valueOf(len))).intValue();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws UncheckedIOException {
        Uncheck.run(() -> {
            super.reset();
        });
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long n) throws UncheckedIOException {
        return ((Long) Uncheck.apply(x$0 -> {
            return Long.valueOf(super.skip(x$0));
        }, Long.valueOf(n))).longValue();
    }
}
