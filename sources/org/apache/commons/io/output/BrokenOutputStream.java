package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;
import org.apache.commons.io.function.Erase;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/BrokenOutputStream.class */
public class BrokenOutputStream extends OutputStream {
    public static final BrokenOutputStream INSTANCE = new BrokenOutputStream();
    private final Supplier<Throwable> exceptionSupplier;

    public BrokenOutputStream() {
        this((Supplier<Throwable>) () -> {
            return new IOException("Broken output stream");
        });
    }

    @Deprecated
    public BrokenOutputStream(IOException exception) {
        this((Supplier<Throwable>) () -> {
            return exception;
        });
    }

    public BrokenOutputStream(Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public BrokenOutputStream(Throwable exception) {
        this((Supplier<Throwable>) () -> {
            return exception;
        });
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        throw rethrow();
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        throw rethrow();
    }

    private RuntimeException rethrow() {
        return Erase.rethrow(this.exceptionSupplier.get());
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        throw rethrow();
    }
}
