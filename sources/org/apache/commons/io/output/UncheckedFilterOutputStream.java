package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.Uncheck;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/UncheckedFilterOutputStream.class */
public final class UncheckedFilterOutputStream extends FilterOutputStream {

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/UncheckedFilterOutputStream$Builder.class */
    public static class Builder extends AbstractStreamBuilder<UncheckedFilterOutputStream, Builder> {
        @Override // org.apache.commons.io.function.IOSupplier
        public UncheckedFilterOutputStream get() throws IOException {
            return new UncheckedFilterOutputStream(getOutputStream());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private UncheckedFilterOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws UncheckedIOException {
        Uncheck.run(() -> {
            super.close();
        });
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws UncheckedIOException {
        Uncheck.run(() -> {
            super.flush();
        });
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b) throws UncheckedIOException {
        Uncheck.accept((IOConsumer<byte[]>) x$0 -> {
            super.write(x$0);
        }, b);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b, int off, int len) throws UncheckedIOException {
        Uncheck.accept((x$0, x$1, x$2) -> {
            super.write(x$0, x$1, x$2);
        }, b, Integer.valueOf(off), Integer.valueOf(len));
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int b) throws UncheckedIOException {
        Uncheck.accept(x$0 -> {
            super.write(x$0);
        }, b);
    }
}
