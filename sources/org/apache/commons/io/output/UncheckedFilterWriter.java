package org.apache.commons.io.output;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.Uncheck;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/UncheckedFilterWriter.class */
public final class UncheckedFilterWriter extends FilterWriter {

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/UncheckedFilterWriter$Builder.class */
    public static class Builder extends AbstractStreamBuilder<UncheckedFilterWriter, Builder> {
        @Override // org.apache.commons.io.function.IOSupplier
        public UncheckedFilterWriter get() throws IOException {
            return new UncheckedFilterWriter(getWriter());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private UncheckedFilterWriter(Writer writer) {
        super(writer);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(char c) throws UncheckedIOException {
        return (Writer) Uncheck.apply(x$0 -> {
            return super.append(x$0);
        }, Character.valueOf(c));
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq) throws UncheckedIOException {
        return (Writer) Uncheck.apply(x$0 -> {
            return super.append(x$0);
        }, csq);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq, int start, int end) throws UncheckedIOException {
        return (Writer) Uncheck.apply((x$0, x$1, x$2) -> {
            return super.append(x$0, x$1, x$2);
        }, csq, Integer.valueOf(start), Integer.valueOf(end));
    }

    @Override // java.io.FilterWriter, java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws UncheckedIOException {
        Uncheck.run(() -> {
            super.close();
        });
    }

    @Override // java.io.FilterWriter, java.io.Writer, java.io.Flushable
    public void flush() throws UncheckedIOException {
        Uncheck.run(() -> {
            super.flush();
        });
    }

    @Override // java.io.Writer
    public void write(char[] cbuf) throws UncheckedIOException {
        Uncheck.accept((IOConsumer<char[]>) x$0 -> {
            super.write(x$0);
        }, cbuf);
    }

    @Override // java.io.FilterWriter, java.io.Writer
    public void write(char[] cbuf, int off, int len) throws UncheckedIOException {
        Uncheck.accept((x$0, x$1, x$2) -> {
            super.write(x$0, x$1, x$2);
        }, cbuf, Integer.valueOf(off), Integer.valueOf(len));
    }

    @Override // java.io.FilterWriter, java.io.Writer
    public void write(int c) throws UncheckedIOException {
        Uncheck.accept(x$0 -> {
            super.write(x$0);
        }, c);
    }

    @Override // java.io.Writer
    public void write(String str) throws UncheckedIOException {
        Uncheck.accept((IOConsumer<String>) x$0 -> {
            super.write(x$0);
        }, str);
    }

    @Override // java.io.FilterWriter, java.io.Writer
    public void write(String str, int off, int len) throws UncheckedIOException {
        Uncheck.accept((x$0, x$1, x$2) -> {
            super.write(x$0, x$1, x$2);
        }, str, Integer.valueOf(off), Integer.valueOf(len));
    }
}
