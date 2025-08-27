package org.bouncycastle.math.ec.rfc8032;

import java.security.SecureRandom;
import org.apache.poi.ddf.EscherProperties;
import org.bouncycastle.crypto.Xof;
import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.math.ec.rfc7748.X448;
import org.bouncycastle.math.ec.rfc7748.X448Field;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/rfc8032/Ed448.class */
public abstract class Ed448 {
    private static final long M26L = 67108863;
    private static final long M28L = 268435455;
    private static final long M32L = 4294967295L;
    private static final int POINT_BYTES = 57;
    private static final int SCALAR_INTS = 14;
    private static final int SCALAR_BYTES = 57;
    public static final int PREHASH_SIZE = 64;
    public static final int PUBLIC_KEY_SIZE = 57;
    public static final int SECRET_KEY_SIZE = 57;
    public static final int SIGNATURE_SIZE = 114;
    private static final int L_0 = 78101261;
    private static final int L_1 = 141809365;
    private static final int L_2 = 175155932;
    private static final int L_3 = 64542499;
    private static final int L_4 = 158326419;
    private static final int L_5 = 191173276;
    private static final int L_6 = 104575268;
    private static final int L_7 = 137584065;
    private static final int L4_0 = 43969588;
    private static final int L4_1 = 30366549;
    private static final int L4_2 = 163752818;
    private static final int L4_3 = 258169998;
    private static final int L4_4 = 96434764;
    private static final int L4_5 = 227822194;
    private static final int L4_6 = 149865618;
    private static final int L4_7 = 550336261;
    private static final int C_d = -39081;
    private static final int WNAF_WIDTH_BASE = 7;
    private static final int PRECOMP_BLOCKS = 5;
    private static final int PRECOMP_TEETH = 5;
    private static final int PRECOMP_SPACING = 18;
    private static final int PRECOMP_POINTS = 16;
    private static final int PRECOMP_MASK = 15;
    private static final byte[] DOM4_PREFIX = Strings.toByteArray("SigEd448");
    private static final int[] P = {-1, -1, -1, -1, -1, -1, -1, -2, -1, -1, -1, -1, -1, -1};
    private static final int[] L = {-1420278541, 595116690, -1916432555, 560775794, -1361693040, -1001465015, 2093622249, -1, -1, -1, -1, -1, -1, 1073741823};
    private static final int[] B_x = {118276190, 40534716, 9670182, 135141552, 85017403, 259173222, 68333082, 171784774, 174973732, 15824510, 73756743, 57518561, 94773951, 248652241, 107736333, 82941708};
    private static final int[] B_y = {36764180, 8885695, 130592152, 20104429, 163904957, 30304195, 121295871, 5901357, 125344798, 171541512, 175338348, 209069246, 3626697, 38307682, 24032956, 110359655};
    private static final Object precompLock = new Object();
    private static PointExt[] precompBaseTable = null;
    private static int[] precompBase = null;

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/rfc8032/Ed448$Algorithm.class */
    public static final class Algorithm {
        public static final int Ed448 = 0;
        public static final int Ed448ph = 1;
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/rfc8032/Ed448$PointExt.class */
    private static class PointExt {
        int[] x;
        int[] y;
        int[] z;

        private PointExt() {
            this.x = X448Field.create();
            this.y = X448Field.create();
            this.z = X448Field.create();
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/rfc8032/Ed448$PointPrecomp.class */
    private static class PointPrecomp {
        int[] x;
        int[] y;

        private PointPrecomp() {
            this.x = X448Field.create();
            this.y = X448Field.create();
        }
    }

    private static byte[] calculateS(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        int[] iArr = new int[28];
        decodeScalar(bArr, 0, iArr);
        int[] iArr2 = new int[14];
        decodeScalar(bArr2, 0, iArr2);
        int[] iArr3 = new int[14];
        decodeScalar(bArr3, 0, iArr3);
        Nat.mulAddTo(14, iArr2, iArr3, iArr);
        byte[] bArr4 = new byte[114];
        for (int i = 0; i < iArr.length; i++) {
            encode32(iArr[i], bArr4, i * 4);
        }
        return reduceScalar(bArr4);
    }

    private static boolean checkContextVar(byte[] bArr) {
        return bArr != null && bArr.length < 256;
    }

    private static int checkPoint(int[] iArr, int[] iArr2) {
        int[] iArrCreate = X448Field.create();
        int[] iArrCreate2 = X448Field.create();
        int[] iArrCreate3 = X448Field.create();
        X448Field.sqr(iArr, iArrCreate2);
        X448Field.sqr(iArr2, iArrCreate3);
        X448Field.mul(iArrCreate2, iArrCreate3, iArrCreate);
        X448Field.add(iArrCreate2, iArrCreate3, iArrCreate2);
        X448Field.mul(iArrCreate, 39081, iArrCreate);
        X448Field.subOne(iArrCreate);
        X448Field.add(iArrCreate, iArrCreate2, iArrCreate);
        X448Field.normalize(iArrCreate);
        return X448Field.isZero(iArrCreate);
    }

    private static int checkPoint(int[] iArr, int[] iArr2, int[] iArr3) {
        int[] iArrCreate = X448Field.create();
        int[] iArrCreate2 = X448Field.create();
        int[] iArrCreate3 = X448Field.create();
        int[] iArrCreate4 = X448Field.create();
        X448Field.sqr(iArr, iArrCreate2);
        X448Field.sqr(iArr2, iArrCreate3);
        X448Field.sqr(iArr3, iArrCreate4);
        X448Field.mul(iArrCreate2, iArrCreate3, iArrCreate);
        X448Field.add(iArrCreate2, iArrCreate3, iArrCreate2);
        X448Field.mul(iArrCreate2, iArrCreate4, iArrCreate2);
        X448Field.sqr(iArrCreate4, iArrCreate4);
        X448Field.mul(iArrCreate, 39081, iArrCreate);
        X448Field.sub(iArrCreate, iArrCreate4, iArrCreate);
        X448Field.add(iArrCreate, iArrCreate2, iArrCreate);
        X448Field.normalize(iArrCreate);
        return X448Field.isZero(iArrCreate);
    }

    private static boolean checkPointVar(byte[] bArr) {
        if ((bArr[56] & Byte.MAX_VALUE) != 0) {
            return false;
        }
        int[] iArr = new int[14];
        decode32(bArr, 0, iArr, 0, 14);
        return !Nat.gte(14, iArr, P);
    }

    private static boolean checkScalarVar(byte[] bArr) {
        if (bArr[56] != 0) {
            return false;
        }
        int[] iArr = new int[14];
        decodeScalar(bArr, 0, iArr);
        return !Nat.gte(14, iArr, L);
    }

    public static Xof createPrehash() {
        return createXof();
    }

    private static Xof createXof() {
        return new SHAKEDigest(256);
    }

    private static int decode16(byte[] bArr, int i) {
        return (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8);
    }

    private static int decode24(byte[] bArr, int i) {
        int i2 = bArr[i] & 255;
        int i3 = i + 1;
        return i2 | ((bArr[i3] & 255) << 8) | ((bArr[i3 + 1] & 255) << 16);
    }

    private static int decode32(byte[] bArr, int i) {
        int i2 = bArr[i] & 255;
        int i3 = i + 1;
        int i4 = i2 | ((bArr[i3] & 255) << 8);
        int i5 = i3 + 1;
        return i4 | ((bArr[i5] & 255) << 16) | (bArr[i5 + 1] << 24);
    }

    private static void decode32(byte[] bArr, int i, int[] iArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            iArr[i2 + i4] = decode32(bArr, i + (i4 * 4));
        }
    }

