package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Snappy.class */
public final class Snappy {
    private static final int MAX_HT_SIZE = 16384;
    private static final int MIN_COMPRESSIBLE_BYTES = 15;
    private static final int PREAMBLE_NOT_FULL = -1;
    private static final int NOT_ENOUGH_INPUT = -1;
    private static final int LITERAL = 0;
    private static final int COPY_1_BYTE_OFFSET = 1;
    private static final int COPY_2_BYTE_OFFSET = 2;
    private static final int COPY_4_BYTE_OFFSET = 3;
    private State state = State.READY;
    private byte tag;
    private int written;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Snappy$State.class */
    private enum State {
        READY,
        READING_PREAMBLE,
        READING_TAG,
        READING_LITERAL,
        READING_COPY
    }

    public void reset() {
        this.state = State.READY;
        this.tag = (byte) 0;
        this.written = 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x00c2, code lost:
    
        encodeLiteral(r7, r8, r10 - r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00cc, code lost:
    
        r0 = r10;
        r0 = 4 + findMatchingLength(r7, r17 + 4, r10 + 4, r9);
        r10 = r10 + r0;
        r0 = r0 - r17;
        encodeCopy(r8, r0, r0);
        r7.readerIndex(r7.readerIndex() + r0);
        r0 = r10 - 1;
        r14 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0112, code lost:
    
        if (r10 < (r9 - 4)) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0118, code lost:
    
        r0 = hash(r7, r0, r0);
        r0[r0] = (short) ((r10 - r0) - 1);
        r0 = hash(r7, r0 + 1, r0);
        r17 = r0 + r0[r0];
        r0[r0] = (short) (r10 - r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x015e, code lost:
    
        if (r7.getInt(r0 + 1) == r7.getInt(r17)) goto L34;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void encode(io.netty.buffer.ByteBuf r7, io.netty.buffer.ByteBuf r8, int r9) {
        /*
            Method dump skipped, instructions count: 387
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.Snappy.encode(io.netty.buffer.ByteBuf, io.netty.buffer.ByteBuf, int):void");
    }

    private static int hash(ByteBuf in, int index, int shift) {
        return (in.getInt(index) * 506832829) >>> shift;
    }

    private static short[] getHashTable(int inputSize) {
        int htSize;
        int i = 256;
        while (true) {
            htSize = i;
            if (htSize >= 16384 || htSize >= inputSize) {
                break;
            }
            i = htSize << 1;
        }
        return new short[htSize];
    }

    private static int findMatchingLength(ByteBuf in, int minIndex, int inIndex, int maxIndex) {
        int matched = 0;
        while (inIndex <= maxIndex - 4 && in.getInt(inIndex) == in.getInt(minIndex + matched)) {
            inIndex += 4;
            matched += 4;
        }
        while (inIndex < maxIndex && in.getByte(minIndex + matched) == in.getByte(inIndex)) {
            inIndex++;
            matched++;
        }
        return matched;
    }

    private static int bitsToEncode(int value) {
        int highestOneBit = Integer.highestOneBit(value);
        int bitLength = 0;
        while (true) {
            int i = highestOneBit >> 1;
            highestOneBit = i;
            if (i != 0) {
                bitLength++;
            } else {
                return bitLength;
            }
        }
    }

    static void encodeLiteral(ByteBuf in, ByteBuf out, int length) {
        if (length < 61) {
            out.writeByte((length - 1) << 2);
        } else {
            int bitLength = bitsToEncode(length - 1);
            int bytesToEncode = 1 + (bitLength / 8);
            out.writeByte((59 + bytesToEncode) << 2);
            for (int i = 0; i < bytesToEncode; i++) {
                out.writeByte(((length - 1) >> (i * 8)) & 255);
            }
        }
        out.writeBytes(in, length);
    }

    private static void encodeCopyWithOffset(ByteBuf out, int offset, int length) {
        if (length < 12 && offset < 2048) {
            out.writeByte(1 | ((length - 4) << 2) | ((offset >> 8) << 5));
            out.writeByte(offset & 255);
        } else {
            out.writeByte(2 | ((length - 1) << 2));
            out.writeByte(offset & 255);
            out.writeByte((offset >> 8) & 255);
        }
    }

    private static void encodeCopy(ByteBuf out, int offset, int length) {
        while (length >= 68) {
            encodeCopyWithOffset(out, offset, 64);
            length -= 64;
        }
        if (length > 64) {
            encodeCopyWithOffset(out, offset, 60);
            length -= 60;
        }
        encodeCopyWithOffset(out, offset, length);
    }

    /* JADX WARN: Code restructure failed: missing block: B:67:0x0000, code lost:
    
        continue;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0045 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0066 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void decode(io.netty.buffer.ByteBuf r6, io.netty.buffer.ByteBuf r7) {
        /*
            Method dump skipped, instructions count: 373
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.Snappy.decode(io.netty.buffer.ByteBuf, io.netty.buffer.ByteBuf):void");
    }

    private static int readPreamble(ByteBuf in) {
        int length = 0;
        int byteIndex = 0;
        while (in.isReadable()) {
            int current = in.readUnsignedByte();
            int i = byteIndex;
            byteIndex++;
            length |= (current & 127) << (i * 7);
            if ((current & 128) == 0) {
                return length;
            }
            if (byteIndex >= 4) {
                throw new DecompressionException("Preamble is greater than 4 bytes");
            }
        }
        return 0;
    }

    static int decodeLiteral(byte tag, ByteBuf in, ByteBuf out) {
        int length;
        in.markReaderIndex();
        switch ((tag >> 2) & 63) {
            case 60:
                if (!in.isReadable()) {
                    return -1;
                }
                length = in.readUnsignedByte();
                break;
            case 61:
                if (in.readableBytes() < 2) {
                    return -1;
                }
                length = in.readUnsignedShortLE();
                break;
            case 62:
                if (in.readableBytes() < 3) {
                    return -1;
                }
                length = in.readUnsignedMediumLE();
                break;
            case 63:
                if (in.readableBytes() < 4) {
                    return -1;
                }
                length = in.readIntLE();
                break;
            default:
                length = (tag >> 2) & 63;
                break;
        }
        int length2 = length + 1;
        if (in.readableBytes() < length2) {
            in.resetReaderIndex();
            return -1;
        }
        out.writeBytes(in, length2);
        return length2;
    }

    private static int decodeCopyWith1ByteOffset(byte tag, ByteBuf in, ByteBuf out, int writtenSoFar) {
        if (!in.isReadable()) {
            return -1;
        }
        int initialIndex = out.writerIndex();
        int length = 4 + ((tag & 28) >> 2);
        int offset = (((tag & 224) << 8) >> 5) | in.readUnsignedByte();
        validateOffset(offset, writtenSoFar);
        out.markReaderIndex();
        if (offset < length) {
            for (int copies = length / offset; copies > 0; copies--) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, offset);
            }
            if (length % offset != 0) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, length % offset);
            }
        } else {
            out.readerIndex(initialIndex - offset);
            out.readBytes(out, length);
        }
        out.resetReaderIndex();
        return length;
    }

    private static int decodeCopyWith2ByteOffset(byte tag, ByteBuf in, ByteBuf out, int writtenSoFar) {
        if (in.readableBytes() < 2) {
            return -1;
        }
        int initialIndex = out.writerIndex();
        int length = 1 + ((tag >> 2) & 63);
        int offset = in.readUnsignedShortLE();
        validateOffset(offset, writtenSoFar);
        out.markReaderIndex();
        if (offset < length) {
            for (int copies = length / offset; copies > 0; copies--) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, offset);
            }
            if (length % offset != 0) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, length % offset);
            }
        } else {
            out.readerIndex(initialIndex - offset);
            out.readBytes(out, length);
        }
        out.resetReaderIndex();
        return length;
    }

    private static int decodeCopyWith4ByteOffset(byte tag, ByteBuf in, ByteBuf out, int writtenSoFar) {
        if (in.readableBytes() < 4) {
            return -1;
        }
        int initialIndex = out.writerIndex();
        int length = 1 + ((tag >> 2) & 63);
        int offset = in.readIntLE();
        validateOffset(offset, writtenSoFar);
        out.markReaderIndex();
        if (offset < length) {
            for (int copies = length / offset; copies > 0; copies--) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, offset);
            }
            if (length % offset != 0) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, length % offset);
            }
        } else {
            out.readerIndex(initialIndex - offset);
            out.readBytes(out, length);
        }
        out.resetReaderIndex();
        return length;
    }

    private static void validateOffset(int offset, int chunkSizeSoFar) {
        if (offset == 0) {
            throw new DecompressionException("Offset is less than minimum permissible value");
        }
        if (offset < 0) {
            throw new DecompressionException("Offset is greater than maximum value supported by this implementation");
        }
        if (offset > chunkSizeSoFar) {
            throw new DecompressionException("Offset exceeds size of chunk");
        }
    }

    static int calculateChecksum(ByteBuf data) {
        return calculateChecksum(data, data.readerIndex(), data.readableBytes());
    }

    static int calculateChecksum(ByteBuf data, int offset, int length) {
        Crc32c crc32 = new Crc32c();
        try {
            crc32.update(data, offset, length);
            int iMaskChecksum = maskChecksum(crc32.getValue());
            crc32.reset();
            return iMaskChecksum;
        } catch (Throwable th) {
            crc32.reset();
            throw th;
        }
    }

    static void validateChecksum(int expectedChecksum, ByteBuf data) {
        validateChecksum(expectedChecksum, data, data.readerIndex(), data.readableBytes());
    }

    static void validateChecksum(int expectedChecksum, ByteBuf data, int offset, int length) {
        int actualChecksum = calculateChecksum(data, offset, length);
        if (actualChecksum != expectedChecksum) {
            throw new DecompressionException("mismatching checksum: " + Integer.toHexString(actualChecksum) + " (expected: " + Integer.toHexString(expectedChecksum) + ')');
        }
    }

    static int maskChecksum(long checksum) {
        return (int) (((checksum >> 15) | (checksum << 17)) - 1568478504);
    }
}
