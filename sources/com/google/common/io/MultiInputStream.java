package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.annotation.Nullable;

/* loaded from: guava-18.0.jar:com/google/common/io/MultiInputStream.class */
final class MultiInputStream extends InputStream {
    private Iterator<? extends ByteSource> it;
    private InputStream in;

    public MultiInputStream(Iterator<? extends ByteSource> it) throws IOException {
        this.it = (Iterator) Preconditions.checkNotNull(it);
        advance();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.in != null) {
            try {
                this.in.close();
                this.in = null;
            } catch (Throwable th) {
                this.in = null;
                throw th;
            }
        }
    }

    private void advance() throws IOException {
        close();
        if (this.it.hasNext()) {
            this.in = this.it.next().openStream();
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.in == null) {
            return 0;
        }
        return this.in.available();
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.in == null) {
            return -1;
        }
        int result = this.in.read();
        if (result == -1) {
            advance();
            return read();
        }
        return result;
    }

    @Override // java.io.InputStream
    public int read(@Nullable byte[] b, int off, int len) throws IOException {
        if (this.in == null) {
            return -1;
        }
        int result = this.in.read(b, off, len);
        if (result == -1) {
            advance();
            return read(b, off, len);
        }
        return result;
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        if (this.in == null || n <= 0) {
            return 0L;
        }
        long result = this.in.skip(n);
        if (result != 0) {
            return result;
        }
        if (read() == -1) {
            return 0L;
        }
        return 1 + this.in.skip(n - 1);
    }
}
