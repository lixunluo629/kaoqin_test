package org.bouncycastle.crypto.digests;

import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hssf.record.DrawingGroupRecord;
import org.apache.poi.hssf.record.DrawingSelectionRecord;
import org.apache.poi.hssf.record.MergeCellsRecord;
import org.apache.poi.hssf.record.UnknownRecord;
import org.aspectj.apache.bcel.Constants;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.util.Arrays;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/digests/WhirlpoolDigest.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/digests/WhirlpoolDigest.class */
public final class WhirlpoolDigest implements ExtendedDigest {
    private static final int BYTE_LENGTH = 64;
    private static final int DIGEST_LENGTH_BYTES = 64;
    private static final int ROUNDS = 10;
    private static final int REDUCTION_POLYNOMIAL = 285;
    private final long[] _rc;
    private static final int BITCOUNT_ARRAY_SIZE = 32;
    private byte[] _buffer;
    private int _bufferPos;
    private short[] _bitCount;
    private long[] _hash;
    private long[] _K;
    private long[] _L;
    private long[] _block;
    private long[] _state;
    private static final int[] SBOX = {24, 35, 198, 232, 135, 184, 1, 79, 54, 166, Constants.GETSTATIC_QUICK, EscherProperties.GEOTEXT__STRETCHTOFITSHAPE, 121, 111, 145, 82, 96, 188, 155, 142, 163, 12, 123, 53, 29, 224, 215, 194, 46, 75, 254, 87, 21, 119, 55, MergeCellsRecord.sid, 159, 240, 74, 218, 88, 201, 41, 10, 177, 160, 107, 133, 189, 93, 16, EscherProperties.GEOTEXT__TIGHTORTRACK, 203, 62, 5, 103, Constants.PUTFIELD_QUICK_W, 39, 65, 139, 167, 125, 149, Constants.INVOKESUPER_QUICK, 251, 238, 124, 102, 221, 23, 71, 158, 202, 45, 191, 7, 173, 90, 131, 51, 99, 2, 170, 113, 200, 25, 73, Constants.INVOKESTATIC_QUICK, EscherProperties.GEOTEXT__ROTATECHARACTERS, 227, 91, 136, 154, 38, 50, 176, UnknownRecord.BITMAP_00E9, 15, 213, 128, 190, 205, 52, 72, 255, 122, 144, 95, 32, 104, 26, 174, 180, 84, 147, 34, 100, EscherProperties.GEOTEXT__HASTEXTEFFECT, 115, 18, 64, 8, 195, 236, Constants.INVOKEVIRTUALOBJECT_QUICK, 161, 141, 61, 151, 0, 207, 43, 118, 130, Constants.INVOKEVIRTUAL_QUICK, 27, 181, 175, 106, 80, 69, EscherProperties.GEOTEXT__KERNCHARACTERS, 48, UnknownRecord.PHONETICPR_00EF, 63, 85, 162, 234, 101, 186, 47, 192, Constants.ANEWARRAY_QUICK, 28, 253, 77, 146, 117, 6, 138, 178, 230, 14, 31, 98, Constants.GETSTATIC2_QUICK, 168, 150, EscherProperties.GEOTEXT__NOMEASUREALONGPATH, 197, 37, 89, 132, 114, 57, 76, 94, 120, 56, 140, Constants.PUTFIELD2_QUICK, 165, 226, 97, 179, 33, 156, 30, 67, 199, 252, 4, 81, 153, 109, 13, EscherProperties.GEOTEXT__BOLDFONT, Constants.MULTIANEWARRAY_QUICK, 126, 36, 59, 171, 206, 17, 143, 78, 183, DrawingGroupRecord.sid, 60, 129, 148, 247, 185, 19, 44, 211, 231, 110, 196, 3, 86, 68, 127, 169, 42, 187, 193, 83, 220, 11, 157, 108, 49, 116, EscherProperties.GEOTEXT__CHARBOUNDINGBOX, 70, 172, 137, 20, 225, 22, 58, 105, 9, 112, 182, 208, DrawingSelectionRecord.sid, 204, 66, 152, 164, 40, 92, EscherProperties.GEOTEXT__STRETCHCHARHEIGHT, 134};
    private static final long[] C0 = new long[256];
    private static final long[] C1 = new long[256];
    private static final long[] C2 = new long[256];
    private static final long[] C3 = new long[256];
    private static final long[] C4 = new long[256];
    private static final long[] C5 = new long[256];
    private static final long[] C6 = new long[256];
    private static final long[] C7 = new long[256];
    private static final short[] EIGHT = new short[32];

