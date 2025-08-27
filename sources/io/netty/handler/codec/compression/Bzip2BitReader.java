package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Bzip2BitReader.class */
class Bzip2BitReader {
    private static final int MAX_COUNT_OF_READABLE_BYTES = 268435455;
    private ByteBuf in;
    private long bitBuffer;
    private int bitCount;

    Bzip2BitReader() {
    }

    void setByteBuf(ByteBuf in) {
        this.in = in;
    }

    int readBits(int count) {
        long readData;
        int offset;
        if (count < 0 || count > 32) {
            throw new IllegalArgumentException("count: " + count + " (expected: 0-32 )");
        }
        int bitCount = this.bitCount;
        long bitBuffer = this.bitBuffer;
        if (bitCount < count) {
            switch (this.in.readableBytes()) {
                case 1:
                    readData = this.in.readUnsignedByte();
                    offset = 8;
                    break;
                case 2:
                    readData = this.in.readUnsignedShort();
                    offset = 16;
                    break;
                case 3:
                    readData = this.in.readUnsignedMedium();
                    offset = 24;
                    break;
                default:
                    readData = this.in.readUnsignedInt();
                    offset = 32;
                    break;
            }
            bitBuffer = (bitBuffer << offset) | readData;
            bitCount += offset;
            this.bitBuffer = bitBuffer;
        }
        int bitCount2 = bitCount - count;
        this.bitCount = bitCount2;
        return (int) ((bitBuffer >>> bitCount2) & (count != 32 ? (1 << count) - 1 : 4294967295L));
    }

    boolean readBoolean() {
        return readBits(1) != 0;
    }

    int readInt() {
        return readBits(32);
    }

    void refill() {
        int readData = this.in.readUnsignedByte();
        this.bitBuffer = (this.bitBuffer << 8) | readData;
        this.bitCount += 8;
    }

    boolean isReadable() {
        return this.bitCount > 0 || this.in.isReadable();
    }

    boolean hasReadableBits(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count: " + count + " (expected value greater than 0)");
        }
        return this.bitCount >= count || ((this.in.readableBytes() << 3) & Integer.MAX_VALUE) >= count - this.bitCount;
    }

    boolean hasReadableBytes(int count) {
        if (count < 0 || count > MAX_COUNT_OF_READABLE_BYTES) {
            throw new IllegalArgumentException("count: " + count + " (expected: 0-" + MAX_COUNT_OF_READABLE_BYTES + ')');
        }
        return hasReadableBits(count << 3);
    }
}
