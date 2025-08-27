package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Supplier;
import org.apache.commons.io.function.Erase;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/BrokenWriter.class */
public class BrokenWriter extends Writer {
    public static final BrokenWriter INSTANCE = new BrokenWriter();
    private final Supplier<Throwable> exceptionSupplier;

    public BrokenWriter() {
        this((Supplier<Throwable>) () -> {
            return new IOException("Broken writer");
        });
    }

    @Deprecated
    public BrokenWriter(IOException exception) {
        this((Supplier<Throwable>) () -> {
            return exception;
        });
    }

    public BrokenWriter(Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public BrokenWriter(Throwable exception) {
        this((Supplier<Throwable>) () -> {
            return exception;
        });
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        throw rethrow();
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        throw rethrow();
    }

    private RuntimeException rethrow() {
        return Erase.rethrow(this.exceptionSupplier.get());
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        throw rethrow();
    }
}