    public WhirlpoolDigest() {
        this._rc = new long[11];
        this._buffer = new byte[64];
        this._bufferPos = 0;
        this._bitCount = new short[32];
        this._hash = new long[8];
        this._K = new long[8];
        this._L = new long[8];
        this._block = new long[8];
        this._state = new long[8];
        for (int i = 0; i < 256; i++) {
            int i2 = SBOX[i];
            int iMaskWithReductionPolynomial = maskWithReductionPolynomial(i2 << 1);
            int iMaskWithReductionPolynomial2 = maskWithReductionPolynomial(iMaskWithReductionPolynomial << 1);
            int i3 = iMaskWithReductionPolynomial2 ^ i2;
            int iMaskWithReductionPolynomial3 = maskWithReductionPolynomial(iMaskWithReductionPolynomial2 << 1);
            int i4 = iMaskWithReductionPolynomial3 ^ i2;
            C0[i] = packIntoLong(i2, i2, iMaskWithReductionPolynomial2, i2, iMaskWithReductionPolynomial3, i3, iMaskWithReductionPolynomial, i4);
            C1[i] = packIntoLong(i4, i2, i2, iMaskWithReductionPolynomial2, i2, iMaskWithReductionPolynomial3, i3, iMaskWithReductionPolynomial);
            C2[i] = packIntoLong(iMaskWithReductionPolynomial, i4, i2, i2, iMaskWithReductionPolynomial2, i2, iMaskWithReductionPolynomial3, i3);
            C3[i] = packIntoLong(i3, iMaskWithReductionPolynomial, i4, i2, i2, iMaskWithReductionPolynomial2, i2, iMaskWithReductionPolynomial3);
            C4[i] = packIntoLong(iMaskWithReductionPolynomial3, i3, iMaskWithReductionPolynomial, i4, i2, i2, iMaskWithReductionPolynomial2, i2);
            C5[i] = packIntoLong(i2, iMaskWithReductionPolynomial3, i3, iMaskWithReductionPolynomial, i4, i2, i2, iMaskWithReductionPolynomial2);
            C6[i] = packIntoLong(iMaskWithReductionPolynomial2, i2, iMaskWithReductionPolynomial3, i3, iMaskWithReductionPolynomial, i4, i2, i2);
            C7[i] = packIntoLong(i2, iMaskWithReductionPolynomial2, i2, iMaskWithReductionPolynomial3, i3, iMaskWithReductionPolynomial, i4, i2);
        }
        this._rc[0] = 0;
        for (int i5 = 1; i5 <= 10; i5++) {
            int i6 = 8 * (i5 - 1);
            this._rc[i5] = (((((((C0[i6] & (-72057594037927936L)) ^ (C1[i6 + 1] & 71776119061217280L)) ^ (C2[i6 + 2] & 280375465082880L)) ^ (C3[i6 + 3] & 1095216660480L)) ^ (C4[i6 + 4] & 4278190080L)) ^ (C5[i6 + 5] & 16711680)) ^ (C6[i6 + 6] & 65280)) ^ (C7[i6 + 7] & 255);
        }
    }

    private long packIntoLong(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        return (((((((i << 56) ^ (i2 << 48)) ^ (i3 << 40)) ^ (i4 << 32)) ^ (i5 << 24)) ^ (i6 << 16)) ^ (i7 << 8)) ^ i8;
    }

    private int maskWithReductionPolynomial(int i) {
        int i2 = i;
        if (i2 >= 256) {
            i2 ^= 285;
        }
        return i2;
    }

    public WhirlpoolDigest(WhirlpoolDigest whirlpoolDigest) {
        this._rc = new long[11];
        this._buffer = new byte[64];
        this._bufferPos = 0;
        this._bitCount = new short[32];
        this._hash = new long[8];
        this._K = new long[8];
        this._L = new long[8];
        this._block = new long[8];
        this._state = new long[8];
        System.arraycopy(whirlpoolDigest._rc, 0, this._rc, 0, this._rc.length);
        System.arraycopy(whirlpoolDigest._buffer, 0, this._buffer, 0, this._buffer.length);
        this._bufferPos = whirlpoolDigest._bufferPos;
        System.arraycopy(whirlpoolDigest._bitCount, 0, this._bitCount, 0, this._bitCount.length);
        System.arraycopy(whirlpoolDigest._hash, 0, this._hash, 0, this._hash.length);
        System.arraycopy(whirlpoolDigest._K, 0, this._K, 0, this._K.length);
        System.arraycopy(whirlpoolDigest._L, 0, this._L, 0, this._L.length);
        System.arraycopy(whirlpoolDigest._block, 0, this._block, 0, this._block.length);
        System.arraycopy(whirlpoolDigest._state, 0, this._state, 0, this._state.length);
    }

