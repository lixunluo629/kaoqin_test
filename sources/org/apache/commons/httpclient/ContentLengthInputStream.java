package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/ContentLengthInputStream.class */
public class ContentLengthInputStream extends InputStream {
    private long contentLength;
    private long pos;
    private boolean closed;
    private InputStream wrappedStream;

    public ContentLengthInputStream(InputStream in, int contentLength) {
        this(in, contentLength);
    }

    public ContentLengthInputStream(InputStream in, long contentLength) {
        this.pos = 0L;
        this.closed = false;
        this.wrappedStream = null;
        this.wrappedStream = in;
        this.contentLength = contentLength;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            try {
                ChunkedInputStream.exhaustInputStream(this);
                this.closed = true;
            } catch (Throwable th) {
                this.closed = true;
                throw th;
            }
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        }
        if (this.pos >= this.contentLength) {
            return -1;
        }
        this.pos++;
        return this.wrappedStream.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        }
        if (this.pos >= this.contentLength) {
            return -1;
        }
        if (this.pos + len > this.contentLength) {
            len = (int) (this.contentLength - this.pos);
        }
        int count = this.wrappedStream.read(b, off, len);
        this.pos += count;
        return count;
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        long length = this.wrappedStream.skip(Math.min(n, this.contentLength - this.pos));
        if (length > 0) {
            this.pos += length;
        }
        return length;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.closed) {
            return 0;
        }
        int avail = this.wrappedStream.available();
        if (this.pos + avail > this.contentLength) {
            avail = (int) (this.contentLength - this.pos);
        }
        return avail;
    }
}
