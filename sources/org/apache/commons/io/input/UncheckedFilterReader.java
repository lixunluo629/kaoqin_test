package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.CharBuffer;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Uncheck;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UncheckedFilterReader.class */
public final class UncheckedFilterReader extends FilterReader {

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UncheckedFilterReader$Builder.class */
    public static class Builder extends AbstractStreamBuilder<UncheckedFilterReader, Builder> {
        @Override // org.apache.commons.io.function.IOSupplier
        public UncheckedFilterReader get() {
            return (UncheckedFilterReader) Uncheck.get(() -> {
                return new UncheckedFilterReader(getReader());
            });
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private UncheckedFilterReader(Reader reader) {
        super(reader);
    }

    @Override // java.io.FilterReader, java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws UncheckedIOException {
        Uncheck.run(() -> {
            super.close();
        });
    }

    @Override // java.io.FilterReader, java.io.Reader
    public void mark(int readAheadLimit) throws UncheckedIOException {
        Uncheck.accept(x$0 -> {
            super.mark(x$0);
        }, readAheadLimit);
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws UncheckedIOException {
        return ((Integer) Uncheck.get(() -> {
            return Integer.valueOf(super.read());
        })).intValue();
    }

    @Override // java.io.Reader
    public int read(char[] cbuf) throws UncheckedIOException {
        return ((Integer) Uncheck.apply(x$0 -> {
            return Integer.valueOf(super.read(x$0));
        }, cbuf)).intValue();
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read(char[] cbuf, int off, int len) throws UncheckedIOException {
        return ((Integer) Uncheck.apply((x$0, x$1, x$2) -> {
            return Integer.valueOf(super.read(x$0, x$1, x$2));
        }, cbuf, Integer.valueOf(off), Integer.valueOf(len))).intValue();
    }

    @Override // java.io.Reader, java.lang.Readable
    public int read(CharBuffer target) throws UncheckedIOException {
        return ((Integer) Uncheck.apply(x$0 -> {
            return Integer.valueOf(super.read(x$0));
        }, target)).intValue();
    }

    @Override // java.io.FilterReader, java.io.Reader
    public boolean ready() throws UncheckedIOException {
        return ((Boolean) Uncheck.get(() -> {
            return Boolean.valueOf(super.ready());
        })).booleanValue();
    }

    @Override // java.io.FilterReader, java.io.Reader
    public void reset() throws UncheckedIOException {
        Uncheck.run(() -> {
            super.reset();
        });
    }

    @Override // java.io.FilterReader, java.io.Reader
    public long skip(long n) throws UncheckedIOException {
        return ((Long) Uncheck.apply(x$0 -> {
            return Long.valueOf(super.skip(x$0));
        }, Long.valueOf(n))).longValue();
    }
}
