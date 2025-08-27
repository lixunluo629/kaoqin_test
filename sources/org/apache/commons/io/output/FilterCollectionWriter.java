package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.io.IOExceptionList;
import org.apache.commons.io.function.IOConsumer;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/FilterCollectionWriter.class */
public class FilterCollectionWriter extends Writer {
    protected final Collection<Writer> EMPTY_WRITERS = Collections.emptyList();
    protected final Collection<Writer> writers;

    protected FilterCollectionWriter(Collection<Writer> writers) {
        this.writers = writers == null ? this.EMPTY_WRITERS : writers;
    }

    protected FilterCollectionWriter(Writer... writers) {
        this.writers = writers == null ? this.EMPTY_WRITERS : Arrays.asList(writers);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(char c) throws IOException {
        return forAllWriters(w -> {
            w.append(c);
        });
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq) throws IOException {
        return forAllWriters(w -> {
            w.append(csq);
        });
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return forAllWriters(w -> {
            w.append(csq, start, end);
        });
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        forAllWriters((v0) -> {
            v0.close();
        });
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        forAllWriters((v0) -> {
            v0.flush();
        });
    }

    private FilterCollectionWriter forAllWriters(IOConsumer<Writer> action) throws IOExceptionList {
        IOConsumer.forAll(action, writers());
        return this;
    }

    @Override // java.io.Writer
    public void write(char[] cbuf) throws IOException {
        forAllWriters(w -> {
            w.write(cbuf);
        });
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        forAllWriters(w -> {
            w.write(cbuf, off, len);
        });
    }

    @Override // java.io.Writer
    public void write(int c) throws IOException {
        forAllWriters(w -> {
            w.write(c);
        });
    }

    @Override // java.io.Writer
    public void write(String str) throws IOException {
        forAllWriters(w -> {
            w.write(str);
        });
    }

    @Override // java.io.Writer
    public void write(String str, int off, int len) throws IOException {
        forAllWriters(w -> {
            w.write(str, off, len);
        });
    }

    private Stream<Writer> writers() {
        return this.writers.stream().filter((v0) -> {
            return Objects.nonNull(v0);
        });
    }
}
