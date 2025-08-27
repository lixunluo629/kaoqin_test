package com.itextpdf.io.font.woff2;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Buffer.class */
class Buffer {
    private byte[] data;
    private int offset;
    private int initial_offset;
    private int length;

    public Buffer(byte[] data, int data_offset, int length) {
        this.offset = 0;
        this.initial_offset = data_offset;
        this.length = length;
        this.data = data;
    }

    public Buffer(Buffer other) {
        this.offset = other.offset;
        this.initial_offset = other.initial_offset;
        this.length = other.length;
        this.data = other.data;
    }

    public int readInt() {
        return readAsNumber(4);
    }

    public short readShort() {
        return JavaUnsignedUtil.toU16(readAsNumber(2));
    }

    public byte readByte() {
        return JavaUnsignedUtil.toU8(readAsNumber(1));
    }

    public void skip(int n_bytes) {
        read(null, 0, n_bytes);
    }

    public void read(byte[] data, int data_offset, int n_bytes) {
        if (this.offset + n_bytes > this.length || this.offset > this.length - n_bytes) {
            throw new FontCompressionException(FontCompressionException.BUFFER_READ_FAILED);
        }
        if (data != null) {
            if (data_offset + n_bytes > data.length || data_offset > data.length - n_bytes) {
                throw new FontCompressionException(FontCompressionException.BUFFER_READ_FAILED);
            }
            System.arraycopy(this.data, this.initial_offset + this.offset, data, data_offset, n_bytes);
        }
        this.offset += n_bytes;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getInitialOffset() {
        return this.initial_offset;
    }

    public int getLength() {
        return this.length;
    }

    private int readAsNumber(int n_bytes) {
        byte[] buffer = new byte[n_bytes];
        read(buffer, 0, n_bytes);
        int result = 0;
        for (int i = 0; i < n_bytes; i++) {
            result = (result << 8) | JavaUnsignedUtil.asU8(buffer[i]);
        }
        return result;
    }
}