    private static boolean decodePointVar(byte[] bArr, int i, boolean z, PointExt pointExt) {
        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, i, i + 57);
        if (!checkPointVar(bArrCopyOfRange)) {
            return false;
        }
        int i2 = (bArrCopyOfRange[56] & 128) >>> 7;
        bArrCopyOfRange[56] = (byte) (bArrCopyOfRange[56] & Byte.MAX_VALUE);
        X448Field.decode(bArrCopyOfRange, 0, pointExt.y);
        int[] iArrCreate = X448Field.create();
        int[] iArrCreate2 = X448Field.create();
        X448Field.sqr(pointExt.y, iArrCreate);
        X448Field.mul(iArrCreate, 39081, iArrCreate2);
        X448Field.negate(iArrCreate, iArrCreate);
        X448Field.addOne(iArrCreate);
        X448Field.addOne(iArrCreate2);
        if (!X448Field.sqrtRatioVar(iArrCreate, iArrCreate2, pointExt.x)) {
            return false;
        }
        X448Field.normalize(pointExt.x);
        if (i2 == 1 && X448Field.isZeroVar(pointExt.x)) {
            return false;
        }
        if (z ^ (i2 != (pointExt.x[0] & 1))) {
            X448Field.negate(pointExt.x, pointExt.x);
        }
        pointExtendXY(pointExt);
        return true;
    }

    private static void decodeScalar(byte[] bArr, int i, int[] iArr) {
        decode32(bArr, i, iArr, 0, 14);
    }

    private static void dom4(Xof xof, byte b, byte[] bArr) {
        xof.update(DOM4_PREFIX, 0, DOM4_PREFIX.length);
        xof.update(b);
        xof.update((byte) bArr.length);
        xof.update(bArr, 0, bArr.length);
    }

    private static void encode24(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) i;
        int i3 = i2 + 1;
        bArr[i3] = (byte) (i >>> 8);
        bArr[i3 + 1] = (byte) (i >>> 16);
    }

