package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.SuppressWarnings;
import java.io.IOException;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/ByteArrayReader.class */
public class ByteArrayReader extends RandomAccessReader {

    @NotNull
    private final byte[] _buffer;
    private final int _baseOffset;

    @SuppressWarnings(value = "EI_EXPOSE_REP2", justification = "Design intent")
    public ByteArrayReader(@NotNull byte[] buffer) {
        this(buffer, 0);
    }

    @SuppressWarnings(value = "EI_EXPOSE_REP2", justification = "Design intent")
    public ByteArrayReader(@NotNull byte[] buffer, int baseOffset) {
        if (buffer == null) {
            throw new NullPointerException();
        }
        if (baseOffset < 0) {
            throw new IllegalArgumentException("Must be zero or greater");
        }
        this._buffer = buffer;
        this._baseOffset = baseOffset;
    }

    @Override // com.drew.lang.RandomAccessReader
    public int toUnshiftedOffset(int localOffset) {
        return localOffset + this._baseOffset;
    }

    @Override // com.drew.lang.RandomAccessReader
    public long getLength() {
        return this._buffer.length - this._baseOffset;
    }

    @Override // com.drew.lang.RandomAccessReader
    public byte getByte(int index) throws IOException {
        validateIndex(index, 1);
        return this._buffer[index + this._baseOffset];
    }

    @Override // com.drew.lang.RandomAccessReader
    protected void validateIndex(int index, int bytesRequested) throws IOException {
        if (!isValidIndex(index, bytesRequested)) {
            throw new BufferBoundsException(toUnshiftedOffset(index), bytesRequested, this._buffer.length);
        }
    }

    @Override // com.drew.lang.RandomAccessReader
    protected boolean isValidIndex(int index, int bytesRequested) throws IOException {
        return bytesRequested >= 0 && index >= 0 && (((long) index) + ((long) bytesRequested)) - 1 < getLength();
    }

    @Override // com.drew.lang.RandomAccessReader
    @NotNull
    public byte[] getBytes(int index, int count) throws IOException {
        validateIndex(index, count);
        byte[] bytes = new byte[count];
        System.arraycopy(this._buffer, index + this._baseOffset, bytes, 0, count);
        return bytes;
    }
}
