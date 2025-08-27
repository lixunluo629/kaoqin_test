package com.itextpdf.io.codec.brotli.dec;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/brotli/dec/BrotliInputStream.class */
public class BrotliInputStream extends InputStream {
    public static final int DEFAULT_INTERNAL_BUFFER_SIZE = 16384;
    private byte[] buffer;
    private int remainingBufferBytes;
    private int bufferOffset;
    private final State state;

    public BrotliInputStream(InputStream source) throws IOException {
        this(source, 16384, null);
    }

    public BrotliInputStream(InputStream source, int byteReadBufferSize) throws IOException {
        this(source, byteReadBufferSize, null);
    }

    public BrotliInputStream(InputStream source, int byteReadBufferSize, byte[] customDictionary) throws IOException {
        this.state = new State();
        if (byteReadBufferSize <= 0) {
            throw new IllegalArgumentException("Bad buffer size:" + byteReadBufferSize);
        }
        if (source == null) {
            throw new IllegalArgumentException("source is null");
        }
        this.buffer = new byte[byteReadBufferSize];
        this.remainingBufferBytes = 0;
        this.bufferOffset = 0;
        try {
            State.setInput(this.state, source);
            if (customDictionary != null) {
                Decode.setCustomDictionary(this.state, customDictionary);
            }
        } catch (BrotliRuntimeException ex) {
            throw new IOException("Brotli decoder initialization failed", ex);
        }
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        State.close(this.state);
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.bufferOffset >= this.remainingBufferBytes) {
            this.remainingBufferBytes = read(this.buffer, 0, this.buffer.length);
            this.bufferOffset = 0;
            if (this.remainingBufferBytes == -1) {
                return -1;
            }
        }
        byte[] bArr = this.buffer;
        int i = this.bufferOffset;
        this.bufferOffset = i + 1;
        return bArr[i] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] destBuffer, int destOffset, int destLen) throws IOException {
        if (destOffset < 0) {
            throw new IllegalArgumentException("Bad offset: " + destOffset);
        }
        if (destLen < 0) {
            throw new IllegalArgumentException("Bad length: " + destLen);
        }
        if (destOffset + destLen > destBuffer.length) {
            throw new IllegalArgumentException("Buffer overflow: " + (destOffset + destLen) + " > " + destBuffer.length);
        }
        if (destLen == 0) {
            return 0;
        }
        int copyLen = Math.max(this.remainingBufferBytes - this.bufferOffset, 0);
        if (copyLen != 0) {
            copyLen = Math.min(copyLen, destLen);
            System.arraycopy(this.buffer, this.bufferOffset, destBuffer, destOffset, copyLen);
            this.bufferOffset += copyLen;
            destOffset += copyLen;
            destLen -= copyLen;
            if (destLen == 0) {
                return copyLen;
            }
        }
        try {
            this.state.output = destBuffer;
            this.state.outputOffset = destOffset;
            this.state.outputLength = destLen;
            this.state.outputUsed = 0;
            Decode.decompress(this.state);
            if (this.state.outputUsed == 0) {
                return -1;
            }
            return this.state.outputUsed + copyLen;
        } catch (BrotliRuntimeException ex) {
            throw new IOException("Brotli stream decoding failed", ex);
        }
    }
}
