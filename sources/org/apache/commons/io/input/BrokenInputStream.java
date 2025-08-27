package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;
import org.apache.commons.io.function.Erase;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/BrokenInputStream.class */
public class BrokenInputStream extends InputStream {
    public static final BrokenInputStream INSTANCE = new BrokenInputStream();
    private final Supplier<Throwable> exceptionSupplier;

    public BrokenInputStream() {
        this((Supplier<Throwable>) () -> {
            return new IOException("Broken input stream");
        });
    }

    @Deprecated
    public BrokenInputStream(IOException exception) {
        this((Supplier<Throwable>) () -> {
            return exception;
        });
    }

    public BrokenInputStream(Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public BrokenInputStream(Throwable exception) {
        this((Supplier<Throwable>) () -> {
            return exception;
        });
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        throw rethrow();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        throw rethrow();
    }

    Throwable getThrowable() {
        return this.exceptionSupplier.get();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        throw rethrow();
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        throw rethrow();
    }

    private RuntimeException rethrow() {
        return Erase.rethrow(getThrowable());
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        throw rethrow();
    }
}
