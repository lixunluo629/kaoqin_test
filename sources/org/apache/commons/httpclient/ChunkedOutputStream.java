package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.httpclient.util.EncodingUtil;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/ChunkedOutputStream.class */
public class ChunkedOutputStream extends OutputStream {
    private static final byte[] CRLF = {13, 10};
    private static final byte[] ENDCHUNK = CRLF;
    private static final byte[] ZERO = {48};
    private OutputStream stream;
    private byte[] cache;
    private int cachePosition;
    private boolean wroteLastChunk;

    public ChunkedOutputStream(OutputStream stream, int bufferSize) throws IOException {
        this.stream = null;
        this.cachePosition = 0;
        this.wroteLastChunk = false;
        this.cache = new byte[bufferSize];
        this.stream = stream;
    }

    public ChunkedOutputStream(OutputStream stream) throws IOException {
        this(stream, 2048);
    }

    protected void flushCache() throws IOException {
        if (this.cachePosition > 0) {
            byte[] chunkHeader = EncodingUtil.getAsciiBytes(new StringBuffer().append(Integer.toHexString(this.cachePosition)).append("\r\n").toString());
            this.stream.write(chunkHeader, 0, chunkHeader.length);
            this.stream.write(this.cache, 0, this.cachePosition);
            this.stream.write(ENDCHUNK, 0, ENDCHUNK.length);
            this.cachePosition = 0;
        }
    }

    protected void flushCacheWithAppend(byte[] bufferToAppend, int off, int len) throws IOException {
        byte[] chunkHeader = EncodingUtil.getAsciiBytes(new StringBuffer().append(Integer.toHexString(this.cachePosition + len)).append("\r\n").toString());
        this.stream.write(chunkHeader, 0, chunkHeader.length);
        this.stream.write(this.cache, 0, this.cachePosition);
        this.stream.write(bufferToAppend, off, len);
        this.stream.write(ENDCHUNK, 0, ENDCHUNK.length);
        this.cachePosition = 0;
    }

    protected void writeClosingChunk() throws IOException {
        this.stream.write(ZERO, 0, ZERO.length);
        this.stream.write(CRLF, 0, CRLF.length);
        this.stream.write(ENDCHUNK, 0, ENDCHUNK.length);
    }

    public void finish() throws IOException {
        if (!this.wroteLastChunk) {
            flushCache();
            writeClosingChunk();
            this.wroteLastChunk = true;
        }
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.cache[this.cachePosition] = (byte) b;
        this.cachePosition++;
        if (this.cachePosition == this.cache.length) {
            flushCache();
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] src, int off, int len) throws IOException {
        if (len >= this.cache.length - this.cachePosition) {
            flushCacheWithAppend(src, off, len);
        } else {
            System.arraycopy(src, off, this.cache, this.cachePosition, len);
            this.cachePosition += len;
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.stream.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        finish();
        super.close();
    }
}
