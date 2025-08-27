package org.terracotta.offheapstore.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.InvalidMarkException;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/ByteBufferInputStream.class */
public class ByteBufferInputStream extends InputStream {
    private final ByteBuffer buffer;

    public ByteBufferInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.buffer.hasRemaining()) {
            return this.buffer.get() & 255;
        }
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        int size = Math.min(len, this.buffer.remaining());
        if (size <= 0) {
            return -1;
        }
        this.buffer.get(b, off, size);
        return size;
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        if (n < 0) {
            return 0L;
        }
        long skip = Math.min(n, this.buffer.remaining());
        this.buffer.position((int) (this.buffer.position() + skip));
        return skip;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.buffer.remaining();
    }

    @Override // java.io.InputStream
    public synchronized void mark(int readlimit) {
        this.buffer.mark();
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        try {
            this.buffer.reset();
        } catch (InvalidMarkException e) {
            throw ((IOException) new IOException().initCause(e));
        }
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }
}
