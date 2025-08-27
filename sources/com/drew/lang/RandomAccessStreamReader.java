package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/RandomAccessStreamReader.class */
public class RandomAccessStreamReader extends RandomAccessReader {
    public static final int DEFAULT_CHUNK_LENGTH = 2048;

    @NotNull
    private final InputStream _stream;
    private final int _chunkLength;
    private final ArrayList<byte[]> _chunks;
    private boolean _isStreamFinished;
    private long _streamLength;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RandomAccessStreamReader.class.desiredAssertionStatus();
    }

    public RandomAccessStreamReader(@NotNull InputStream stream) {
        this(stream, 2048, -1L);
    }

    public RandomAccessStreamReader(@NotNull InputStream stream, int chunkLength) {
        this(stream, chunkLength, -1L);
    }

    public RandomAccessStreamReader(@NotNull InputStream stream, int chunkLength, long streamLength) {
        this._chunks = new ArrayList<>();
        if (stream == null) {
            throw new NullPointerException();
        }
        if (chunkLength <= 0) {
            throw new IllegalArgumentException("chunkLength must be greater than zero");
        }
        this._chunkLength = chunkLength;
        this._stream = stream;
        this._streamLength = streamLength;
    }

    @Override // com.drew.lang.RandomAccessReader
    public long getLength() throws IOException {
        if (this._streamLength != -1) {
            return this._streamLength;
        }
        isValidIndex(Integer.MAX_VALUE, 1);
        if ($assertionsDisabled || this._isStreamFinished) {
            return this._streamLength;
        }
        throw new AssertionError();
    }

    @Override // com.drew.lang.RandomAccessReader
    protected void validateIndex(int index, int bytesRequested) throws IOException {
        if (index < 0) {
            throw new BufferBoundsException(String.format("Attempt to read from buffer using a negative index (%d)", Integer.valueOf(index)));
        }
        if (bytesRequested < 0) {
            throw new BufferBoundsException("Number of requested bytes must be zero or greater");
        }
        if ((index + bytesRequested) - 1 > 2147483647L) {
            throw new BufferBoundsException(String.format("Number of requested bytes summed with starting index exceed maximum range of signed 32 bit integers (requested index: %d, requested count: %d)", Integer.valueOf(index), Integer.valueOf(bytesRequested)));
        }
        if (!isValidIndex(index, bytesRequested)) {
            if (!$assertionsDisabled && !this._isStreamFinished) {
                throw new AssertionError();
            }
            throw new BufferBoundsException(index, bytesRequested, this._streamLength);
        }
    }

    @Override // com.drew.lang.RandomAccessReader
    protected boolean isValidIndex(int index, int bytesRequested) throws IOException {
        if (index < 0 || bytesRequested < 0) {
            return false;
        }
        long endIndexLong = (index + bytesRequested) - 1;
        if (endIndexLong > 2147483647L) {
            return false;
        }
        int endIndex = (int) endIndexLong;
        if (this._isStreamFinished) {
            return ((long) endIndex) < this._streamLength;
        }
        int chunkIndex = endIndex / this._chunkLength;
        while (chunkIndex >= this._chunks.size()) {
            if (!$assertionsDisabled && this._isStreamFinished) {
                throw new AssertionError();
            }
            byte[] chunk = new byte[this._chunkLength];
            int totalBytesRead = 0;
            while (!this._isStreamFinished && totalBytesRead != this._chunkLength) {
                int bytesRead = this._stream.read(chunk, totalBytesRead, this._chunkLength - totalBytesRead);
                if (bytesRead == -1) {
                    this._isStreamFinished = true;
                    int observedStreamLength = (this._chunks.size() * this._chunkLength) + totalBytesRead;
                    if (this._streamLength == -1) {
                        this._streamLength = observedStreamLength;
                    } else if (this._streamLength != observedStreamLength && !$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    if (endIndex >= this._streamLength) {
                        this._chunks.add(chunk);
                        return false;
                    }
                } else {
                    totalBytesRead += bytesRead;
                }
            }
            this._chunks.add(chunk);
        }
        return true;
    }

    @Override // com.drew.lang.RandomAccessReader
    public int toUnshiftedOffset(int localOffset) {
        return localOffset;
    }

    @Override // com.drew.lang.RandomAccessReader
    public byte getByte(int index) throws IOException {
        if (!$assertionsDisabled && index < 0) {
            throw new AssertionError();
        }
        int chunkIndex = index / this._chunkLength;
        int innerIndex = index % this._chunkLength;
        byte[] chunk = this._chunks.get(chunkIndex);
        return chunk[innerIndex];
    }

    @Override // com.drew.lang.RandomAccessReader
    @NotNull
    public byte[] getBytes(int index, int count) throws IOException {
        validateIndex(index, count);
        byte[] bytes = new byte[count];
        int remaining = count;
        int fromIndex = index;
        int i = 0;
        while (true) {
            int toIndex = i;
            if (remaining != 0) {
                int fromChunkIndex = fromIndex / this._chunkLength;
                int fromInnerIndex = fromIndex % this._chunkLength;
                int length = Math.min(remaining, this._chunkLength - fromInnerIndex);
                byte[] chunk = this._chunks.get(fromChunkIndex);
                System.arraycopy(chunk, fromInnerIndex, bytes, toIndex, length);
                remaining -= length;
                fromIndex += length;
                i = toIndex + length;
            } else {
                return bytes;
            }
        }
    }
}
