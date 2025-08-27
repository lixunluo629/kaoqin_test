package org.apache.commons.io.input;

import java.io.EOFException;
import java.io.IOException;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/NullInputStream.class */
public class NullInputStream extends AbstractInputStream {

    @Deprecated
    public static final NullInputStream INSTANCE = new NullInputStream();
    private final long size;
    private long position;
    private long mark;
    private long readLimit;
    private final boolean throwEofException;
    private final boolean markSupported;

    public NullInputStream() {
        this(0L, true, false);
    }

    public NullInputStream(long size) {
        this(size, true, false);
    }

    public NullInputStream(long size, boolean markSupported, boolean throwEofException) {
        this.mark = -1L;
        this.size = size;
        this.markSupported = markSupported;
        this.throwEofException = throwEofException;
    }

    @Override // java.io.InputStream
    public int available() {
        if (isClosed()) {
            return 0;
        }
        long avail = this.size - this.position;
        if (avail <= 0) {
            return 0;
        }
        if (avail > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) avail;
    }

    private void checkThrowEof(String message) throws EOFException {
        if (this.throwEofException) {
            throw new EOFException(message);
        }
    }

    @Override // org.apache.commons.io.input.AbstractInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.mark = -1L;
    }

    public long getPosition() {
        return this.position;
    }

    public long getSize() {
        return this.size;
    }

    private int handleEof() throws IOException {
        checkThrowEof("handleEof()");
        return -1;
    }

    public NullInputStream init() {
        setClosed(false);
        this.position = 0L;
        this.mark = -1L;
        this.readLimit = 0L;
        return this;
    }

    @Override // java.io.InputStream
    public synchronized void mark(int readLimit) {
        if (!this.markSupported) {
            throw UnsupportedOperationExceptions.mark();
        }
        this.mark = this.position;
        this.readLimit = readLimit;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.markSupported;
    }

    protected int processByte() {
        return 0;
    }

    protected void processBytes(byte[] bytes, int offset, int length) {
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        checkOpen();
        if (this.position == this.size) {
            return handleEof();
        }
        this.position++;
        return processByte();
    }

    @Override // java.io.InputStream
    public int read(byte[] bytes) throws IOException {
        return read(bytes, 0, bytes.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] bytes, int offset, int length) throws IOException {
        if (bytes.length == 0 || length == 0) {
            return 0;
        }
        checkOpen();
        if (this.position == this.size) {
            return handleEof();
        }
        this.position += length;
        int returnLength = length;
        if (this.position > this.size) {
            returnLength = length - ((int) (this.position - this.size));
            this.position = this.size;
        }
        processBytes(bytes, offset, returnLength);
        return returnLength;
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        if (!this.markSupported) {
            throw UnsupportedOperationExceptions.reset();
        }
        if (this.mark < 0) {
            throw new IOException("No position has been marked");
        }
        if (this.position > this.mark + this.readLimit) {
            throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readLimit + "]");
        }
        this.position = this.mark;
        setClosed(false);
    }

    @Override // java.io.InputStream
    public long skip(long numberOfBytes) throws IOException {
        if (isClosed()) {
            checkThrowEof("skip(long)");
            return -1L;
        }
        if (this.position == this.size) {
            return handleEof();
        }
        this.position += numberOfBytes;
        long returnLength = numberOfBytes;
        if (this.position > this.size) {
            returnLength = numberOfBytes - (this.position - this.size);
            this.position = this.size;
        }
        return returnLength;
    }
}