    @Override // org.bouncycastle.crypto.Digest
    public String getAlgorithmName() {
        return "Whirlpool";
    }

    @Override // org.bouncycastle.crypto.Digest
    public int getDigestSize() {
        return 64;
    }

    @Override // org.bouncycastle.crypto.Digest
    public int doFinal(byte[] bArr, int i) {
        finish();
        for (int i2 = 0; i2 < 8; i2++) {
            convertLongToByteArray(this._hash[i2], bArr, i + (i2 * 8));
        }
        reset();
        return getDigestSize();
    }

    @Override // org.bouncycastle.crypto.Digest
    public void reset() {
        this._bufferPos = 0;
        Arrays.fill(this._bitCount, (short) 0);
        Arrays.fill(this._buffer, (byte) 0);
        Arrays.fill(this._hash, 0L);
        Arrays.fill(this._K, 0L);
        Arrays.fill(this._L, 0L);
        Arrays.fill(this._block, 0L);
        Arrays.fill(this._state, 0L);
    }

    private void processFilledBuffer(byte[] bArr, int i) {
        for (int i2 = 0; i2 < this._state.length; i2++) {
            this._block[i2] = bytesToLongFromBuffer(this._buffer, i2 * 8);
        }
        processBlock();
        this._bufferPos = 0;
        Arrays.fill(this._buffer, (byte) 0);
    }

    private long bytesToLongFromBuffer(byte[] bArr, int i) {
        return ((bArr[i + 0] & 255) << 56) | ((bArr[i + 1] & 255) << 48) | ((bArr[i + 2] & 255) << 40) | ((bArr[i + 3] & 255) << 32) | ((bArr[i + 4] & 255) << 24) | ((bArr[i + 5] & 255) << 16) | ((bArr[i + 6] & 255) << 8) | (bArr[i + 7] & 255);
    }

    private void convertLongToByteArray(long j, byte[] bArr, int i) {
        for (int i2 = 0; i2 < 8; i2++) {
            bArr[i + i2] = (byte) ((j >> (56 - (i2 * 8))) & 255);
        }
    }

