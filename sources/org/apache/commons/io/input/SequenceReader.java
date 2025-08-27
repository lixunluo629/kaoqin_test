package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import org.apache.commons.io.function.Uncheck;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/SequenceReader.class */
public class SequenceReader extends Reader {
    private Reader reader;
    private final Iterator<? extends Reader> readers;

    public SequenceReader(Iterable<? extends Reader> readers) {
        this.readers = ((Iterable) Objects.requireNonNull(readers, "readers")).iterator();
        this.reader = (Reader) Uncheck.get(this::nextReader);
    }

    public SequenceReader(Reader... readers) {
        this(Arrays.asList(readers));
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        while (nextReader() != null) {
        }
    }

    private Reader nextReader() throws IOException {
        if (this.reader != null) {
            this.reader.close();
        }
        if (this.readers.hasNext()) {
            this.reader = this.readers.next();
        } else {
            this.reader = null;
        }
        return this.reader;
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        int c = -1;
        while (this.reader != null) {
            c = this.reader.read();
            if (c != -1) {
                break;
            }
            nextReader();
        }
        return c;
    }

    @Override // java.io.Reader
    public int read(char[] cbuf, int off, int len) throws IOException {
        Objects.requireNonNull(cbuf, "cbuf");
        if (len < 0 || off < 0 || off + len > cbuf.length) {
            throw new IndexOutOfBoundsException("Array Size=" + cbuf.length + ", offset=" + off + ", length=" + len);
        }
        int count = 0;
        while (this.reader != null) {
            int readLen = this.reader.read(cbuf, off, len);
            if (readLen == -1) {
                nextReader();
            } else {
                count += readLen;
                off += readLen;
                len -= readLen;
                if (len <= 0) {
                    break;
                }
            }
        }
        if (count > 0) {
            return count;
        }
        return -1;
    }
}
