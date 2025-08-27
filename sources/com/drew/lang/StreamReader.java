package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/StreamReader.class */
public class StreamReader extends SequentialReader {

    @NotNull
    private final InputStream _stream;
    private long _pos;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StreamReader.class.desiredAssertionStatus();
    }

    @Override // com.drew.lang.SequentialReader
    public long getPosition() {
        return this._pos;
    }

    public StreamReader(@NotNull InputStream stream) {
        if (stream == null) {
            throw new NullPointerException();
        }
        this._stream = stream;
        this._pos = 0L;
    }

    @Override // com.drew.lang.SequentialReader
    public byte getByte() throws IOException {
        int value = this._stream.read();
        if (value == -1) {
            throw new EOFException("End of data reached.");
        }
        this._pos++;
        return (byte) value;
    }

    @Override // com.drew.lang.SequentialReader
    @NotNull
    public byte[] getBytes(int count) throws IOException {
        byte[] bytes = new byte[count];
        getBytes(bytes, 0, count);
        return bytes;
    }

    @Override // com.drew.lang.SequentialReader
    public void getBytes(@NotNull byte[] buffer, int offset, int count) throws IOException {
        int totalBytesRead = 0;
        while (totalBytesRead != count) {
            int bytesRead = this._stream.read(buffer, offset + totalBytesRead, count - totalBytesRead);
            if (bytesRead == -1) {
                throw new EOFException("End of data reached.");
            }
            totalBytesRead += bytesRead;
            if (!$assertionsDisabled && totalBytesRead > count) {
                throw new AssertionError();
            }
        }
        this._pos += totalBytesRead;
    }

    @Override // com.drew.lang.SequentialReader
    public void skip(long n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("n must be zero or greater.");
        }
        long skippedCount = skipInternal(n);
        if (skippedCount != n) {
            throw new EOFException(String.format("Unable to skip. Requested %d bytes but skipped %d.", Long.valueOf(n), Long.valueOf(skippedCount)));
        }
    }

    @Override // com.drew.lang.SequentialReader
    public boolean trySkip(long n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("n must be zero or greater.");
        }
        return skipInternal(n) == n;
    }

    @Override // com.drew.lang.SequentialReader
    public int available() {
        try {
            return this._stream.available();
        } catch (IOException e) {
            return 0;
        }
    }

    private long skipInternal(long n) throws IOException {
        long skippedTotal = 0;
        while (skippedTotal != n) {
            long skipped = this._stream.skip(n - skippedTotal);
            if (!$assertionsDisabled && skipped < 0) {
                throw new AssertionError();
            }
            skippedTotal += skipped;
            if (skipped == 0) {
                break;
            }
        }
        this._pos += skippedTotal;
        return skippedTotal;
    }
}
