package com.itextpdf.io.codec.brotli.dec;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/brotli/dec/BitReader.class */
final class BitReader {
    private static final int CAPACITY = 1024;
    private static final int SLACK = 16;
    private static final int INT_BUFFER_SIZE = 1040;
    private static final int BYTE_READ_SIZE = 4096;
    private static final int BYTE_BUFFER_SIZE = 4160;
    private InputStream input;
    private boolean endOfStreamReached;
    long accumulator;
    int bitOffset;
    private int intOffset;
    private final byte[] byteBuffer = new byte[BYTE_BUFFER_SIZE];
    private final int[] intBuffer = new int[1040];
    private final IntReader intReader = new IntReader();
    private int tailBytes = 0;

    BitReader() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0063, code lost:
    
        r6.endOfStreamReached = true;
        r6.tailBytes = r8;
        r8 = r8 + 3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void readMoreInput(com.itextpdf.io.codec.brotli.dec.BitReader r6) throws java.io.IOException {
        /*
            r0 = r6
            int r0 = r0.intOffset
            r1 = 1015(0x3f7, float:1.422E-42)
            if (r0 > r1) goto Lb
            return
        Lb:
            r0 = r6
            boolean r0 = r0.endOfStreamReached
            if (r0 == 0) goto L26
            r0 = r6
            int r0 = intAvailable(r0)
            r1 = -2
            if (r0 < r1) goto L1c
            return
        L1c:
            com.itextpdf.io.codec.brotli.dec.BrotliRuntimeException r0 = new com.itextpdf.io.codec.brotli.dec.BrotliRuntimeException
            r1 = r0
            java.lang.String r2 = "No more input"
            r1.<init>(r2)
            throw r0
        L26:
            r0 = r6
            int r0 = r0.intOffset
            r1 = 2
            int r0 = r0 << r1
            r7 = r0
            r0 = 4096(0x1000, float:5.74E-42)
            r1 = r7
            int r0 = r0 - r1
            r8 = r0
            r0 = r6
            byte[] r0 = r0.byteBuffer
            r1 = r7
            r2 = r6
            byte[] r2 = r2.byteBuffer
            r3 = 0
            r4 = r8
            java.lang.System.arraycopy(r0, r1, r2, r3, r4)
            r0 = r6
            r1 = 0
            r0.intOffset = r1
        L46:
            r0 = r8
            r1 = 4096(0x1000, float:5.74E-42)
            if (r0 >= r1) goto L7a
            r0 = r6
            java.io.InputStream r0 = r0.input     // Catch: java.io.IOException -> L7d
            r1 = r6
            byte[] r1 = r1.byteBuffer     // Catch: java.io.IOException -> L7d
            r2 = r8
            r3 = 4096(0x1000, float:5.74E-42)
            r4 = r8
            int r3 = r3 - r4
            int r0 = r0.read(r1, r2, r3)     // Catch: java.io.IOException -> L7d
            r9 = r0
            r0 = r9
            if (r0 > 0) goto L73
            r0 = r6
            r1 = 1
            r0.endOfStreamReached = r1     // Catch: java.io.IOException -> L7d
            r0 = r6
            r1 = r8
            r0.tailBytes = r1     // Catch: java.io.IOException -> L7d
            int r8 = r8 + 3
            goto L7a
        L73:
            r0 = r8
            r1 = r9
            int r0 = r0 + r1
            r8 = r0
            goto L46
        L7a:
            goto L89
        L7d:
            r9 = move-exception
            com.itextpdf.io.codec.brotli.dec.BrotliRuntimeException r0 = new com.itextpdf.io.codec.brotli.dec.BrotliRuntimeException
            r1 = r0
            java.lang.String r2 = "Failed to read input"
            r3 = r9
            r1.<init>(r2, r3)
            throw r0
        L89:
            r0 = r6
            com.itextpdf.io.codec.brotli.dec.IntReader r0 = r0.intReader
            r1 = r8
            r2 = 2
            int r1 = r1 >> r2
            com.itextpdf.io.codec.brotli.dec.IntReader.convert(r0, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.io.codec.brotli.dec.BitReader.readMoreInput(com.itextpdf.io.codec.brotli.dec.BitReader):void");
    }

    static void checkHealth(BitReader br, boolean endOfStream) {
        if (!br.endOfStreamReached) {
            return;
        }
        int byteOffset = ((br.intOffset << 2) + ((br.bitOffset + 7) >> 3)) - 8;
        if (byteOffset > br.tailBytes) {
            throw new BrotliRuntimeException("Read after end");
        }
        if (endOfStream && byteOffset != br.tailBytes) {
            throw new BrotliRuntimeException("Unused bytes after end");
        }
    }

    static void fillBitWindow(BitReader br) {
        if (br.bitOffset >= 32) {
            int[] iArr = br.intBuffer;
            br.intOffset = br.intOffset + 1;
            br.accumulator = (iArr[r3] << 32) | (br.accumulator >>> 32);
            br.bitOffset -= 32;
        }
    }

    static int readBits(BitReader br, int n) {
        fillBitWindow(br);
        int val = ((int) (br.accumulator >>> br.bitOffset)) & ((1 << n) - 1);
        br.bitOffset += n;
        return val;
    }

    static void init(BitReader br, InputStream input) throws IOException {
        if (br.input != null) {
            throw new IllegalStateException("Bit reader already has associated input stream");
        }
        IntReader.init(br.intReader, br.byteBuffer, br.intBuffer);
        br.input = input;
        br.accumulator = 0L;
        br.bitOffset = 64;
        br.intOffset = 1024;
        br.endOfStreamReached = false;
        prepare(br);
    }

    private static void prepare(BitReader br) throws IOException {
        readMoreInput(br);
        checkHealth(br, false);
        fillBitWindow(br);
        fillBitWindow(br);
    }

    static void reload(BitReader br) throws IOException {
        if (br.bitOffset == 64) {
            prepare(br);
        }
    }

    static void close(BitReader br) throws IOException {
        InputStream is = br.input;
        br.input = null;
        if (is != null) {
            is.close();
        }
    }

    static void jumpToByteBoundary(BitReader br) {
        int padding = (64 - br.bitOffset) & 7;
        if (padding != 0) {
            int paddingBits = readBits(br, padding);
            if (paddingBits != 0) {
                throw new BrotliRuntimeException("Corrupted padding bits");
            }
        }
    }

    static int intAvailable(BitReader br) {
        int limit = 1024;
        if (br.endOfStreamReached) {
            limit = (br.tailBytes + 3) >> 2;
        }
        return limit - br.intOffset;
    }

    static void copyBytes(BitReader br, byte[] data, int offset, int length) throws IOException {
        if ((br.bitOffset & 7) != 0) {
            throw new BrotliRuntimeException("Unaligned copyBytes");
        }
        while (br.bitOffset != 64 && length != 0) {
            int i = offset;
            offset++;
            data[i] = (byte) (br.accumulator >>> br.bitOffset);
            br.bitOffset += 8;
            length--;
        }
        if (length == 0) {
            return;
        }
        int copyInts = Math.min(intAvailable(br), length >> 2);
        if (copyInts > 0) {
            int readOffset = br.intOffset << 2;
            System.arraycopy(br.byteBuffer, readOffset, data, offset, copyInts << 2);
            offset += copyInts << 2;
            length -= copyInts << 2;
            br.intOffset += copyInts;
        }
        if (length == 0) {
            return;
        }
        if (intAvailable(br) > 0) {
            fillBitWindow(br);
            while (length != 0) {
                int i2 = offset;
                offset++;
                data[i2] = (byte) (br.accumulator >>> br.bitOffset);
                br.bitOffset += 8;
                length--;
            }
            checkHealth(br, false);
            return;
        }
        while (length > 0) {
            try {
                int len = br.input.read(data, offset, length);
                if (len == -1) {
                    throw new BrotliRuntimeException("Unexpected end of input");
                }
                offset += len;
                length -= len;
            } catch (IOException e) {
                throw new BrotliRuntimeException("Failed to read input", e);
            }
        }
    }
}