    private static void encode32(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) i;
        int i3 = i2 + 1;
        bArr[i3] = (byte) (i >>> 8);
        int i4 = i3 + 1;
        bArr[i4] = (byte) (i >>> 16);
        bArr[i4 + 1] = (byte) (i >>> 24);
    }

    private static void encode56(long j, byte[] bArr, int i) {
        encode32((int) j, bArr, i);
        encode24((int) (j >>> 32), bArr, i + 4);
    }

    private static int encodePoint(PointExt pointExt, byte[] bArr, int i) {
        int[] iArrCreate = X448Field.create();
        int[] iArrCreate2 = X448Field.create();
        X448Field.inv(pointExt.z, iArrCreate2);
        X448Field.mul(pointExt.x, iArrCreate2, iArrCreate);
        X448Field.mul(pointExt.y, iArrCreate2, iArrCreate2);
        X448Field.normalize(iArrCreate);
        X448Field.normalize(iArrCreate2);
        int iCheckPoint = checkPoint(iArrCreate, iArrCreate2);
        X448Field.encode(iArrCreate2, bArr, i);
        bArr[(i + 57) - 1] = (byte) ((iArrCreate[0] & 1) << 7);
        return iCheckPoint;
    }

    public static void generatePrivateKey(SecureRandom secureRandom, byte[] bArr) {
        secureRandom.nextBytes(bArr);
    }

    public static void generatePublicKey(byte[] bArr, int i, byte[] bArr2, int i2) {
        Xof xofCreateXof = createXof();
        byte[] bArr3 = new byte[114];
        xofCreateXof.update(bArr, i, 57);
        xofCreateXof.doFinal(bArr3, 0, bArr3.length);
        byte[] bArr4 = new byte[57];
        pruneScalar(bArr3, 0, bArr4);
        scalarMultBaseEncoded(bArr4, bArr2, i2);
    }

    private static int getWindow4(int[] iArr, int i) {
        return (iArr[i >>> 3] >>> ((i & 7) << 2)) & 15;
    }

    private static byte[] getWNAF(int[] iArr, int i) {
        int[] iArr2 = new int[28];
        int length = iArr2.length;
        int i2 = 0;
        int i3 = 14;
        while (true) {
            i3--;
            if (i3 < 0) {
                break;
            }
            int i4 = iArr[i3];
            int i5 = length - 1;
            iArr2[i5] = (i4 >>> 16) | (i2 << 16);
            length = i5 - 1;
            i2 = i4;
            iArr2[length] = i4;
        }
        byte[] bArr = new byte[EscherProperties.FILL__NOFILLHITTEST];
        int i6 = 1 << i;
        int i7 = i6 - 1;
        int i8 = i6 >>> 1;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        while (i11 < iArr2.length) {
            int i12 = iArr2[i11];
            while (i9 < 16) {
                int i13 = i12 >>> i9;
                if ((i13 & 1) == i10) {
                    i9++;
                } else {
                    int i14 = (i13 & i7) + i10;
                    int i15 = i14 & i8;
                    i10 = i15 >>> (i - 1);
                    bArr[(i11 << 4) + i9] = (byte) (i14 - (i15 << 1));
                    i9 += i;
                }
            }
            i11++;
            i9 -= 16;
        }
        return bArr;
    }

    private static void implSign(Xof xof, byte[] bArr, byte[] bArr2, byte[] bArr3, int i, byte[] bArr4, byte b, byte[] bArr5, int i2, int i3, byte[] bArr6, int i4) {
        dom4(xof, b, bArr4);
        xof.update(bArr, 57, 57);
        xof.update(bArr5, i2, i3);
        xof.doFinal(bArr, 0, bArr.length);
        byte[] bArrReduceScalar = reduceScalar(bArr);
        byte[] bArr7 = new byte[57];
        scalarMultBaseEncoded(bArrReduceScalar, bArr7, 0);
        dom4(xof, b, bArr4);
        xof.update(bArr7, 0, 57);
        xof.update(bArr3, i, 57);
        xof.update(bArr5, i2, i3);
        xof.doFinal(bArr, 0, bArr.length);
        byte[] bArrCalculateS = calculateS(bArrReduceScalar, reduceScalar(bArr), bArr2);
        System.arraycopy(bArr7, 0, bArr6, i4, 57);
        System.arraycopy(bArrCalculateS, 0, bArr6, i4 + 57, 57);
    }

    private static void implSign(byte[] bArr, int i, byte[] bArr2, byte b, byte[] bArr3, int i2, int i3, byte[] bArr4, int i4) {
        if (!checkContextVar(bArr2)) {
            throw new IllegalArgumentException("ctx");
        }
        Xof xofCreateXof = createXof();
        byte[] bArr5 = new byte[114];
        xofCreateXof.update(bArr, i, 57);
        xofCreateXof.doFinal(bArr5, 0, bArr5.length);
        byte[] bArr6 = new byte[57];
        pruneScalar(bArr5, 0, bArr6);
        byte[] bArr7 = new byte[57];
        scalarMultBaseEncoded(bArr6, bArr7, 0);
        implSign(xofCreateXof, bArr5, bArr6, bArr7, 0, bArr2, b, bArr3, i2, i3, bArr4, i4);
    }

    private static void implSign(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, byte b, byte[] bArr4, int i3, int i4, byte[] bArr5, int i5) {
        if (!checkContextVar(bArr3)) {
            throw new IllegalArgumentException("ctx");
        }
        Xof xofCreateXof = createXof();
        byte[] bArr6 = new byte[114];
        xofCreateXof.update(bArr, i, 57);
        xofCreateXof.doFinal(bArr6, 0, bArr6.length);
        byte[] bArr7 = new byte[57];
        pruneScalar(bArr6, 0, bArr7);
        implSign(xofCreateXof, bArr6, bArr7, bArr2, i2, bArr3, b, bArr4, i3, i4, bArr5, i5);
    }

    private static boolean implVerify(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, byte b, byte[] bArr4, int i3, int i4) {
        if (!checkContextVar(bArr3)) {
            throw new IllegalArgumentException("ctx");
        }
        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, i, i + 57);
        byte[] bArrCopyOfRange2 = Arrays.copyOfRange(bArr, i + 57, i + 114);
        if (!checkPointVar(bArrCopyOfRange) || !checkScalarVar(bArrCopyOfRange2)) {
            return false;
        }
        PointExt pointExt = new PointExt();
        if (!decodePointVar(bArr2, i2, true, pointExt)) {
            return false;
        }
        Xof xofCreateXof = createXof();
        byte[] bArr5 = new byte[114];
        dom4(xofCreateXof, b, bArr3);
        xofCreateXof.update(bArrCopyOfRange, 0, 57);
        xofCreateXof.update(bArr2, i2, 57);
        xofCreateXof.update(bArr4, i3, i4);
        xofCreateXof.doFinal(bArr5, 0, bArr5.length);
        byte[] bArrReduceScalar = reduceScalar(bArr5);
        int[] iArr = new int[14];
        decodeScalar(bArrCopyOfRange2, 0, iArr);
        int[] iArr2 = new int[14];
        decodeScalar(bArrReduceScalar, 0, iArr2);
        PointExt pointExt2 = new PointExt();
        scalarMultStrausVar(iArr, iArr2, pointExt, pointExt2);
        byte[] bArr6 = new byte[57];
        return 0 != encodePoint(pointExt2, bArr6, 0) && Arrays.areEqual(bArr6, bArrCopyOfRange);
    }

    private static void pointAdd(PointExt pointExt, PointExt pointExt2) {
        int[] iArrCreate = X448Field.create();
        int[] iArrCreate2 = X448Field.create();
        int[] iArrCreate3 = X448Field.create();
        int[] iArrCreate4 = X448Field.create();
        int[] iArrCreate5 = X448Field.create();
        int[] iArrCreate6 = X448Field.create();
        int[] iArrCreate7 = X448Field.create();
        int[] iArrCreate8 = X448Field.create();
        X448Field.mul(pointExt.z, pointExt2.z, iArrCreate);
        X448Field.sqr(iArrCreate, iArrCreate2);
        X448Field.mul(pointExt.x, pointExt2.x, iArrCreate3);
        X448Field.mul(pointExt.y, pointExt2.y, iArrCreate4);
        X448Field.mul(iArrCreate3, iArrCreate4, iArrCreate5);
        X448Field.mul(iArrCreate5, 39081, iArrCreate5);
        X448Field.add(iArrCreate2, iArrCreate5, iArrCreate6);
        X448Field.sub(iArrCreate2, iArrCreate5, iArrCreate7);
        X448Field.add(pointExt.x, pointExt.y, iArrCreate2);
        X448Field.add(pointExt2.x, pointExt2.y, iArrCreate5);
        X448Field.mul(iArrCreate2, iArrCreate5, iArrCreate8);
        X448Field.add(iArrCreate4, iArrCreate3, iArrCreate2);
        X448Field.sub(iArrCreate4, iArrCreate3, iArrCreate5);
        X448Field.carry(iArrCreate2);
        X448Field.sub(iArrCreate8, iArrCreate2, iArrCreate8);
        X448Field.mul(iArrCreate8, iArrCreate, iArrCreate8);
        X448Field.mul(iArrCreate5, iArrCreate, iArrCreate5);
        X448Field.mul(iArrCreate6, iArrCreate8, pointExt2.x);
        X448Field.mul(iArrCreate5, iArrCreate7, pointExt2.y);
        X448Field.mul(iArrCreate6, iArrCreate7, pointExt2.z);
    }

    private static void pointAddVar(boolean z, PointExt pointExt, PointExt pointExt2) {
        int[] iArr;
        int[] iArr2;
        int[] iArr3;
        int[] iArr4;
        int[] iArrCreate = X448Field.create();
        int[] iArrCreate2 = X448Field.create();
        int[] iArrCreate3 = X448Field.create();
        int[] iArrCreate4 = X448Field.create();
        int[] iArrCreate5 = X448Field.create();
        int[] iArrCreate6 = X448Field.create();
        int[] iArrCreate7 = X448Field.create();
        int[] iArrCreate8 = X448Field.create();
        if (z) {
            iArr = iArrCreate5;
            iArr2 = iArrCreate2;
            iArr3 = iArrCreate7;
            iArr4 = iArrCreate6;
            X448Field.sub(pointExt.y, pointExt.x, iArrCreate8);
        } else {
            iArr = iArrCreate2;
            iArr2 = iArrCreate5;
            iArr3 = iArrCreate6;
            iArr4 = iArrCreate7;
            X448Field.add(pointExt.y, pointExt.x, iArrCreate8);
        }
        X448Field.mul(pointExt.z, pointExt2.z, iArrCreate);
        X448Field.sqr(iArrCreate, iArrCreate2);
        X448Field.mul(pointExt.x, pointExt2.x, iArrCreate3);
        X448Field.mul(pointExt.y, pointExt2.y, iArrCreate4);
        X448Field.mul(iArrCreate3, iArrCreate4, iArrCreate5);
        X448Field.mul(iArrCreate5, 39081, iArrCreate5);
        X448Field.add(iArrCreate2, iArrCreate5, iArr3);
        X448Field.sub(iArrCreate2, iArrCreate5, iArr4);
        X448Field.add(pointExt2.x, pointExt2.y, iArrCreate5);
        X448Field.mul(iArrCreate8, iArrCreate5, iArrCreate8);
        X448Field.add(iArrCreate4, iArrCreate3, iArr);
        X448Field.sub(iArrCreate4, iArrCreate3, iArr2);
        X448Field.carry(iArr);
        X448Field.sub(iArrCreate8, iArrCreate2, iArrCreate8);
        X448Field.mul(iArrCreate8, iArrCreate, iArrCreate8);
        X448Field.mul(iArrCreate5, iArrCreate, iArrCreate5);
        X448Field.mul(iArrCreate6, iArrCreate8, pointExt2.x);
        X448Field.mul(iArrCreate5, iArrCreate7, pointExt2.y);
        X448Field.mul(iArrCreate6, iArrCreate7, pointExt2.z);
    }

    private static void pointAddPrecomp(PointPrecomp pointPrecomp, PointExt pointExt) {
        int[] iArrCreate = X448Field.create();
        int[] iArrCreate2 = X448Field.create();
        int[] iArrCreate3 = X448Field.create();
        int[] iArrCreate4 = X448Field.create();
        int[] iArrCreate5 = X448Field.create();
        int[] iArrCreate6 = X448Field.create();
        int[] iArrCreate7 = X448Field.create();
        X448Field.sqr(pointExt.z, iArrCreate);
        X448Field.mul(pointPrecomp.x, pointExt.x, iArrCreate2);
        X448Field.mul(pointPrecomp.y, pointExt.y, iArrCreate3);
        X448Field.mul(iArrCreate2, iArrCreate3, iArrCreate4);
        X448Field.mul(iArrCreate4, 39081, iArrCreate4);
        X448Field.add(iArrCreate, iArrCreate4, iArrCreate5);
        X448Field.sub(iArrCreate, iArrCreate4, iArrCreate6);
        X448Field.add(pointPrecomp.x, pointPrecomp.y, iArrCreate);
        X448Field.add(pointExt.x, pointExt.y, iArrCreate4);
        X448Field.mul(iArrCreate, iArrCreate4, iArrCreate7);
        X448Field.add(iArrCreate3, iArrCreate2, iArrCreate);
        X448Field.sub(iArrCreate3, iArrCreate2, iArrCreate4);
        X448Field.carry(iArrCreate);
        X448Field.sub(iArrCreate7, iArrCreate, iArrCreate7);
        X448Field.mul(iArrCreate7, pointExt.z, iArrCreate7);
        X448Field.mul(iArrCreate4, pointExt.z, iArrCreate4);
        X448Field.mul(iArrCreate5, iArrCreate7, pointExt.x);
        X448Field.mul(iArrCreate4, iArrCreate6, pointExt.y);
        X448Field.mul(iArrCreate5, iArrCreate6, pointExt.z);
    }

    private static PointExt pointCopy(PointExt pointExt) {
        PointExt pointExt2 = new PointExt();
        pointCopy(pointExt, pointExt2);
        return pointExt2;
    }

    private static void pointCopy(PointExt pointExt, PointExt pointExt2) {
        X448Field.copy(pointExt.x, 0, pointExt2.x, 0);
        X448Field.copy(pointExt.y, 0, pointExt2.y, 0);
        X448Field.copy(pointExt.z, 0, pointExt2.z, 0);
    }

    private static void pointDouble(PointExt pointExt) {
        int[] iArrCreate = X448Field.create();
        int[] iArrCreate2 = X448Field.create();
        int[] iArrCreate3 = X448Field.create();
        int[] iArrCreate4 = X448Field.create();
        int[] iArrCreate5 = X448Field.create();
        int[] iArrCreate6 = X448Field.create();
        X448Field.add(pointExt.x, pointExt.y, iArrCreate);
        X448Field.sqr(iArrCreate, iArrCreate);
        X448Field.sqr(pointExt.x, iArrCreate2);
        X448Field.sqr(pointExt.y, iArrCreate3);
        X448Field.add(iArrCreate2, iArrCreate3, iArrCreate4);
        X448Field.carry(iArrCreate4);
        X448Field.sqr(pointExt.z, iArrCreate5);
        X448Field.add(iArrCreate5, iArrCreate5, iArrCreate5);
        X448Field.carry(iArrCreate5);
        X448Field.sub(iArrCreate4, iArrCreate5, iArrCreate6);
        X448Field.sub(iArrCreate, iArrCreate4, iArrCreate);
        X448Field.sub(iArrCreate2, iArrCreate3, iArrCreate2);
        X448Field.mul(iArrCreate, iArrCreate6, pointExt.x);
        X448Field.mul(iArrCreate4, iArrCreate2, pointExt.y);
        X448Field.mul(iArrCreate4, iArrCreate6, pointExt.z);
    }

    private static void pointExtendXY(PointExt pointExt) {
        X448Field.one(pointExt.z);
    }

    private static void pointLookup(int i, int i2, PointPrecomp pointPrecomp) {
        int i3 = i * 16 * 2 * 16;
        for (int i4 = 0; i4 < 16; i4++) {
            int i5 = ((i4 ^ i2) - 1) >> 31;
            X448Field.cmov(i5, precompBase, i3, pointPrecomp.x, 0);
            int i6 = i3 + 16;
            X448Field.cmov(i5, precompBase, i6, pointPrecomp.y, 0);
            i3 = i6 + 16;
        }
    }

    private static void pointLookup(int[] iArr, int i, int[] iArr2, PointExt pointExt) {
        int window4 = getWindow4(iArr, i);
        int i2 = (window4 >>> 3) ^ 1;
        int i3 = (window4 ^ (-i2)) & 7;
        int i4 = 0;
        for (int i5 = 0; i5 < 8; i5++) {
            int i6 = ((i5 ^ i3) - 1) >> 31;
            X448Field.cmov(i6, iArr2, i4, pointExt.x, 0);
            int i7 = i4 + 16;
            X448Field.cmov(i6, iArr2, i7, pointExt.y, 0);
            int i8 = i7 + 16;
            X448Field.cmov(i6, iArr2, i8, pointExt.z, 0);
            i4 = i8 + 16;
        }
        X448Field.cnegate(i2, pointExt.x);
    }

    private static int[] pointPrecomp(PointExt pointExt, int i) {
        PointExt pointExtPointCopy = pointCopy(pointExt);
        PointExt pointExtPointCopy2 = pointCopy(pointExtPointCopy);
        pointDouble(pointExtPointCopy2);
        int[] iArrCreateTable = X448Field.createTable(i * 3);
        int i2 = 0;
        int i3 = 0;
        while (true) {
            X448Field.copy(pointExtPointCopy.x, 0, iArrCreateTable, i2);
            int i4 = i2 + 16;
            X448Field.copy(pointExtPointCopy.y, 0, iArrCreateTable, i4);
            int i5 = i4 + 16;
            X448Field.copy(pointExtPointCopy.z, 0, iArrCreateTable, i5);
            i2 = i5 + 16;
            i3++;
            if (i3 == i) {
                return iArrCreateTable;
            }
            pointAdd(pointExtPointCopy2, pointExtPointCopy);
        }
    }

    private static PointExt[] pointPrecompVar(PointExt pointExt, int i) {
        PointExt pointExtPointCopy = pointCopy(pointExt);
        pointDouble(pointExtPointCopy);
        PointExt[] pointExtArr = new PointExt[i];
        pointExtArr[0] = pointCopy(pointExt);
        for (int i2 = 1; i2 < i; i2++) {
            pointExtArr[i2] = pointCopy(pointExtArr[i2 - 1]);
            pointAddVar(false, pointExtPointCopy, pointExtArr[i2]);
        }
        return pointExtArr;
    }

    private static void pointSetNeutral(PointExt pointExt) {
        X448Field.zero(pointExt.x);
        X448Field.one(pointExt.y);
        X448Field.one(pointExt.z);
    }

    public static void precompute() {
        synchronized (precompLock) {
            if (precompBase != null) {
                return;
            }
            PointExt pointExt = new PointExt();
            X448Field.copy(B_x, 0, pointExt.x, 0);
            X448Field.copy(B_y, 0, pointExt.y, 0);
            pointExtendXY(pointExt);
            precompBaseTable = pointPrecompVar(pointExt, 32);
            precompBase = X448Field.createTable(160);
            int i = 0;
            for (int i2 = 0; i2 < 5; i2++) {
                PointExt[] pointExtArr = new PointExt[5];
                PointExt pointExt2 = new PointExt();
                pointSetNeutral(pointExt2);
                for (int i3 = 0; i3 < 5; i3++) {
                    pointAddVar(true, pointExt, pointExt2);
                    pointDouble(pointExt);
                    pointExtArr[i3] = pointCopy(pointExt);
                    if (i2 + i3 != 8) {
                        for (int i4 = 1; i4 < 18; i4++) {
                            pointDouble(pointExt);
                        }
                    }
                }
                PointExt[] pointExtArr2 = new PointExt[16];
                int i5 = 0 + 1;
                pointExtArr2[0] = pointExt2;
                for (int i6 = 0; i6 < 4; i6++) {
                    int i7 = 1 << i6;
                    int i8 = 0;
                    while (i8 < i7) {
                        pointExtArr2[i5] = pointCopy(pointExtArr2[i5 - i7]);
                        pointAddVar(false, pointExtArr[i6], pointExtArr2[i5]);
                        i8++;
                        i5++;
                    }
                }
                for (int i9 = 0; i9 < 16; i9++) {
                    PointExt pointExt3 = pointExtArr2[i9];
                    X448Field.inv(pointExt3.z, pointExt3.z);
                    X448Field.mul(pointExt3.x, pointExt3.z, pointExt3.x);
                    X448Field.mul(pointExt3.y, pointExt3.z, pointExt3.y);
                    X448Field.copy(pointExt3.x, 0, precompBase, i);
                    int i10 = i + 16;
                    X448Field.copy(pointExt3.y, 0, precompBase, i10);
                    i = i10 + 16;
                }
            }
        }
    }

    private static void pruneScalar(byte[] bArr, int i, byte[] bArr2) {
        System.arraycopy(bArr, i, bArr2, 0, 56);
        bArr2[0] = (byte) (bArr2[0] & 252);
        bArr2[55] = (byte) (bArr2[55] | 128);
        bArr2[56] = 0;
    }

    private static byte[] reduceScalar(byte[] bArr) {
        long jDecode32 = decode32(bArr, 84) & 4294967295L;
        long jDecode322 = decode32(bArr, 91) & 4294967295L;
        long jDecode323 = decode32(bArr, 98) & 4294967295L;
        long jDecode324 = decode32(bArr, 105) & 4294967295L;
        long jDecode16 = decode16(bArr, 112) & 4294967295L;
        long jDecode325 = (decode32(bArr, 56) & 4294967295L) + (jDecode16 * 43969588);
        long jDecode24 = ((decode24(bArr, 60) << 4) & 4294967295L) + (jDecode16 * 30366549);
        long jDecode326 = (decode32(bArr, 63) & 4294967295L) + (jDecode16 * 163752818);
        long jDecode242 = ((decode24(bArr, 67) << 4) & 4294967295L) + (jDecode16 * 258169998);
        long jDecode327 = (decode32(bArr, 70) & 4294967295L) + (jDecode16 * 96434764);
        long jDecode243 = ((decode24(bArr, 74) << 4) & 4294967295L) + (jDecode16 * 227822194);
        long jDecode328 = (decode32(bArr, 77) & 4294967295L) + (jDecode16 * 149865618);
        long jDecode244 = ((decode24(bArr, 81) << 4) & 4294967295L) + (jDecode16 * 550336261);
        long jDecode245 = ((decode24(bArr, 109) << 4) & 4294967295L) + (jDecode324 >>> 28);
        long j = jDecode324 & M28L;
        long jDecode246 = ((decode24(bArr, 53) << 4) & 4294967295L) + (jDecode245 * 43969588);
        long j2 = jDecode325 + (jDecode245 * 30366549);
        long j3 = jDecode24 + (jDecode245 * 163752818);
        long j4 = jDecode326 + (jDecode245 * 258169998);
        long j5 = jDecode242 + (jDecode245 * 96434764);
        long j6 = jDecode327 + (jDecode245 * 227822194);
        long j7 = jDecode243 + (jDecode245 * 149865618);
        long j8 = jDecode328 + (jDecode245 * 550336261);
        long jDecode329 = (decode32(bArr, 49) & 4294967295L) + (j * 43969588);
        long j9 = jDecode246 + (j * 30366549);
        long j10 = j2 + (j * 163752818);
        long j11 = j3 + (j * 258169998);
        long j12 = j4 + (j * 96434764);
        long j13 = j5 + (j * 227822194);
        long j14 = j6 + (j * 149865618);
        long j15 = j7 + (j * 550336261);
        long jDecode247 = ((decode24(bArr, 102) << 4) & 4294967295L) + (jDecode323 >>> 28);
        long j16 = jDecode323 & M28L;
        long jDecode248 = ((decode24(bArr, 46) << 4) & 4294967295L) + (jDecode247 * 43969588);
        long j17 = jDecode329 + (jDecode247 * 30366549);
        long j18 = j9 + (jDecode247 * 163752818);
        long j19 = j10 + (jDecode247 * 258169998);
        long j20 = j11 + (jDecode247 * 96434764);
        long j21 = j12 + (jDecode247 * 227822194);
        long j22 = j13 + (jDecode247 * 149865618);
        long j23 = j14 + (jDecode247 * 550336261);
        long jDecode3210 = (decode32(bArr, 42) & 4294967295L) + (j16 * 43969588);
        long j24 = jDecode248 + (j16 * 30366549);
        long j25 = j17 + (j16 * 163752818);
        long j26 = j18 + (j16 * 258169998);
        long j27 = j19 + (j16 * 96434764);
        long j28 = j20 + (j16 * 227822194);
        long j29 = j21 + (j16 * 149865618);
        long j30 = j22 + (j16 * 550336261);
        long jDecode249 = ((decode24(bArr, 95) << 4) & 4294967295L) + (jDecode322 >>> 28);
        long j31 = jDecode322 & M28L;
        long jDecode2410 = ((decode24(bArr, 39) << 4) & 4294967295L) + (jDecode249 * 43969588);
        long j32 = jDecode3210 + (jDecode249 * 30366549);
        long j33 = j24 + (jDecode249 * 163752818);
        long j34 = j25 + (jDecode249 * 258169998);
        long j35 = j26 + (jDecode249 * 96434764);
        long j36 = j27 + (jDecode249 * 227822194);
        long j37 = j28 + (jDecode249 * 149865618);
        long j38 = j29 + (jDecode249 * 550336261);
        long jDecode3211 = (decode32(bArr, 35) & 4294967295L) + (j31 * 43969588);
        long j39 = jDecode2410 + (j31 * 30366549);
        long j40 = j32 + (j31 * 163752818);
        long j41 = j33 + (j31 * 258169998);
        long j42 = j34 + (j31 * 96434764);
        long j43 = j35 + (j31 * 227822194);
        long j44 = j36 + (j31 * 149865618);
        long j45 = j37 + (j31 * 550336261);
        long jDecode2411 = ((decode24(bArr, 88) << 4) & 4294967295L) + (jDecode32 >>> 28);
        long j46 = jDecode32 & M28L;
        long jDecode2412 = ((decode24(bArr, 32) << 4) & 4294967295L) + (jDecode2411 * 43969588);
        long j47 = jDecode3211 + (jDecode2411 * 30366549);
        long j48 = j39 + (jDecode2411 * 163752818);
        long j49 = j40 + (jDecode2411 * 258169998);
        long j50 = j41 + (jDecode2411 * 96434764);
        long j51 = j42 + (jDecode2411 * 227822194);
        long j52 = j43 + (jDecode2411 * 149865618);
        long j53 = j44 + (jDecode2411 * 550336261);
        long j54 = j15 + (j23 >>> 28);
        long j55 = j23 & M28L;
        long j56 = j8 + (j54 >>> 28);
        long j57 = j54 & M28L;
        long j58 = jDecode244 + (j56 >>> 28);
        long j59 = j56 & M28L;
        long j60 = j46 + (j58 >>> 28);
        long j61 = j58 & M28L;
        long jDecode3212 = (decode32(bArr, 28) & 4294967295L) + (j60 * 43969588);
        long j62 = jDecode2412 + (j60 * 30366549);
        long j63 = j47 + (j60 * 163752818);
        long j64 = j48 + (j60 * 258169998);
        long j65 = j49 + (j60 * 96434764);
        long j66 = j50 + (j60 * 227822194);
        long j67 = j51 + (j60 * 149865618);
        long j68 = j52 + (j60 * 550336261);
        long jDecode2413 = ((decode24(bArr, 25) << 4) & 4294967295L) + (j61 * 43969588);
        long j69 = jDecode3212 + (j61 * 30366549);
        long j70 = j62 + (j61 * 163752818);
        long j71 = j63 + (j61 * 258169998);
        long j72 = j64 + (j61 * 96434764);
        long j73 = j65 + (j61 * 227822194);
        long j74 = j66 + (j61 * 149865618);
        long j75 = j67 + (j61 * 550336261);
        long jDecode3213 = (decode32(bArr, 21) & 4294967295L) + (j59 * 43969588);
        long j76 = jDecode2413 + (j59 * 30366549);
        long j77 = j69 + (j59 * 163752818);
        long j78 = j70 + (j59 * 258169998);
        long j79 = j71 + (j59 * 96434764);
        long j80 = j72 + (j59 * 227822194);
        long j81 = j73 + (j59 * 149865618);
        long j82 = j74 + (j59 * 550336261);
        long j83 = j38 + (j45 >>> 28);
        long j84 = j45 & M28L;
        long j85 = j30 + (j83 >>> 28);
        long j86 = j83 & M28L;
        long j87 = j55 + (j85 >>> 28);
        long j88 = j85 & M28L;
        long j89 = j57 + (j87 >>> 28);
        long j90 = j87 & M28L;
        long jDecode2414 = ((decode24(bArr, 18) << 4) & 4294967295L) + (j89 * 43969588);
        long j91 = jDecode3213 + (j89 * 30366549);
        long j92 = j76 + (j89 * 163752818);
        long j93 = j77 + (j89 * 258169998);
        long j94 = j78 + (j89 * 96434764);
        long j95 = j79 + (j89 * 227822194);
        long j96 = j80 + (j89 * 149865618);
        long j97 = j81 + (j89 * 550336261);
        long jDecode3214 = (decode32(bArr, 14) & 4294967295L) + (j90 * 43969588);
        long j98 = jDecode2414 + (j90 * 30366549);
        long j99 = j91 + (j90 * 163752818);
        long j100 = j92 + (j90 * 258169998);
        long j101 = j93 + (j90 * 96434764);
        long j102 = j94 + (j90 * 227822194);
        long j103 = j95 + (j90 * 149865618);
        long j104 = j96 + (j90 * 550336261);
        long jDecode2415 = ((decode24(bArr, 11) << 4) & 4294967295L) + (j88 * 43969588);
        long j105 = jDecode3214 + (j88 * 30366549);
        long j106 = j98 + (j88 * 163752818);
        long j107 = j99 + (j88 * 258169998);
        long j108 = j100 + (j88 * 96434764);
        long j109 = j101 + (j88 * 227822194);
        long j110 = j102 + (j88 * 149865618);
        long j111 = j103 + (j88 * 550336261);
        long j112 = j68 + (j75 >>> 28);
        long j113 = j75 & M28L;
        long j114 = j53 + (j112 >>> 28);
        long j115 = j112 & M28L;
        long j116 = j84 + (j114 >>> 28);
        long j117 = j114 & M28L;
        long j118 = j86 + (j116 >>> 28);
        long j119 = j116 & M28L;
        long jDecode3215 = (decode32(bArr, 7) & 4294967295L) + (j118 * 43969588);
        long j120 = jDecode2415 + (j118 * 30366549);
        long j121 = j105 + (j118 * 163752818);
        long j122 = j106 + (j118 * 258169998);
        long j123 = j107 + (j118 * 96434764);
        long j124 = j108 + (j118 * 227822194);
        long j125 = j109 + (j118 * 149865618);
        long j126 = j110 + (j118 * 550336261);
        long jDecode2416 = ((decode24(bArr, 4) << 4) & 4294967295L) + (j119 * 43969588);
        long j127 = jDecode3215 + (j119 * 30366549);
        long j128 = j120 + (j119 * 163752818);
        long j129 = j121 + (j119 * 258169998);
        long j130 = j122 + (j119 * 96434764);
        long j131 = j123 + (j119 * 227822194);
        long j132 = j124 + (j119 * 149865618);
        long j133 = j125 + (j119 * 550336261);
        long j134 = (j117 * 4) + (j115 >>> 26);
        long j135 = j115 & M26L;
        long j136 = j134 + 1;
        long jDecode3216 = (decode32(bArr, 0) & 4294967295L) + (j136 * 78101261);
        long j137 = jDecode2416 + (j136 * 141809365);
        long j138 = j127 + (j136 * 175155932);
        long j139 = j128 + (j136 * 64542499);
        long j140 = j129 + (j136 * 158326419);
        long j141 = j130 + (j136 * 191173276);
        long j142 = j131 + (j136 * 104575268);
        long j143 = j132 + (j136 * 137584065);
        long j144 = j137 + (jDecode3216 >>> 28);
        long j145 = jDecode3216 & M28L;
        long j146 = j138 + (j144 >>> 28);
        long j147 = j144 & M28L;
        long j148 = j139 + (j146 >>> 28);
        long j149 = j146 & M28L;
        long j150 = j140 + (j148 >>> 28);
        long j151 = j148 & M28L;
        long j152 = j141 + (j150 >>> 28);
        long j153 = j150 & M28L;
        long j154 = j142 + (j152 >>> 28);
        long j155 = j152 & M28L;
        long j156 = j143 + (j154 >>> 28);
        long j157 = j154 & M28L;
        long j158 = j133 + (j156 >>> 28);
        long j159 = j156 & M28L;
        long j160 = j126 + (j158 >>> 28);
        long j161 = j158 & M28L;
        long j162 = j111 + (j160 >>> 28);
        long j163 = j160 & M28L;
        long j164 = j104 + (j162 >>> 28);
        long j165 = j162 & M28L;
        long j166 = j97 + (j164 >>> 28);
        long j167 = j164 & M28L;
        long j168 = j82 + (j166 >>> 28);
        long j169 = j166 & M28L;
        long j170 = j113 + (j168 >>> 28);
        long j171 = j168 & M28L;
        long j172 = j135 + (j170 >>> 28);
        long j173 = j170 & M28L;
        long j174 = j172 >>> 26;
        long j175 = j172 & M26L;
        long j176 = j174 - 1;
        long j177 = j145 - (j176 & 78101261);
        long j178 = j147 - (j176 & 141809365);
        long j179 = j149 - (j176 & 175155932);
        long j180 = j151 - (j176 & 64542499);
        long j181 = j153 - (j176 & 158326419);
        long j182 = j155 - (j176 & 191173276);
        long j183 = j157 - (j176 & 104575268);
        long j184 = j159 - (j176 & 137584065);
        long j185 = j178 + (j177 >> 28);
        long j186 = j177 & M28L;
        long j187 = j179 + (j185 >> 28);
        long j188 = j185 & M28L;
        long j189 = j180 + (j187 >> 28);
        long j190 = j187 & M28L;
        long j191 = j181 + (j189 >> 28);
        long j192 = j189 & M28L;
        long j193 = j182 + (j191 >> 28);
        long j194 = j191 & M28L;
        long j195 = j183 + (j193 >> 28);
        long j196 = j193 & M28L;
        long j197 = j184 + (j195 >> 28);
        long j198 = j195 & M28L;
        long j199 = j161 + (j197 >> 28);
        long j200 = j197 & M28L;
        long j201 = j163 + (j199 >> 28);
        long j202 = j199 & M28L;
        long j203 = j165 + (j201 >> 28);
        long j204 = j201 & M28L;
        long j205 = j167 + (j203 >> 28);
        long j206 = j203 & M28L;
        long j207 = j169 + (j205 >> 28);
        long j208 = j205 & M28L;
        long j209 = j171 + (j207 >> 28);
        long j210 = j207 & M28L;
        long j211 = j173 + (j209 >> 28);
        long j212 = j209 & M28L;
        long j213 = j175 + (j211 >> 28);
        long j214 = j211 & M28L;
        byte[] bArr2 = new byte[57];
        encode56(j186 | (j188 << 28), bArr2, 0);
        encode56(j190 | (j192 << 28), bArr2, 7);
        encode56(j194 | (j196 << 28), bArr2, 14);
        encode56(j198 | (j200 << 28), bArr2, 21);
        encode56(j202 | (j204 << 28), bArr2, 28);
        encode56(j206 | (j208 << 28), bArr2, 35);
        encode56(j210 | (j212 << 28), bArr2, 42);
        encode56(j214 | (j213 << 28), bArr2, 49);
        return bArr2;
    }

    private static void scalarMult(byte[] bArr, PointExt pointExt, PointExt pointExt2) {
        precompute();
        int[] iArr = new int[14];
        decodeScalar(bArr, 0, iArr);
        Nat.shiftDownBits(14, iArr, 2, 0);
        Nat.cadd(14, (iArr[0] ^ (-1)) & 1, iArr, L, iArr);
        Nat.shiftDownBit(14, iArr, 1);
        int[] iArrPointPrecomp = pointPrecomp(pointExt, 8);
        pointLookup(iArr, 111, iArrPointPrecomp, pointExt2);
        PointExt pointExt3 = new PointExt();
        for (int i = 110; i >= 0; i--) {
            for (int i2 = 0; i2 < 4; i2++) {
                pointDouble(pointExt2);
            }
            pointLookup(iArr, i, iArrPointPrecomp, pointExt3);
            pointAdd(pointExt3, pointExt2);
        }
        for (int i3 = 0; i3 < 2; i3++) {
            pointDouble(pointExt2);
        }
    }

    private static void scalarMultBase(byte[] bArr, PointExt pointExt) {
        precompute();
        pointSetNeutral(pointExt);
        int[] iArr = new int[15];
        decodeScalar(bArr, 0, iArr);
        iArr[14] = 4 + Nat.cadd(14, (iArr[0] ^ (-1)) & 1, iArr, L, iArr);
        Nat.shiftDownBit(iArr.length, iArr, 0);
        PointPrecomp pointPrecomp = new PointPrecomp();
        int i = 17;
        while (true) {
            int i2 = i;
            for (int i3 = 0; i3 < 5; i3++) {
                int i4 = 0;
                for (int i5 = 0; i5 < 5; i5++) {
                    i4 = (i4 & ((1 << i5) ^ (-1))) ^ ((iArr[i2 >>> 5] >>> (i2 & 31)) << i5);
                    i2 += 18;
                }
                int i6 = (i4 >>> 4) & 1;
                pointLookup(i3, (i4 ^ (-i6)) & 15, pointPrecomp);
                X448Field.cnegate(i6, pointPrecomp.x);
                pointAddPrecomp(pointPrecomp, pointExt);
            }
            i--;
            if (i < 0) {
                return;
            } else {
                pointDouble(pointExt);
            }
        }
    }

    private static void scalarMultBaseEncoded(byte[] bArr, byte[] bArr2, int i) {
        PointExt pointExt = new PointExt();
        scalarMultBase(bArr, pointExt);
        if (0 == encodePoint(pointExt, bArr2, i)) {
            throw new IllegalStateException();
        }
    }

    public static void scalarMultBaseXY(X448.Friend friend, byte[] bArr, int i, int[] iArr, int[] iArr2) {
        if (null == friend) {
            throw new NullPointerException("This method is only for use by X448");
        }
        byte[] bArr2 = new byte[57];
        pruneScalar(bArr, i, bArr2);
        PointExt pointExt = new PointExt();
        scalarMultBase(bArr2, pointExt);
        if (0 == checkPoint(pointExt.x, pointExt.y, pointExt.z)) {
            throw new IllegalStateException();
        }
        X448Field.copy(pointExt.x, 0, iArr, 0);
        X448Field.copy(pointExt.y, 0, iArr2, 0);
    }

    private static void scalarMultStrausVar(int[] iArr, int[] iArr2, PointExt pointExt, PointExt pointExt2) {
        precompute();
        byte[] wnaf = getWNAF(iArr, 7);
        byte[] wnaf2 = getWNAF(iArr2, 5);
        PointExt[] pointExtArrPointPrecompVar = pointPrecompVar(pointExt, 8);
        pointSetNeutral(pointExt2);
        int i = 446;
        while (true) {
            byte b = wnaf[i];
            if (b != 0) {
                int i2 = b >> 31;
                pointAddVar(i2 != 0, precompBaseTable[(b ^ i2) >>> 1], pointExt2);
            }
            byte b2 = wnaf2[i];
            if (b2 != 0) {
                int i3 = b2 >> 31;
                pointAddVar(i3 != 0, pointExtArrPointPrecompVar[(b2 ^ i3) >>> 1], pointExt2);
            }
            i--;
            if (i < 0) {
                return;
            } else {
                pointDouble(pointExt2);
            }
        }
    }

    public static void sign(byte[] bArr, int i, byte[] bArr2, byte[] bArr3, int i2, int i3, byte[] bArr4, int i4) {
        implSign(bArr, i, bArr2, (byte) 0, bArr3, i2, i3, bArr4, i4);
    }

    public static void sign(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, byte[] bArr4, int i3, int i4, byte[] bArr5, int i5) {
        implSign(bArr, i, bArr2, i2, bArr3, (byte) 0, bArr4, i3, i4, bArr5, i5);
    }

    public static void signPrehash(byte[] bArr, int i, byte[] bArr2, byte[] bArr3, int i2, byte[] bArr4, int i3) {
        implSign(bArr, i, bArr2, (byte) 1, bArr3, i2, 64, bArr4, i3);
    }

    public static void signPrehash(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, byte[] bArr4, int i3, byte[] bArr5, int i4) {
        implSign(bArr, i, bArr2, i2, bArr3, (byte) 1, bArr4, i3, 64, bArr5, i4);
    }

    public static void signPrehash(byte[] bArr, int i, byte[] bArr2, Xof xof, byte[] bArr3, int i2) {
        byte[] bArr4 = new byte[64];
        if (64 != xof.doFinal(bArr4, 0, 64)) {
            throw new IllegalArgumentException("ph");
        }
        implSign(bArr, i, bArr2, (byte) 1, bArr4, 0, bArr4.length, bArr3, i2);
    }

    public static void signPrehash(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, Xof xof, byte[] bArr4, int i3) {
        byte[] bArr5 = new byte[64];
        if (64 != xof.doFinal(bArr5, 0, 64)) {
            throw new IllegalArgumentException("ph");
        }
        implSign(bArr, i, bArr2, i2, bArr3, (byte) 1, bArr5, 0, bArr5.length, bArr4, i3);
    }

    public static boolean verify(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, byte[] bArr4, int i3, int i4) {
        return implVerify(bArr, i, bArr2, i2, bArr3, (byte) 0, bArr4, i3, i4);
    }

    public static boolean verifyPrehash(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, byte[] bArr4, int i3) {
        return implVerify(bArr, i, bArr2, i2, bArr3, (byte) 1, bArr4, i3, 64);
    }

    public static boolean verifyPrehash(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, Xof xof) {
        byte[] bArr4 = new byte[64];
        if (64 != xof.doFinal(bArr4, 0, 64)) {
            throw new IllegalArgumentException("ph");
        }
        return implVerify(bArr, i, bArr2, i2, bArr3, (byte) 1, bArr4, 0, bArr4.length);
    }
}
