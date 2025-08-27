package org.apache.poi.poifs.storage;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/storage/DataInputBlock.class */
public final class DataInputBlock {
    private final byte[] _buf;
    private int _readIndex;
    private int _maxIndex;

    DataInputBlock(byte[] data, int startOffset) {
        this._buf = data;
        this._readIndex = startOffset;
        this._maxIndex = this._buf.length;
    }

    public int available() {
        return this._maxIndex - this._readIndex;
    }

    public int readUByte() {
        byte[] bArr = this._buf;
        int i = this._readIndex;
        this._readIndex = i + 1;
        return bArr[i] & 255;
    }

    public int readUShortLE() {
        int i = this._readIndex;
        int i2 = i + 1;
        int b0 = this._buf[i] & 255;
        int b1 = this._buf[i2] & 255;
        this._readIndex = i2 + 1;
        return (b1 << 8) + (b0 << 0);
    }

    public int readUShortLE(DataInputBlock prevBlock) {
        int i = prevBlock._buf.length - 1;
        int b0 = prevBlock._buf[i] & 255;
        byte[] bArr = this._buf;
        int i2 = this._readIndex;
        this._readIndex = i2 + 1;
        int b1 = bArr[i2] & 255;
        return (b1 << 8) + (b0 << 0);
    }

    public int readIntLE() {
        int i = this._readIndex;
        int i2 = i + 1;
        int b0 = this._buf[i] & 255;
        int i3 = i2 + 1;
        int b1 = this._buf[i2] & 255;
        int i4 = i3 + 1;
        int b2 = this._buf[i3] & 255;
        int b3 = this._buf[i4] & 255;
        this._readIndex = i4 + 1;
        return (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
    }

    public int readIntLE(DataInputBlock prevBlock, int prevBlockAvailable) {
        byte[] buf = new byte[4];
        readSpanning(prevBlock, prevBlockAvailable, buf);
        int b0 = buf[0] & 255;
        int b1 = buf[1] & 255;
        int b2 = buf[2] & 255;
        int b3 = buf[3] & 255;
        return (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
    }

    public long readLongLE() {
        int i = this._readIndex;
        int i2 = i + 1;
        int b0 = this._buf[i] & 255;
        int i3 = i2 + 1;
        int b1 = this._buf[i2] & 255;
        int i4 = i3 + 1;
        int b2 = this._buf[i3] & 255;
        int i5 = i4 + 1;
        int b3 = this._buf[i4] & 255;
        int i6 = i5 + 1;
        int b4 = this._buf[i5] & 255;
        int i7 = i6 + 1;
        int b5 = this._buf[i6] & 255;
        int i8 = i7 + 1;
        int b6 = this._buf[i7] & 255;
        int b7 = this._buf[i8] & 255;
        this._readIndex = i8 + 1;
        return (b7 << 56) + (b6 << 48) + (b5 << 40) + (b4 << 32) + (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
    }

    public long readLongLE(DataInputBlock prevBlock, int prevBlockAvailable) {
        byte[] buf = new byte[8];
        readSpanning(prevBlock, prevBlockAvailable, buf);
        int b0 = buf[0] & 255;
        int b1 = buf[1] & 255;
        int b2 = buf[2] & 255;
        int b3 = buf[3] & 255;
        int b4 = buf[4] & 255;
        int b5 = buf[5] & 255;
        int b6 = buf[6] & 255;
        int b7 = buf[7] & 255;
        return (b7 << 56) + (b6 << 48) + (b5 << 40) + (b4 << 32) + (b3 << 24) + (b2 << 16) + (b1 << 8) + (b0 << 0);
    }

    private void readSpanning(DataInputBlock prevBlock, int prevBlockAvailable, byte[] buf) {
        System.arraycopy(prevBlock._buf, prevBlock._readIndex, buf, 0, prevBlockAvailable);
        int secondReadLen = buf.length - prevBlockAvailable;
        System.arraycopy(this._buf, 0, buf, prevBlockAvailable, secondReadLen);
        this._readIndex = secondReadLen;
    }

    public void readFully(byte[] buf, int off, int len) {
        System.arraycopy(this._buf, this._readIndex, buf, off, len);
        this._readIndex += len;
    }
}
