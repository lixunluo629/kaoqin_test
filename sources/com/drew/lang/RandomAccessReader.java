package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.StringValue;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/RandomAccessReader.class */
public abstract class RandomAccessReader {
    private boolean _isMotorolaByteOrder = true;

    public abstract int toUnshiftedOffset(int i);

    public abstract byte getByte(int i) throws IOException;

    @NotNull
    public abstract byte[] getBytes(int i, int i2) throws IOException;

    protected abstract void validateIndex(int i, int i2) throws IOException;

    protected abstract boolean isValidIndex(int i, int i2) throws IOException;

    public abstract long getLength() throws IOException;

    public void setMotorolaByteOrder(boolean motorolaByteOrder) {
        this._isMotorolaByteOrder = motorolaByteOrder;
    }

    public boolean isMotorolaByteOrder() {
        return this._isMotorolaByteOrder;
    }

    public boolean getBit(int index) throws IOException {
        int byteIndex = index / 8;
        int bitIndex = index % 8;
        validateIndex(byteIndex, 1);
        byte b = getByte(byteIndex);
        return ((b >> bitIndex) & 1) == 1;
    }

    public short getUInt8(int index) throws IOException {
        validateIndex(index, 1);
        return (short) (getByte(index) & 255);
    }

    public byte getInt8(int index) throws IOException {
        validateIndex(index, 1);
        return getByte(index);
    }

    public int getUInt16(int index) throws IOException {
        validateIndex(index, 2);
        if (this._isMotorolaByteOrder) {
            return ((getByte(index) << 8) & 65280) | (getByte(index + 1) & 255);
        }
        return ((getByte(index + 1) << 8) & 65280) | (getByte(index) & 255);
    }

    public short getInt16(int index) throws IOException {
        validateIndex(index, 2);
        if (this._isMotorolaByteOrder) {
            return (short) (((getByte(index) << 8) & (-256)) | (getByte(index + 1) & 255));
        }
        return (short) (((getByte(index + 1) << 8) & (-256)) | (getByte(index) & 255));
    }

    public int getInt24(int index) throws IOException {
        validateIndex(index, 3);
        if (this._isMotorolaByteOrder) {
            return ((getByte(index) << 16) & 16711680) | ((getByte(index + 1) << 8) & 65280) | (getByte(index + 2) & 255);
        }
        return ((getByte(index + 2) << 16) & 16711680) | ((getByte(index + 1) << 8) & 65280) | (getByte(index) & 255);
    }

    public long getUInt32(int index) throws IOException {
        validateIndex(index, 4);
        if (this._isMotorolaByteOrder) {
            return ((getByte(index) << 24) & 4278190080L) | ((getByte(index + 1) << 16) & 16711680) | ((getByte(index + 2) << 8) & 65280) | (getByte(index + 3) & 255);
        }
        return ((getByte(index + 3) << 24) & 4278190080L) | ((getByte(index + 2) << 16) & 16711680) | ((getByte(index + 1) << 8) & 65280) | (getByte(index) & 255);
    }

    public int getInt32(int index) throws IOException {
        validateIndex(index, 4);
        if (this._isMotorolaByteOrder) {
            return ((getByte(index) << 24) & (-16777216)) | ((getByte(index + 1) << 16) & 16711680) | ((getByte(index + 2) << 8) & 65280) | (getByte(index + 3) & 255);
        }
        return ((getByte(index + 3) << 24) & (-16777216)) | ((getByte(index + 2) << 16) & 16711680) | ((getByte(index + 1) << 8) & 65280) | (getByte(index) & 255);
    }

    public long getInt64(int index) throws IOException {
        validateIndex(index, 8);
        if (this._isMotorolaByteOrder) {
            return ((getByte(index) << 56) & (-72057594037927936L)) | ((getByte(index + 1) << 48) & 71776119061217280L) | ((getByte(index + 2) << 40) & 280375465082880L) | ((getByte(index + 3) << 32) & 1095216660480L) | ((getByte(index + 4) << 24) & 4278190080L) | ((getByte(index + 5) << 16) & 16711680) | ((getByte(index + 6) << 8) & 65280) | (getByte(index + 7) & 255);
        }
        return ((getByte(index + 7) << 56) & (-72057594037927936L)) | ((getByte(index + 6) << 48) & 71776119061217280L) | ((getByte(index + 5) << 40) & 280375465082880L) | ((getByte(index + 4) << 32) & 1095216660480L) | ((getByte(index + 3) << 24) & 4278190080L) | ((getByte(index + 2) << 16) & 16711680) | ((getByte(index + 1) << 8) & 65280) | (getByte(index) & 255);
    }

    public float getS15Fixed16(int index) throws IOException {
        validateIndex(index, 4);
        if (this._isMotorolaByteOrder) {
            float res = ((getByte(index) & 255) << 8) | (getByte(index + 1) & 255);
            int d = ((getByte(index + 2) & 255) << 8) | (getByte(index + 3) & 255);
            return (float) (res + (d / 65536.0d));
        }
        float res2 = ((getByte(index + 3) & 255) << 8) | (getByte(index + 2) & 255);
        int d2 = ((getByte(index + 1) & 255) << 8) | (getByte(index) & 255);
        return (float) (res2 + (d2 / 65536.0d));
    }

    public float getFloat32(int index) throws IOException {
        return Float.intBitsToFloat(getInt32(index));
    }

    public double getDouble64(int index) throws IOException {
        return Double.longBitsToDouble(getInt64(index));
    }

    @NotNull
    public StringValue getStringValue(int index, int bytesRequested, @Nullable Charset charset) throws IOException {
        return new StringValue(getBytes(index, bytesRequested), charset);
    }

    @NotNull
    public String getString(int index, int bytesRequested, @NotNull Charset charset) throws IOException {
        return new String(getBytes(index, bytesRequested), charset.name());
    }

    @NotNull
    public String getString(int index, int bytesRequested, @NotNull String charset) throws IOException {
        byte[] bytes = getBytes(index, bytesRequested);
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            return new String(bytes);
        }
    }

    @NotNull
    public String getNullTerminatedString(int index, int maxLengthBytes, @NotNull Charset charset) throws IOException {
        return new String(getNullTerminatedBytes(index, maxLengthBytes), charset.name());
    }

    @NotNull
    public StringValue getNullTerminatedStringValue(int index, int maxLengthBytes, @Nullable Charset charset) throws IOException {
        byte[] bytes = getNullTerminatedBytes(index, maxLengthBytes);
        return new StringValue(bytes, charset);
    }

    @NotNull
    public byte[] getNullTerminatedBytes(int index, int maxLengthBytes) throws IOException {
        byte[] buffer = getBytes(index, maxLengthBytes);
        int length = 0;
        while (length < buffer.length && buffer[length] != 0) {
            length++;
        }
        if (length == maxLengthBytes) {
            return buffer;
        }
        byte[] bytes = new byte[length];
        if (length > 0) {
            System.arraycopy(buffer, 0, bytes, 0, length);
        }
        return bytes;
    }
}
