package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Supplier;
import org.apache.commons.io.function.Erase;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/BrokenReader.class */
public class BrokenReader extends Reader {
    public static final BrokenReader INSTANCE = new BrokenReader();
    private final Supplier<Throwable> exceptionSupplier;

    public BrokenReader() {
        this((Supplier<Throwable>) () -> {
            return new IOException("Broken reader");
        });
    }

    @Deprecated
    public BrokenReader(IOException exception) {
        this((Supplier<Throwable>) () -> {
            return exception;
        });
    }

    public BrokenReader(Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public BrokenReader(Throwable exception) {
        this((Supplier<Throwable>) () -> {
            return exception;
        });
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        throw rethrow();
    }

    @Override // java.io.Reader
    public void mark(int readAheadLimit) throws IOException {
        throw rethrow();
    }

    @Override // java.io.Reader
    public int read(char[] cbuf, int off, int len) throws IOException {
        throw rethrow();
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        throw rethrow();
    }

    @Override // java.io.Reader
    public void reset() throws IOException {
        throw rethrow();
    }

    private RuntimeException rethrow() {
        return Erase.rethrow(this.exceptionSupplier.get());
    }

    @Override // java.io.Reader
    public long skip(long n) throws IOException {
        throw rethrow();
    }
}
