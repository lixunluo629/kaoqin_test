package io.netty.handler.codec.compression;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/FastLz.class */
final class FastLz {
    private static final int MAX_DISTANCE = 8191;
    private static final int MAX_FARDISTANCE = 73725;
    private static final int HASH_LOG = 13;
    private static final int HASH_SIZE = 8192;
    private static final int HASH_MASK = 8191;
    private static final int MAX_COPY = 32;
    private static final int MAX_LEN = 264;
    private static final int MIN_RECOMENDED_LENGTH_FOR_LEVEL_2 = 65536;
    static final int MAGIC_NUMBER = 4607066;
    static final byte BLOCK_TYPE_NON_COMPRESSED = 0;
    static final byte BLOCK_TYPE_COMPRESSED = 1;
    static final byte BLOCK_WITHOUT_CHECKSUM = 0;
    static final byte BLOCK_WITH_CHECKSUM = 16;
    static final int OPTIONS_OFFSET = 3;
    static final int CHECKSUM_OFFSET = 4;
    static final int MAX_CHUNK_LENGTH = 65535;
    static final int MIN_LENGTH_TO_COMPRESSION = 32;
    static final int LEVEL_AUTO = 0;
    static final int LEVEL_1 = 1;
    static final int LEVEL_2 = 2;

    static int calculateOutputBufferLength(int inputLength) {
        int outputLength = (int) (inputLength * 1.06d);
        return Math.max(outputLength, 66);
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x0160  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static int compress(byte[] r8, int r9, int r10, byte[] r11, int r12, int r13) {
        /*
            Method dump skipped, instructions count: 1591
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.FastLz.compress(byte[], int, int, byte[], int, int):int");
    }

    static int decompress(byte[] input, int inOffset, int inLength, byte[] output, int outOffset, int outLength) {
        int ref;
        int code;
        int level = (input[inOffset] >> 5) + 1;
        if (level != 1 && level != 2) {
            throw new DecompressionException(String.format("invalid level: %d (expected: %d or %d)", Integer.valueOf(level), 1, 2));
        }
        int op = 0;
        int ip = 0 + 1;
        long ctrl = input[inOffset + 0] & 31;
        int loop = 1;
        do {
            int ref2 = op;
            long len = ctrl >> 5;
            long ofs = (ctrl & 31) << 8;
            if (ctrl >= 32) {
                long len2 = len - 1;
                int ref3 = (int) (ref2 - ofs);
                if (len2 == 6) {
                    if (level == 1) {
                        int i = ip;
                        ip++;
                        len2 += input[inOffset + i] & 255;
                    } else {
                        do {
                            int i2 = ip;
                            ip++;
                            code = input[inOffset + i2] & 255;
                            len2 += code;
                        } while (code == 255);
                    }
                }
                if (level == 1) {
                    int i3 = ip;
                    ip++;
                    ref = ref3 - (input[inOffset + i3] & 255);
                } else {
                    int i4 = ip;
                    ip++;
                    int code2 = input[inOffset + i4] & 255;
                    ref = ref3 - code2;
                    if (code2 == 255 && ofs == 7936) {
                        long ofs2 = (input[inOffset + ip] & 255) << 8;
                        ip = ip + 1 + 1;
                        ref = (int) ((op - (ofs2 + (input[inOffset + r15] & 255))) - 8191);
                    }
                }
                if (op + len2 + 3 > outLength || ref - 1 < 0) {
                    return 0;
                }
                if (ip < inLength) {
                    int i5 = ip;
                    ip++;
                    ctrl = input[inOffset + i5] & 255;
                } else {
                    loop = 0;
                }
                if (ref == op) {
                    byte b = output[(outOffset + ref) - 1];
                    int i6 = op;
                    int op2 = op + 1;
                    output[outOffset + i6] = b;
                    int op3 = op2 + 1;
                    output[outOffset + op2] = b;
                    op = op3 + 1;
                    output[outOffset + op3] = b;
                    while (len2 != 0) {
                        int i7 = op;
                        op++;
                        output[outOffset + i7] = b;
                        len2--;
                    }
                } else {
                    int ref4 = ref - 1;
                    int i8 = op;
                    int op4 = op + 1;
                    int ref5 = ref4 + 1;
                    output[outOffset + i8] = output[outOffset + ref4];
                    int op5 = op4 + 1;
                    int ref6 = ref5 + 1;
                    output[outOffset + op4] = output[outOffset + ref5];
                    op = op5 + 1;
                    int ref7 = ref6 + 1;
                    output[outOffset + op5] = output[outOffset + ref6];
                    while (len2 != 0) {
                        int i9 = op;
                        op++;
                        int i10 = ref7;
                        ref7++;
                        output[outOffset + i9] = output[outOffset + i10];
                        len2--;
                    }
                }
            } else {
                ctrl++;
                if (op + ctrl > outLength || ip + ctrl > inLength) {
                    return 0;
                }
                int i11 = op;
                op++;
                int i12 = ip;
                ip++;
                output[outOffset + i11] = input[inOffset + i12];
                while (true) {
                    ctrl--;
                    if (ctrl == 0) {
                        break;
                    }
                    int i13 = op;
                    op++;
                    int i14 = ip;
                    ip++;
                    output[outOffset + i13] = input[inOffset + i14];
                }
                loop = ip < inLength ? 1 : 0;
                if (loop != 0) {
                    int i15 = ip;
                    ip++;
                    ctrl = input[inOffset + i15] & 255;
                }
            }
        } while (loop != 0);
        return op;
    }

    private static int hashFunction(byte[] p, int offset) {
        int v = readU16(p, offset);
        return (v ^ (readU16(p, offset + 1) ^ (v >> 3))) & 8191;
    }

    private static int readU16(byte[] data, int offset) {
        if (offset + 1 >= data.length) {
            return data[offset] & 255;
        }
        return ((data[offset + 1] & 255) << 8) | (data[offset] & 255);
    }

    private FastLz() {
    }
}
