package org.ehcache.impl.internal.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/util/ByteBufferInputStream.class */
public class ByteBufferInputStream extends InputStream {
    private final ByteBuffer buffer;

    public ByteBufferInputStream(ByteBuffer buffer) {
        this.buffer = buffer.slice();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.buffer.hasRemaining()) {
            return 255 & this.buffer.get();
        }
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) {
        int len2 = Math.min(len, this.buffer.remaining());
        this.buffer.get(b, off, len2);
        return len2;
    }

    @Override // java.io.InputStream
    public long skip(long n) {
        long n2 = Math.min(this.buffer.remaining(), Math.max(n, 0L));
        this.buffer.position((int) (this.buffer.position() + n2));
        return n2;
    }

    @Override // java.io.InputStream
    public synchronized int available() {
        return this.buffer.remaining();
    }
}
