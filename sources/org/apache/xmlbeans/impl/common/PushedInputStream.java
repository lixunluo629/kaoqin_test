package org.apache.xmlbeans.impl.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/PushedInputStream.class */
public abstract class PushedInputStream extends InputStream {
    private static int defaultBufferSize = 2048;
    protected byte[] buf;
    protected int writepos;
    protected int readpos;
    protected int markpos;
    protected int marklimit;
    protected OutputStream outputStream;

    protected abstract void fill(int i) throws IOException;

    public final OutputStream getOutputStream() {
        return this.outputStream;
    }

    public PushedInputStream() {
        this(defaultBufferSize);
    }

    public PushedInputStream(int size) {
        this.markpos = -1;
        this.outputStream = new InternalOutputStream();
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial buffer size");
        }
        this.buf = new byte[size];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shift(int cb) {
        int savepos = this.readpos;
        if (this.markpos > 0) {
            if (this.readpos - this.markpos > this.marklimit) {
                this.markpos = -1;
            } else {
                savepos = this.markpos;
            }
        }
        int size = this.writepos - savepos;
        if (savepos > 0 && this.buf.length - size >= cb && size <= cb) {
            System.arraycopy(this.buf, savepos, this.buf, 0, size);
        } else {
            int newcount = size + cb;
            byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
            System.arraycopy(this.buf, savepos, newbuf, 0, size);
            this.buf = newbuf;
        }
        if (savepos > 0) {
            this.readpos -= savepos;
            if (this.markpos > 0) {
                this.markpos -= savepos;
            }
            this.writepos -= savepos;
        }
    }

    @Override // java.io.InputStream
    public synchronized int read() throws IOException {
        if (this.readpos >= this.writepos) {
            fill(1);
            if (this.readpos >= this.writepos) {
                return -1;
            }
        }
        byte[] bArr = this.buf;
        int i = this.readpos;
        this.readpos = i + 1;
        return bArr[i] & 255;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        int avail = this.writepos - this.readpos;
        if (avail < len) {
            fill(len - avail);
            avail = this.writepos - this.readpos;
            if (avail <= 0) {
                return -1;
            }
        }
        int cnt = avail < len ? avail : len;
        System.arraycopy(this.buf, this.readpos, b, off, cnt);
        this.readpos += cnt;
        return cnt;
    }

    @Override // java.io.InputStream
    public synchronized long skip(long n) throws IOException {
        if (n <= 0) {
            return 0L;
        }
        long avail = this.writepos - this.readpos;
        if (avail < n) {
            long req = n - avail;
            if (req > 2147483647L) {
                req = 2147483647L;
            }
            fill((int) req);
            avail = this.writepos - this.readpos;
            if (avail <= 0) {
                return 0L;
            }
        }
        long skipped = avail < n ? avail : n;
        this.readpos = (int) (this.readpos + skipped);
        return skipped;
    }

    @Override // java.io.InputStream
    public synchronized int available() {
        return this.writepos - this.readpos;
    }

    @Override // java.io.InputStream
    public synchronized void mark(int readlimit) {
        this.marklimit = readlimit;
        this.markpos = this.readpos;
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        if (this.markpos < 0) {
            throw new IOException("Resetting to invalid mark");
        }
        this.readpos = this.markpos;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/PushedInputStream$InternalOutputStream.class */
    private class InternalOutputStream extends OutputStream {
        private InternalOutputStream() {
        }

        @Override // java.io.OutputStream
        public synchronized void write(int b) throws IOException {
            if (PushedInputStream.this.writepos + 1 > PushedInputStream.this.buf.length) {
                PushedInputStream.this.shift(1);
            }
            PushedInputStream.this.buf[PushedInputStream.this.writepos] = (byte) b;
            PushedInputStream.this.writepos++;
        }

        @Override // java.io.OutputStream
        public synchronized void write(byte[] b, int off, int len) {
            if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (len == 0) {
                return;
            }
            if (PushedInputStream.this.writepos + len > PushedInputStream.this.buf.length) {
                PushedInputStream.this.shift(len);
            }
            System.arraycopy(b, off, PushedInputStream.this.buf, PushedInputStream.this.writepos, len);
            PushedInputStream.this.writepos += len;
        }
    }
}
