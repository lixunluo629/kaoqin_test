package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.SuppressWarnings;
import java.io.IOException;
import java.io.RandomAccessFile;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/RandomAccessFileReader.class */
public class RandomAccessFileReader extends RandomAccessReader {

    @NotNull
    private final RandomAccessFile _file;
    private final long _length;
    private int _currentIndex;
    private final int _baseOffset;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RandomAccessFileReader.class.desiredAssertionStatus();
    }

    @SuppressWarnings(value = "EI_EXPOSE_REP2", justification = "Design intent")
    public RandomAccessFileReader(@NotNull RandomAccessFile file) throws IOException {
        this(file, 0);
    }

    @SuppressWarnings(value = "EI_EXPOSE_REP2", justification = "Design intent")
    public RandomAccessFileReader(@NotNull RandomAccessFile file, int baseOffset) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        }
        this._file = file;
        this._baseOffset = baseOffset;
        this._length = this._file.length();
    }

    @Override // com.drew.lang.RandomAccessReader
    public int toUnshiftedOffset(int localOffset) {
        return localOffset + this._baseOffset;
    }

    @Override // com.drew.lang.RandomAccessReader
    public long getLength() {
        return this._length;
    }

    @Override // com.drew.lang.RandomAccessReader
    public byte getByte(int index) throws IOException {
        if (index != this._currentIndex) {
            seek(index);
        }
        int b = this._file.read();
        if (b < 0) {
            throw new BufferBoundsException("Unexpected end of file encountered.");
        }
        if (!$assertionsDisabled && b > 255) {
            throw new AssertionError();
        }
        this._currentIndex++;
        return (byte) b;
    }

    @Override // com.drew.lang.RandomAccessReader
    @NotNull
    public byte[] getBytes(int index, int count) throws IOException {
        validateIndex(index, count);
        if (index != this._currentIndex) {
            seek(index);
        }
        byte[] bytes = new byte[count];
        int bytesRead = this._file.read(bytes);
        this._currentIndex += bytesRead;
        if (bytesRead != count) {
            throw new BufferBoundsException("Unexpected end of file encountered.");
        }
        return bytes;
    }

    private void seek(int index) throws IOException {
        if (index == this._currentIndex) {
            return;
        }
        this._file.seek(index);
        this._currentIndex = index;
    }

    @Override // com.drew.lang.RandomAccessReader
    protected boolean isValidIndex(int index, int bytesRequested) throws IOException {
        return bytesRequested >= 0 && index >= 0 && (((long) index) + ((long) bytesRequested)) - 1 < this._length;
    }

    @Override // com.drew.lang.RandomAccessReader
    protected void validateIndex(int index, int bytesRequested) throws IOException {
        if (!isValidIndex(index, bytesRequested)) {
            throw new BufferBoundsException(index, bytesRequested, this._length);
        }
    }
}