    protected void processBlock() {
        for (int i = 0; i < 8; i++) {
            long j = this._block[i];
            long j2 = this._hash[i];
            this._K[i] = j2;
            this._state[i] = j ^ j2;
        }
        for (int i2 = 1; i2 <= 10; i2++) {
            for (int i3 = 0; i3 < 8; i3++) {
                this._L[i3] = 0;
                long[] jArr = this._L;
                int i4 = i3;
                jArr[i4] = jArr[i4] ^ C0[((int) (this._K[(i3 - 0) & 7] >>> 56)) & 255];
                long[] jArr2 = this._L;
                int i5 = i3;
                jArr2[i5] = jArr2[i5] ^ C1[((int) (this._K[(i3 - 1) & 7] >>> 48)) & 255];
                long[] jArr3 = this._L;
                int i6 = i3;
                jArr3[i6] = jArr3[i6] ^ C2[((int) (this._K[(i3 - 2) & 7] >>> 40)) & 255];
                long[] jArr4 = this._L;
                int i7 = i3;
                jArr4[i7] = jArr4[i7] ^ C3[((int) (this._K[(i3 - 3) & 7] >>> 32)) & 255];
                long[] jArr5 = this._L;
                int i8 = i3;
                jArr5[i8] = jArr5[i8] ^ C4[((int) (this._K[(i3 - 4) & 7] >>> 24)) & 255];
                long[] jArr6 = this._L;
                int i9 = i3;
                jArr6[i9] = jArr6[i9] ^ C5[((int) (this._K[(i3 - 5) & 7] >>> 16)) & 255];
                long[] jArr7 = this._L;
                int i10 = i3;
                jArr7[i10] = jArr7[i10] ^ C6[((int) (this._K[(i3 - 6) & 7] >>> 8)) & 255];
                long[] jArr8 = this._L;
                int i11 = i3;
                jArr8[i11] = jArr8[i11] ^ C7[((int) this._K[(i3 - 7) & 7]) & 255];
            }
            System.arraycopy(this._L, 0, this._K, 0, this._K.length);
            long[] jArr9 = this._K;
            jArr9[0] = jArr9[0] ^ this._rc[i2];
            for (int i12 = 0; i12 < 8; i12++) {
                this._L[i12] = this._K[i12];
                long[] jArr10 = this._L;
                int i13 = i12;
                jArr10[i13] = jArr10[i13] ^ C0[((int) (this._state[(i12 - 0) & 7] >>> 56)) & 255];
                long[] jArr11 = this._L;
                int i14 = i12;
                jArr11[i14] = jArr11[i14] ^ C1[((int) (this._state[(i12 - 1) & 7] >>> 48)) & 255];
                long[] jArr12 = this._L;
                int i15 = i12;
                jArr12[i15] = jArr12[i15] ^ C2[((int) (this._state[(i12 - 2) & 7] >>> 40)) & 255];
                long[] jArr13 = this._L;
                int i16 = i12;
                jArr13[i16] = jArr13[i16] ^ C3[((int) (this._state[(i12 - 3) & 7] >>> 32)) & 255];
                long[] jArr14 = this._L;
                int i17 = i12;
                jArr14[i17] = jArr14[i17] ^ C4[((int) (this._state[(i12 - 4) & 7] >>> 24)) & 255];
                long[] jArr15 = this._L;
                int i18 = i12;
                jArr15[i18] = jArr15[i18] ^ C5[((int) (this._state[(i12 - 5) & 7] >>> 16)) & 255];
                long[] jArr16 = this._L;
                int i19 = i12;
                jArr16[i19] = jArr16[i19] ^ C6[((int) (this._state[(i12 - 6) & 7] >>> 8)) & 255];
                long[] jArr17 = this._L;
                int i20 = i12;
                jArr17[i20] = jArr17[i20] ^ C7[((int) this._state[(i12 - 7) & 7]) & 255];
            }
            System.arraycopy(this._L, 0, this._state, 0, this._state.length);
        }
        for (int i21 = 0; i21 < 8; i21++) {
            long[] jArr18 = this._hash;
            int i22 = i21;
            jArr18[i22] = jArr18[i22] ^ (this._state[i21] ^ this._block[i21]);
        }
    }

    @Override // org.bouncycastle.crypto.Digest
    public void update(byte b) {
        this._buffer[this._bufferPos] = b;
        this._bufferPos++;
        if (this._bufferPos == this._buffer.length) {
            processFilledBuffer(this._buffer, 0);
        }
        increment();
    }

    private void increment() {
        int i = 0;
        for (int length = this._bitCount.length - 1; length >= 0; length--) {
            int i2 = (this._bitCount[length] & 255) + EIGHT[length] + i;
            i = i2 >>> 8;
            this._bitCount[length] = (short) (i2 & 255);
        }
    }

    @Override // org.bouncycastle.crypto.Digest
    public void update(byte[] bArr, int i, int i2) {
        while (i2 > 0) {
            update(bArr[i]);
            i++;
            i2--;
        }
    }

    private void finish() {
        byte[] bArrCopyBitLength = copyBitLength();
        byte[] bArr = this._buffer;
        int i = this._bufferPos;
        this._bufferPos = i + 1;
        bArr[i] = (byte) (bArr[i] | 128);
        if (this._bufferPos == this._buffer.length) {
            processFilledBuffer(this._buffer, 0);
        }
        if (this._bufferPos > 32) {
            while (this._bufferPos != 0) {
                update((byte) 0);
            }
        }
        while (this._bufferPos <= 32) {
            update((byte) 0);
        }
        System.arraycopy(bArrCopyBitLength, 0, this._buffer, 32, bArrCopyBitLength.length);
        processFilledBuffer(this._buffer, 0);
    }

    private byte[] copyBitLength() {
        byte[] bArr = new byte[32];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (this._bitCount[i] & 255);
        }
        return bArr;
    }

    @Override // org.bouncycastle.crypto.ExtendedDigest
    public int getByteLength() {
        return 64;
    }

    static {
        EIGHT[31] = 8;
    }
}
