package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/* loaded from: guava-18.0.jar:com/google/common/io/CharSink.class */
public abstract class CharSink {
    public abstract Writer openStream() throws IOException;

    protected CharSink() {
    }

    public Writer openBufferedStream() throws IOException {
        Writer writer = openStream();
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    public void write(CharSequence charSequence) throws Throwable {
        Preconditions.checkNotNull(charSequence);
        Closer closer = Closer.create();
        try {
            try {
                Writer out = (Writer) closer.register(openStream());
                out.append(charSequence);
                out.flush();
                closer.close();
            } catch (Throwable e) {
                throw closer.rethrow(e);
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    public void writeLines(Iterable<? extends CharSequence> lines) throws Throwable {
        writeLines(lines, System.getProperty("line.separator"));
    }

    public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator) throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Preconditions.checkNotNull(lines);
        Preconditions.checkNotNull(lineSeparator);
        Closer closer = Closer.create();
        try {
            try {
                Writer out = (Writer) closer.register(openBufferedStream());
                for (CharSequence line : lines) {
                    out.append(line).append((CharSequence) lineSeparator);
                }
                out.flush();
                closer.close();
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }

    public long writeFrom(Readable readable) throws Throwable {
        RuntimeException runtimeExceptionRethrow;
        Preconditions.checkNotNull(readable);
        Closer closer = Closer.create();
        try {
            try {
                Writer out = (Writer) closer.register(openStream());
                long written = CharStreams.copy(readable, out);
                out.flush();
                closer.close();
                return written;
            } finally {
            }
        } catch (Throwable th) {
            closer.close();
            throw th;
        }
    }
}
