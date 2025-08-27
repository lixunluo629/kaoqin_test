package org.apache.commons.io.input;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.CharBuffer;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Uncheck;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UncheckedBufferedReader.class */
public final class UncheckedBufferedReader extends BufferedReader {

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/UncheckedBufferedReader$Builder.class */
    public static class Builder extends AbstractStreamBuilder<UncheckedBufferedReader, Builder> {
        @Override // org.apache.commons.io.function.IOSupplier
        public UncheckedBufferedReader get() {
            return (UncheckedBufferedReader) Uncheck.get(() -> {
                return new UncheckedBufferedReader(getReader(), getBufferSize());
            });
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private UncheckedBufferedReader(Reader reader, int bufferSize) {
        super(reader, bufferSize);
    }

    @Override // java.io.BufferedReader, java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws UncheckedIOException {
        Uncheck.run(() -> {
            super.close();
        });
    }

    @Override // java.io.BufferedReader, java.io.Reader
    public void mark(int readAheadLimit) throws UncheckedIOException {
        Uncheck.accept(x$0 -> {
            super.mark(x$0);
        }, readAheadLimit);
    }

    @Override // java.io.BufferedReader, java.io.Reader
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

    @Override // java.io.BufferedReader, java.io.Reader
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

    @Override // java.io.BufferedReader
    public String readLine() throws UncheckedIOException {
        return (String) Uncheck.get(() -> {
            return super.readLine();
        });
    }

    @Override // java.io.BufferedReader, java.io.Reader
    public boolean ready() throws UncheckedIOException {
        return ((Boolean) Uncheck.get(() -> {
            return Boolean.valueOf(super.ready());
        })).booleanValue();
    }

    @Override // java.io.BufferedReader, java.io.Reader
    public void reset() throws UncheckedIOException {
        Uncheck.run(() -> {
            super.reset();
        });
    }

    @Override // java.io.BufferedReader, java.io.Reader
    public long skip(long n) throws UncheckedIOException {
        return ((Long) Uncheck.apply(x$0 -> {
            return Long.valueOf(super.skip(x$0));
        }, Long.valueOf(n))).longValue();
    }
}
