package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import java.io.EOFException;
import java.io.IOException;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/SequentialByteArrayReader.class */
public class SequentialByteArrayReader extends SequentialReader {

    @NotNull
    private final byte[] _bytes;
    private int _index;

    @Override // com.drew.lang.SequentialReader
    public long getPosition() {
        return this._index;
    }

    public SequentialByteArrayReader(@NotNull byte[] bytes) {
        this(bytes, 0);
    }

    public SequentialByteArrayReader(@NotNull byte[] bytes, int baseIndex) {
        if (bytes == null) {
            throw new NullPointerException();
        }
        this._bytes = bytes;
        this._index = baseIndex;
    }

    @Override // com.drew.lang.SequentialReader
    public byte getByte() throws IOException {
        if (this._index >= this._bytes.length) {
            throw new EOFException("End of data reached.");
        }
        byte[] bArr = this._bytes;
        int i = this._index;
        this._index = i + 1;
        return bArr[i];
    }

    @Override // com.drew.lang.SequentialReader
    @NotNull
    public byte[] getBytes(int count) throws IOException {
        if (this._index + count > this._bytes.length) {
            throw new EOFException("End of data reached.");
        }
        byte[] bytes = new byte[count];
        System.arraycopy(this._bytes, this._index, bytes, 0, count);
        this._index += count;
        return bytes;
    }

    @Override // com.drew.lang.SequentialReader
    public void getBytes(@NotNull byte[] buffer, int offset, int count) throws IOException {
        if (this._index + count > this._bytes.length) {
            throw new EOFException("End of data reached.");
        }
        System.arraycopy(this._bytes, this._index, buffer, offset, count);
        this._index += count;
    }

    @Override // com.drew.lang.SequentialReader
    public void skip(long n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("n must be zero or greater.");
        }
        if (this._index + n > this._bytes.length) {
            throw new EOFException("End of data reached.");
        }
        this._index = (int) (this._index + n);
    }

    @Override // com.drew.lang.SequentialReader
    public boolean trySkip(long n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("n must be zero or greater.");
        }
        this._index = (int) (this._index + n);
        if (this._index > this._bytes.length) {
            this._index = this._bytes.length;
            return false;
        }
        return true;
    }

    @Override // com.drew.lang.SequentialReader
    public int available() {
        return this._bytes.length - this._index;
    }
}
