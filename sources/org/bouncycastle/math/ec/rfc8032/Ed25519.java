package org.bouncycastle.math.ec.rfc8032;

import java.security.SecureRandom;
import org.apache.poi.ss.usermodel.ExcelStyleDateFormatter;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.math.ec.rfc7748.X25519;
import org.bouncycastle.math.ec.rfc7748.X25519Field;
import org.bouncycastle.math.raw.Interleave;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat256;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/rfc8032/Ed25519.class */
public abstract class Ed25519 {
    private static final long M28L = 268435455;
    private static final long M32L = 4294967295L;
    private static final int POINT_BYTES = 32;
    private static final int SCALAR_INTS = 8;
    private static final int SCALAR_BYTES = 32;
    public static final int PREHASH_SIZE = 64;
    public static final int PUBLIC_KEY_SIZE = 32;
    public static final int SECRET_KEY_SIZE = 32;
    public static final int SIGNATURE_SIZE = 64;
    private static final int L0 = -50998291;
    private static final int L1 = 19280294;
    private static final int L2 = 127719000;
    private static final int L3 = -6428113;
    private static final int L4 = 5343;
    private static final int WNAF_WIDTH_BASE = 7;
    private static final int PRECOMP_BLOCKS = 8;
    private static final int PRECOMP_TEETH = 4;
    private static final int PRECOMP_SPACING = 8;
    private static final int PRECOMP_POINTS = 8;
    private static final int PRECOMP_MASK = 7;
    private static final byte[] DOM2_PREFIX = Strings.toByteArray("SigEd25519 no Ed25519 collisions");
    private static final int[] P = {-19, -1, -1, -1, -1, -1, -1, Integer.MAX_VALUE};
    private static final int[] L = {1559614445, 1477600026, -1560830762, 350157278, 0, 0, 0, 268435456};
    private static final int[] B_x = {52811034, 25909283, 8072341, 50637101, 13785486, 30858332, 20483199, 20966410, 43936626, 4379245};
    private static final int[] B_y = {40265304, 26843545, 6710886, 53687091, 13421772, 40265318, 26843545, 6710886, 53687091, 13421772};
    private static final int[] C_d = {56195235, 47411844, 25868126, 40503822, ExcelStyleDateFormatter.S_BRACKET_SYMBOL, 58321048, 30416477, 31930572, 57760639, 10749657};
    private static final int[] C_d2 = {45281625, 27714825, 18181821, 13898781, 114729, 49533232, 60832955, 30306712, 48412415, 4722099};
    private static final int[] C_d4 = {23454386, 55429651, 2809210, 27797563, 229458, 31957600, 54557047, 27058993, 29715967, 9444199};
    private static final Object precompLock = new Object();
    private static PointExt[] precompBaseTable = null;
    private static int[] precompBase = null;

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/rfc8032/Ed25519$Algorithm.class */
    public static final class Algorithm {
        public static final int Ed25519 = 0;
        public static final int Ed25519ctx = 1;
        public static final int Ed25519ph = 2;
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/rfc8032/Ed25519$PointAccum.class */
    private static class PointAccum {
        int[] x;
        int[] y;
        int[] z;
        int[] u;
        int[] v;

        private PointAccum() {
            this.x = X25519Field.create();
            this.y = X25519Field.create();
            this.z = X25519Field.create();
            this.u = X25519Field.create();
            this.v = X25519Field.create();
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/rfc8032/Ed25519$PointAffine.class */
    private static class PointAffine {
        int[] x;
        int[] y;

        private PointAffine() {
            this.x = X25519Field.create();
            this.y = X25519Field.create();
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/rfc8032/Ed25519$PointExt.class */
    private static class PointExt {
        int[] x;
        int[] y;
        int[] z;
        int[] t;

        private PointExt() {
            this.x = X25519Field.create();
            this.y = X25519Field.create();
            this.z = X25519Field.create();
            this.t = X25519Field.create();
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/rfc8032/Ed25519$PointPrecomp.class */
    private static class PointPrecomp {
        int[] ypx_h;
        int[] ymx_h;
        int[] xyd;

        private PointPrecomp() {
            this.ypx_h = X25519Field.create();
            this.ymx_h = X25519Field.create();
            this.xyd = X25519Field.create();
        }
    }

    private static byte[] calculateS(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        int[] iArr = new int[16];
        decodeScalar(bArr, 0, iArr);
        int[] iArr2 = new int[8];
        decodeScalar(bArr2, 0, iArr2);
        int[] iArr3 = new int[8];
        decodeScalar(bArr3, 0, iArr3);
        Nat256.mulAddTo(iArr2, iArr3, iArr);
        byte[] bArr4 = new byte[64];
        for (int i = 0; i < iArr.length; i++) {
            encode32(iArr[i], bArr4, i * 4);
        }
        return reduceScalar(bArr4);
    }

    private static boolean checkContextVar(byte[] bArr, byte b) {
        return (bArr == null && b == 0) || (bArr != null && bArr.length < 256);
    }

    private static int checkPoint(int[] iArr, int[] iArr2) {
        int[] iArrCreate = X25519Field.create();
        int[] iArrCreate2 = X25519Field.create();
        int[] iArrCreate3 = X25519Field.create();
        X25519Field.sqr(iArr, iArrCreate2);
        X25519Field.sqr(iArr2, iArrCreate3);
        X25519Field.mul(iArrCreate2, iArrCreate3, iArrCreate);
        X25519Field.sub(iArrCreate3, iArrCreate2, iArrCreate3);
        X25519Field.mul(iArrCreate, C_d, iArrCreate);
        X25519Field.addOne(iArrCreate);
        X25519Field.sub(iArrCreate, iArrCreate3, iArrCreate);
        X25519Field.normalize(iArrCreate);
        return X25519Field.isZero(iArrCreate);
    }

    private static int checkPoint(int[] iArr, int[] iArr2, int[] iArr3) {
        int[] iArrCreate = X25519Field.create();
        int[] iArrCreate2 = X25519Field.create();
        int[] iArrCreate3 = X25519Field.create();
        int[] iArrCreate4 = X25519Field.create();
        X25519Field.sqr(iArr, iArrCreate2);
        X25519Field.sqr(iArr2, iArrCreate3);
        X25519Field.sqr(iArr3, iArrCreate4);
        X25519Field.mul(iArrCreate2, iArrCreate3, iArrCreate);
        X25519Field.sub(iArrCreate3, iArrCreate2, iArrCreate3);
        X25519Field.mul(iArrCreate3, iArrCreate4, iArrCreate3);
        X25519Field.sqr(iArrCreate4, iArrCreate4);
        X25519Field.mul(iArrCreate, C_d, iArrCreate);
        X25519Field.add(iArrCreate, iArrCreate4, iArrCreate);
        X25519Field.sub(iArrCreate, iArrCreate3, iArrCreate);
        X25519Field.normalize(iArrCreate);
        return X25519Field.isZero(iArrCreate);
    }

    private static boolean checkPointVar(byte[] bArr) {
        int[] iArr = new int[8];
        decode32(bArr, 0, iArr, 0, 8);
        iArr[7] = iArr[7] & Integer.MAX_VALUE;
        return !Nat256.gte(iArr, P);
    }

    private static boolean checkScalarVar(byte[] bArr) {
        int[] iArr = new int[8];
        decodeScalar(bArr, 0, iArr);
        return !Nat256.gte(iArr, L);
    }

    private static Digest createDigest() {
        return new SHA512Digest();
    }

    public static Digest createPrehash() {
        return createDigest();
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

    private static boolean decodePointVar(byte[] bArr, int i, boolean z, PointAffine pointAffine) {
        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, i, i + 32);
        if (!checkPointVar(bArrCopyOfRange)) {
            return false;
        }
        int i2 = (bArrCopyOfRange[31] & 128) >>> 7;
        bArrCopyOfRange[31] = (byte) (bArrCopyOfRange[31] & Byte.MAX_VALUE);
        X25519Field.decode(bArrCopyOfRange, 0, pointAffine.y);
        int[] iArrCreate = X25519Field.create();
        int[] iArrCreate2 = X25519Field.create();
        X25519Field.sqr(pointAffine.y, iArrCreate);
        X25519Field.mul(C_d, iArrCreate, iArrCreate2);
        X25519Field.subOne(iArrCreate);
        X25519Field.addOne(iArrCreate2);
        if (!X25519Field.sqrtRatioVar(iArrCreate, iArrCreate2, pointAffine.x)) {
            return false;
        }
        X25519Field.normalize(pointAffine.x);
        if (i2 == 1 && X25519Field.isZeroVar(pointAffine.x)) {
            return false;
        }
        if (!(z ^ (i2 != (pointAffine.x[0] & 1)))) {
            return true;
        }
        X25519Field.negate(pointAffine.x, pointAffine.x);
        return true;
    }

    private static void decodeScalar(byte[] bArr, int i, int[] iArr) {
        decode32(bArr, i, iArr, 0, 8);
    }

    private static void dom2(Digest digest, byte b, byte[] bArr) {
        if (bArr != null) {
            digest.update(DOM2_PREFIX, 0, DOM2_PREFIX.length);
            digest.update(b);
            digest.update((byte) bArr.length);
            digest.update(bArr, 0, bArr.length);
        }
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

    private static int encodePoint(PointAccum pointAccum, byte[] bArr, int i) {
        int[] iArrCreate = X25519Field.create();
        int[] iArrCreate2 = X25519Field.create();
        X25519Field.inv(pointAccum.z, iArrCreate2);
        X25519Field.mul(pointAccum.x, iArrCreate2, iArrCreate);
        X25519Field.mul(pointAccum.y, iArrCreate2, iArrCreate2);
        X25519Field.normalize(iArrCreate);
        X25519Field.normalize(iArrCreate2);
        int iCheckPoint = checkPoint(iArrCreate, iArrCreate2);
        X25519Field.encode(iArrCreate2, bArr, i);
        int i2 = (i + 32) - 1;
        bArr[i2] = (byte) (bArr[i2] | ((iArrCreate[0] & 1) << 7));
        return iCheckPoint;
    }

    public static void generatePrivateKey(SecureRandom secureRandom, byte[] bArr) {
        secureRandom.nextBytes(bArr);
    }

    public static void generatePublicKey(byte[] bArr, int i, byte[] bArr2, int i2) {
        Digest digestCreateDigest = createDigest();
        byte[] bArr3 = new byte[digestCreateDigest.getDigestSize()];
        digestCreateDigest.update(bArr, i, 32);
        digestCreateDigest.doFinal(bArr3, 0);
        byte[] bArr4 = new byte[32];
        pruneScalar(bArr3, 0, bArr4);
        scalarMultBaseEncoded(bArr4, bArr2, i2);
    }

    private static int getWindow4(int[] iArr, int i) {
        return (iArr[i >>> 3] >>> ((i & 7) << 2)) & 15;
    }

    private static byte[] getWNAF(int[] iArr, int i) {
        int[] iArr2 = new int[16];
        int length = iArr2.length;
        int i2 = 0;
        int i3 = 8;
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
        byte[] bArr = new byte[253];
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

    private static void implSign(Digest digest, byte[] bArr, byte[] bArr2, byte[] bArr3, int i, byte[] bArr4, byte b, byte[] bArr5, int i2, int i3, byte[] bArr6, int i4) {
        dom2(digest, b, bArr4);
        digest.update(bArr, 32, 32);
        digest.update(bArr5, i2, i3);
        digest.doFinal(bArr, 0);
        byte[] bArrReduceScalar = reduceScalar(bArr);
        byte[] bArr7 = new byte[32];
        scalarMultBaseEncoded(bArrReduceScalar, bArr7, 0);
        dom2(digest, b, bArr4);
        digest.update(bArr7, 0, 32);
        digest.update(bArr3, i, 32);
        digest.update(bArr5, i2, i3);
        digest.doFinal(bArr, 0);
        byte[] bArrCalculateS = calculateS(bArrReduceScalar, reduceScalar(bArr), bArr2);
        System.arraycopy(bArr7, 0, bArr6, i4, 32);
        System.arraycopy(bArrCalculateS, 0, bArr6, i4 + 32, 32);
    }

    private static void implSign(byte[] bArr, int i, byte[] bArr2, byte b, byte[] bArr3, int i2, int i3, byte[] bArr4, int i4) {
        if (!checkContextVar(bArr2, b)) {
            throw new IllegalArgumentException("ctx");
        }
        Digest digestCreateDigest = createDigest();
        byte[] bArr5 = new byte[digestCreateDigest.getDigestSize()];
        digestCreateDigest.update(bArr, i, 32);
        digestCreateDigest.doFinal(bArr5, 0);
        byte[] bArr6 = new byte[32];
        pruneScalar(bArr5, 0, bArr6);
        byte[] bArr7 = new byte[32];
        scalarMultBaseEncoded(bArr6, bArr7, 0);
        implSign(digestCreateDigest, bArr5, bArr6, bArr7, 0, bArr2, b, bArr3, i2, i3, bArr4, i4);
    }

    private static void implSign(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, byte b, byte[] bArr4, int i3, int i4, byte[] bArr5, int i5) {
        if (!checkContextVar(bArr3, b)) {
            throw new IllegalArgumentException("ctx");
        }
        Digest digestCreateDigest = createDigest();
        byte[] bArr6 = new byte[digestCreateDigest.getDigestSize()];
        digestCreateDigest.update(bArr, i, 32);
        digestCreateDigest.doFinal(bArr6, 0);
        byte[] bArr7 = new byte[32];
        pruneScalar(bArr6, 0, bArr7);
        implSign(digestCreateDigest, bArr6, bArr7, bArr2, i2, bArr3, b, bArr4, i3, i4, bArr5, i5);
    }

    private static boolean implVerify(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, byte b, byte[] bArr4, int i3, int i4) {
        if (!checkContextVar(bArr3, b)) {
            throw new IllegalArgumentException("ctx");
        }
        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, i, i + 32);
        byte[] bArrCopyOfRange2 = Arrays.copyOfRange(bArr, i + 32, i + 64);
        if (!checkPointVar(bArrCopyOfRange) || !checkScalarVar(bArrCopyOfRange2)) {
            return false;
        }
        PointAffine pointAffine = new PointAffine();
        if (!decodePointVar(bArr2, i2, true, pointAffine)) {
            return false;
        }
        Digest digestCreateDigest = createDigest();
        byte[] bArr5 = new byte[digestCreateDigest.getDigestSize()];
        dom2(digestCreateDigest, b, bArr3);
        digestCreateDigest.update(bArrCopyOfRange, 0, 32);
        digestCreateDigest.update(bArr2, i2, 32);
        digestCreateDigest.update(bArr4, i3, i4);
        digestCreateDigest.doFinal(bArr5, 0);
        byte[] bArrReduceScalar = reduceScalar(bArr5);
        int[] iArr = new int[8];
        decodeScalar(bArrCopyOfRange2, 0, iArr);
        int[] iArr2 = new int[8];
        decodeScalar(bArrReduceScalar, 0, iArr2);
        PointAccum pointAccum = new PointAccum();
        scalarMultStrausVar(iArr, iArr2, pointAffine, pointAccum);
        byte[] bArr6 = new byte[32];
        return 0 != encodePoint(pointAccum, bArr6, 0) && Arrays.areEqual(bArr6, bArrCopyOfRange);
    }

    private static void pointAdd(PointExt pointExt, PointAccum pointAccum) {
        int[] iArrCreate = X25519Field.create();
        int[] iArrCreate2 = X25519Field.create();
        int[] iArrCreate3 = X25519Field.create();
        int[] iArrCreate4 = X25519Field.create();
        int[] iArr = pointAccum.u;
        int[] iArrCreate5 = X25519Field.create();
        int[] iArrCreate6 = X25519Field.create();
        int[] iArr2 = pointAccum.v;
        X25519Field.apm(pointAccum.y, pointAccum.x, iArrCreate2, iArrCreate);
        X25519Field.apm(pointExt.y, pointExt.x, iArrCreate4, iArrCreate3);
        X25519Field.mul(iArrCreate, iArrCreate3, iArrCreate);
        X25519Field.mul(iArrCreate2, iArrCreate4, iArrCreate2);
        X25519Field.mul(pointAccum.u, pointAccum.v, iArrCreate3);
        X25519Field.mul(iArrCreate3, pointExt.t, iArrCreate3);
        X25519Field.mul(iArrCreate3, C_d2, iArrCreate3);
        X25519Field.mul(pointAccum.z, pointExt.z, iArrCreate4);
        X25519Field.add(iArrCreate4, iArrCreate4, iArrCreate4);
        X25519Field.apm(iArrCreate2, iArrCreate, iArr2, iArr);
        X25519Field.apm(iArrCreate4, iArrCreate3, iArrCreate6, iArrCreate5);
        X25519Field.carry(iArrCreate6);
        X25519Field.mul(iArr, iArrCreate5, pointAccum.x);
        X25519Field.mul(iArrCreate6, iArr2, pointAccum.y);
        X25519Field.mul(iArrCreate5, iArrCreate6, pointAccum.z);
    }

    private static void pointAdd(PointExt pointExt, PointExt pointExt2) {
        int[] iArrCreate = X25519Field.create();
        int[] iArrCreate2 = X25519Field.create();
        int[] iArrCreate3 = X25519Field.create();
        int[] iArrCreate4 = X25519Field.create();
        int[] iArrCreate5 = X25519Field.create();
        int[] iArrCreate6 = X25519Field.create();
        int[] iArrCreate7 = X25519Field.create();
        int[] iArrCreate8 = X25519Field.create();
        X25519Field.apm(pointExt.y, pointExt.x, iArrCreate2, iArrCreate);
        X25519Field.apm(pointExt2.y, pointExt2.x, iArrCreate4, iArrCreate3);
        X25519Field.mul(iArrCreate, iArrCreate3, iArrCreate);
        X25519Field.mul(iArrCreate2, iArrCreate4, iArrCreate2);
        X25519Field.mul(pointExt.t, pointExt2.t, iArrCreate3);
        X25519Field.mul(iArrCreate3, C_d2, iArrCreate3);
        X25519Field.mul(pointExt.z, pointExt2.z, iArrCreate4);
        X25519Field.add(iArrCreate4, iArrCreate4, iArrCreate4);
        X25519Field.apm(iArrCreate2, iArrCreate, iArrCreate8, iArrCreate5);
        X25519Field.apm(iArrCreate4, iArrCreate3, iArrCreate7, iArrCreate6);
        X25519Field.carry(iArrCreate7);
        X25519Field.mul(iArrCreate5, iArrCreate6, pointExt2.x);
        X25519Field.mul(iArrCreate7, iArrCreate8, pointExt2.y);
        X25519Field.mul(iArrCreate6, iArrCreate7, pointExt2.z);
        X25519Field.mul(iArrCreate5, iArrCreate8, pointExt2.t);
    }

    private static void pointAddVar(boolean z, PointExt pointExt, PointAccum pointAccum) {
        int[] iArr;
        int[] iArr2;
        int[] iArr3;
        int[] iArr4;
        int[] iArrCreate = X25519Field.create();
        int[] iArrCreate2 = X25519Field.create();
        int[] iArrCreate3 = X25519Field.create();
        int[] iArrCreate4 = X25519Field.create();
        int[] iArr5 = pointAccum.u;
        int[] iArrCreate5 = X25519Field.create();
        int[] iArrCreate6 = X25519Field.create();
        int[] iArr6 = pointAccum.v;
        if (z) {
            iArr = iArrCreate4;
            iArr2 = iArrCreate3;
            iArr3 = iArrCreate6;
            iArr4 = iArrCreate5;
        } else {
            iArr = iArrCreate3;
            iArr2 = iArrCreate4;
            iArr3 = iArrCreate5;
            iArr4 = iArrCreate6;
        }
        X25519Field.apm(pointAccum.y, pointAccum.x, iArrCreate2, iArrCreate);
        X25519Field.apm(pointExt.y, pointExt.x, iArr2, iArr);
        X25519Field.mul(iArrCreate, iArrCreate3, iArrCreate);
        X25519Field.mul(iArrCreate2, iArrCreate4, iArrCreate2);
        X25519Field.mul(pointAccum.u, pointAccum.v, iArrCreate3);
        X25519Field.mul(iArrCreate3, pointExt.t, iArrCreate3);
        X25519Field.mul(iArrCreate3, C_d2, iArrCreate3);
        X25519Field.mul(pointAccum.z, pointExt.z, iArrCreate4);
        X25519Field.add(iArrCreate4, iArrCreate4, iArrCreate4);
        X25519Field.apm(iArrCreate2, iArrCreate, iArr6, iArr5);
        X25519Field.apm(iArrCreate4, iArrCreate3, iArr4, iArr3);
        X25519Field.carry(iArr4);
        X25519Field.mul(iArr5, iArrCreate5, pointAccum.x);
        X25519Field.mul(iArrCreate6, iArr6, pointAccum.y);
        X25519Field.mul(iArrCreate5, iArrCreate6, pointAccum.z);
    }

    private static void pointAddVar(boolean z, PointExt pointExt, PointExt pointExt2, PointExt pointExt3) {
        int[] iArr;
        int[] iArr2;
        int[] iArr3;
        int[] iArr4;
        int[] iArrCreate = X25519Field.create();
        int[] iArrCreate2 = X25519Field.create();
        int[] iArrCreate3 = X25519Field.create();
        int[] iArrCreate4 = X25519Field.create();
        int[] iArrCreate5 = X25519Field.create();
        int[] iArrCreate6 = X25519Field.create();
        int[] iArrCreate7 = X25519Field.create();
        int[] iArrCreate8 = X25519Field.create();
        if (z) {
            iArr = iArrCreate4;
            iArr2 = iArrCreate3;
            iArr3 = iArrCreate7;
            iArr4 = iArrCreate6;
        } else {
            iArr = iArrCreate3;
            iArr2 = iArrCreate4;
            iArr3 = iArrCreate6;
            iArr4 = iArrCreate7;
        }
        X25519Field.apm(pointExt.y, pointExt.x, iArrCreate2, iArrCreate);
        X25519Field.apm(pointExt2.y, pointExt2.x, iArr2, iArr);
        X25519Field.mul(iArrCreate, iArrCreate3, iArrCreate);
        X25519Field.mul(iArrCreate2, iArrCreate4, iArrCreate2);
        X25519Field.mul(pointExt.t, pointExt2.t, iArrCreate3);
        X25519Field.mul(iArrCreate3, C_d2, iArrCreate3);
        X25519Field.mul(pointExt.z, pointExt2.z, iArrCreate4);
        X25519Field.add(iArrCreate4, iArrCreate4, iArrCreate4);
        X25519Field.apm(iArrCreate2, iArrCreate, iArrCreate8, iArrCreate5);
        X25519Field.apm(iArrCreate4, iArrCreate3, iArr4, iArr3);
        X25519Field.carry(iArr4);
        X25519Field.mul(iArrCreate5, iArrCreate6, pointExt3.x);
        X25519Field.mul(iArrCreate7, iArrCreate8, pointExt3.y);
        X25519Field.mul(iArrCreate6, iArrCreate7, pointExt3.z);
        X25519Field.mul(iArrCreate5, iArrCreate8, pointExt3.t);
    }

    private static void pointAddPrecomp(PointPrecomp pointPrecomp, PointAccum pointAccum) {
        int[] iArrCreate = X25519Field.create();
        int[] iArrCreate2 = X25519Field.create();
        int[] iArrCreate3 = X25519Field.create();
        int[] iArr = pointAccum.u;
        int[] iArrCreate4 = X25519Field.create();
        int[] iArrCreate5 = X25519Field.create();
        int[] iArr2 = pointAccum.v;
        X25519Field.apm(pointAccum.y, pointAccum.x, iArrCreate2, iArrCreate);
        X25519Field.mul(iArrCreate, pointPrecomp.ymx_h, iArrCreate);
        X25519Field.mul(iArrCreate2, pointPrecomp.ypx_h, iArrCreate2);
        X25519Field.mul(pointAccum.u, pointAccum.v, iArrCreate3);
        X25519Field.mul(iArrCreate3, pointPrecomp.xyd, iArrCreate3);
        X25519Field.apm(iArrCreate2, iArrCreate, iArr2, iArr);
        X25519Field.apm(pointAccum.z, iArrCreate3, iArrCreate5, iArrCreate4);
        X25519Field.carry(iArrCreate5);
        X25519Field.mul(iArr, iArrCreate4, pointAccum.x);
        X25519Field.mul(iArrCreate5, iArr2, pointAccum.y);
        X25519Field.mul(iArrCreate4, iArrCreate5, pointAccum.z);
    }

    private static PointExt pointCopy(PointAccum pointAccum) {
        PointExt pointExt = new PointExt();
        X25519Field.copy(pointAccum.x, 0, pointExt.x, 0);
        X25519Field.copy(pointAccum.y, 0, pointExt.y, 0);
        X25519Field.copy(pointAccum.z, 0, pointExt.z, 0);
        X25519Field.mul(pointAccum.u, pointAccum.v, pointExt.t);
        return pointExt;
    }

    private static PointExt pointCopy(PointAffine pointAffine) {
        PointExt pointExt = new PointExt();
        X25519Field.copy(pointAffine.x, 0, pointExt.x, 0);
        X25519Field.copy(pointAffine.y, 0, pointExt.y, 0);
        pointExtendXY(pointExt);
        return pointExt;
    }

    private static PointExt pointCopy(PointExt pointExt) {
        PointExt pointExt2 = new PointExt();
        pointCopy(pointExt, pointExt2);
        return pointExt2;
    }

    private static void pointCopy(PointAffine pointAffine, PointAccum pointAccum) {
        X25519Field.copy(pointAffine.x, 0, pointAccum.x, 0);
        X25519Field.copy(pointAffine.y, 0, pointAccum.y, 0);
        pointExtendXY(pointAccum);
    }

    private static void pointCopy(PointExt pointExt, PointExt pointExt2) {
        X25519Field.copy(pointExt.x, 0, pointExt2.x, 0);
        X25519Field.copy(pointExt.y, 0, pointExt2.y, 0);
        X25519Field.copy(pointExt.z, 0, pointExt2.z, 0);
        X25519Field.copy(pointExt.t, 0, pointExt2.t, 0);
    }

    private static void pointDouble(PointAccum pointAccum) {
        int[] iArrCreate = X25519Field.create();
        int[] iArrCreate2 = X25519Field.create();
        int[] iArrCreate3 = X25519Field.create();
        int[] iArr = pointAccum.u;
        int[] iArrCreate4 = X25519Field.create();
        int[] iArrCreate5 = X25519Field.create();
        int[] iArr2 = pointAccum.v;
        X25519Field.sqr(pointAccum.x, iArrCreate);
        X25519Field.sqr(pointAccum.y, iArrCreate2);
        X25519Field.sqr(pointAccum.z, iArrCreate3);
        X25519Field.add(iArrCreate3, iArrCreate3, iArrCreate3);
        X25519Field.apm(iArrCreate, iArrCreate2, iArr2, iArrCreate5);
        X25519Field.add(pointAccum.x, pointAccum.y, iArr);
        X25519Field.sqr(iArr, iArr);
        X25519Field.sub(iArr2, iArr, iArr);
        X25519Field.add(iArrCreate3, iArrCreate5, iArrCreate4);
        X25519Field.carry(iArrCreate4);
        X25519Field.mul(iArr, iArrCreate4, pointAccum.x);
        X25519Field.mul(iArrCreate5, iArr2, pointAccum.y);
        X25519Field.mul(iArrCreate4, iArrCreate5, pointAccum.z);
    }

    private static void pointExtendXY(PointAccum pointAccum) {
        X25519Field.one(pointAccum.z);
        X25519Field.copy(pointAccum.x, 0, pointAccum.u, 0);
        X25519Field.copy(pointAccum.y, 0, pointAccum.v, 0);
    }

    private static void pointExtendXY(PointExt pointExt) {
        X25519Field.one(pointExt.z);
        X25519Field.mul(pointExt.x, pointExt.y, pointExt.t);
    }

    private static void pointLookup(int i, int i2, PointPrecomp pointPrecomp) {
        int i3 = i * 8 * 3 * 10;
        for (int i4 = 0; i4 < 8; i4++) {
            int i5 = ((i4 ^ i2) - 1) >> 31;
            X25519Field.cmov(i5, precompBase, i3, pointPrecomp.ypx_h, 0);
            int i6 = i3 + 10;
            X25519Field.cmov(i5, precompBase, i6, pointPrecomp.ymx_h, 0);
            int i7 = i6 + 10;
            X25519Field.cmov(i5, precompBase, i7, pointPrecomp.xyd, 0);
            i3 = i7 + 10;
        }
    }

    private static void pointLookup(int[] iArr, int i, int[] iArr2, PointExt pointExt) {
        int window4 = getWindow4(iArr, i);
        int i2 = (window4 >>> 3) ^ 1;
        int i3 = (window4 ^ (-i2)) & 7;
        int i4 = 0;
        for (int i5 = 0; i5 < 8; i5++) {
            int i6 = ((i5 ^ i3) - 1) >> 31;
            X25519Field.cmov(i6, iArr2, i4, pointExt.x, 0);
            int i7 = i4 + 10;
            X25519Field.cmov(i6, iArr2, i7, pointExt.y, 0);
            int i8 = i7 + 10;
            X25519Field.cmov(i6, iArr2, i8, pointExt.z, 0);
            int i9 = i8 + 10;
            X25519Field.cmov(i6, iArr2, i9, pointExt.t, 0);
            i4 = i9 + 10;
        }
        X25519Field.cnegate(i2, pointExt.x);
        X25519Field.cnegate(i2, pointExt.t);
    }

    private static void pointLookup(int[] iArr, int i, PointExt pointExt) {
        int i2 = 40 * i;
        X25519Field.copy(iArr, i2, pointExt.x, 0);
        int i3 = i2 + 10;
        X25519Field.copy(iArr, i3, pointExt.y, 0);
        int i4 = i3 + 10;
        X25519Field.copy(iArr, i4, pointExt.z, 0);
        X25519Field.copy(iArr, i4 + 10, pointExt.t, 0);
    }

    private static int[] pointPrecomp(PointAffine pointAffine, int i) {
        PointExt pointExtPointCopy = pointCopy(pointAffine);
        PointExt pointExtPointCopy2 = pointCopy(pointExtPointCopy);
        pointAdd(pointExtPointCopy, pointExtPointCopy2);
        int[] iArrCreateTable = X25519Field.createTable(i * 4);
        int i2 = 0;
        int i3 = 0;
        while (true) {
            X25519Field.copy(pointExtPointCopy.x, 0, iArrCreateTable, i2);
            int i4 = i2 + 10;
            X25519Field.copy(pointExtPointCopy.y, 0, iArrCreateTable, i4);
            int i5 = i4 + 10;
            X25519Field.copy(pointExtPointCopy.z, 0, iArrCreateTable, i5);
            int i6 = i5 + 10;
            X25519Field.copy(pointExtPointCopy.t, 0, iArrCreateTable, i6);
            i2 = i6 + 10;
            i3++;
            if (i3 == i) {
                return iArrCreateTable;
            }
            pointAdd(pointExtPointCopy2, pointExtPointCopy);
        }
    }

    private static PointExt[] pointPrecompVar(PointExt pointExt, int i) {
        PointExt pointExt2 = new PointExt();
        pointAddVar(false, pointExt, pointExt, pointExt2);
        PointExt[] pointExtArr = new PointExt[i];
        pointExtArr[0] = pointCopy(pointExt);
        for (int i2 = 1; i2 < i; i2++) {
            PointExt pointExt3 = pointExtArr[i2 - 1];
            PointExt pointExt4 = new PointExt();
            pointExtArr[i2] = pointExt4;
            pointAddVar(false, pointExt3, pointExt2, pointExt4);
        }
        return pointExtArr;
    }

    private static void pointSetNeutral(PointAccum pointAccum) {
        X25519Field.zero(pointAccum.x);
        X25519Field.one(pointAccum.y);
        X25519Field.one(pointAccum.z);
        X25519Field.zero(pointAccum.u);
        X25519Field.one(pointAccum.v);
    }

    private static void pointSetNeutral(PointExt pointExt) {
        X25519Field.zero(pointExt.x);
        X25519Field.one(pointExt.y);
        X25519Field.one(pointExt.z);
        X25519Field.zero(pointExt.t);
    }

    public static void precompute() {
        synchronized (precompLock) {
            if (precompBase != null) {
                return;
            }
            PointExt pointExt = new PointExt();
            X25519Field.copy(B_x, 0, pointExt.x, 0);
            X25519Field.copy(B_y, 0, pointExt.y, 0);
            pointExtendXY(pointExt);
            precompBaseTable = pointPrecompVar(pointExt, 32);
            PointAccum pointAccum = new PointAccum();
            X25519Field.copy(B_x, 0, pointAccum.x, 0);
            X25519Field.copy(B_y, 0, pointAccum.y, 0);
            pointExtendXY(pointAccum);
            precompBase = X25519Field.createTable(192);
            int i = 0;
            for (int i2 = 0; i2 < 8; i2++) {
                PointExt[] pointExtArr = new PointExt[4];
                PointExt pointExt2 = new PointExt();
                pointSetNeutral(pointExt2);
                for (int i3 = 0; i3 < 4; i3++) {
                    pointAddVar(true, pointExt2, pointCopy(pointAccum), pointExt2);
                    pointDouble(pointAccum);
                    pointExtArr[i3] = pointCopy(pointAccum);
                    if (i2 + i3 != 10) {
                        for (int i4 = 1; i4 < 8; i4++) {
                            pointDouble(pointAccum);
                        }
                    }
                }
                PointExt[] pointExtArr2 = new PointExt[8];
                int i5 = 0 + 1;
                pointExtArr2[0] = pointExt2;
                for (int i6 = 0; i6 < 3; i6++) {
                    int i7 = 1 << i6;
                    int i8 = 0;
                    while (i8 < i7) {
                        PointExt pointExt3 = pointExtArr2[i5 - i7];
                        PointExt pointExt4 = pointExtArr[i6];
                        PointExt pointExt5 = new PointExt();
                        pointExtArr2[i5] = pointExt5;
                        pointAddVar(false, pointExt3, pointExt4, pointExt5);
                        i8++;
                        i5++;
                    }
                }
                for (int i9 = 0; i9 < 8; i9++) {
                    PointExt pointExt6 = pointExtArr2[i9];
                    int[] iArrCreate = X25519Field.create();
                    int[] iArrCreate2 = X25519Field.create();
                    X25519Field.add(pointExt6.z, pointExt6.z, iArrCreate);
                    X25519Field.inv(iArrCreate, iArrCreate2);
                    X25519Field.mul(pointExt6.x, iArrCreate2, iArrCreate);
                    X25519Field.mul(pointExt6.y, iArrCreate2, iArrCreate2);
                    PointPrecomp pointPrecomp = new PointPrecomp();
                    X25519Field.apm(iArrCreate2, iArrCreate, pointPrecomp.ypx_h, pointPrecomp.ymx_h);
                    X25519Field.mul(iArrCreate, iArrCreate2, pointPrecomp.xyd);
                    X25519Field.mul(pointPrecomp.xyd, C_d4, pointPrecomp.xyd);
                    X25519Field.normalize(pointPrecomp.ypx_h);
                    X25519Field.normalize(pointPrecomp.ymx_h);
                    X25519Field.copy(pointPrecomp.ypx_h, 0, precompBase, i);
                    int i10 = i + 10;
                    X25519Field.copy(pointPrecomp.ymx_h, 0, precompBase, i10);
                    int i11 = i10 + 10;
                    X25519Field.copy(pointPrecomp.xyd, 0, precompBase, i11);
                    i = i11 + 10;
                }
            }
        }
    }

    private static void pruneScalar(byte[] bArr, int i, byte[] bArr2) {
        System.arraycopy(bArr, i, bArr2, 0, 32);
        bArr2[0] = (byte) (bArr2[0] & 248);
        bArr2[31] = (byte) (bArr2[31] & Byte.MAX_VALUE);
        bArr2[31] = (byte) (bArr2[31] | 64);
    }

    private static byte[] reduceScalar(byte[] bArr) {
        long jDecode32 = decode32(bArr, 49) & 4294967295L;
        long jDecode322 = decode32(bArr, 56) & 4294967295L;
        long j = bArr[63] & 255;
        long jDecode24 = ((decode24(bArr, 32) << 4) & 4294967295L) - (j * (-50998291));
        long jDecode323 = (decode32(bArr, 35) & 4294967295L) - (j * 19280294);
        long jDecode242 = ((decode24(bArr, 39) << 4) & 4294967295L) - (j * 127719000);
        long jDecode324 = (decode32(bArr, 42) & 4294967295L) - (j * (-6428113));
        long jDecode243 = ((decode24(bArr, 46) << 4) & 4294967295L) - (j * 5343);
        long jDecode244 = ((decode24(bArr, 60) << 4) & 4294967295L) + (jDecode322 >> 28);
        long j2 = jDecode322 & M28L;
        long jDecode325 = (decode32(bArr, 28) & 4294967295L) - (jDecode244 * (-50998291));
        long j3 = jDecode24 - (jDecode244 * 19280294);
        long j4 = jDecode323 - (jDecode244 * 127719000);
        long j5 = jDecode242 - (jDecode244 * (-6428113));
        long j6 = jDecode324 - (jDecode244 * 5343);
        long jDecode245 = ((decode24(bArr, 25) << 4) & 4294967295L) - (j2 * (-50998291));
        long j7 = jDecode325 - (j2 * 19280294);
        long j8 = j3 - (j2 * 127719000);
        long j9 = j4 - (j2 * (-6428113));
        long j10 = j5 - (j2 * 5343);
        long jDecode246 = ((decode24(bArr, 53) << 4) & 4294967295L) + (jDecode32 >> 28);
        long j11 = jDecode32 & M28L;
        long jDecode326 = (decode32(bArr, 21) & 4294967295L) - (jDecode246 * (-50998291));
        long j12 = jDecode245 - (jDecode246 * 19280294);
        long j13 = j7 - (jDecode246 * 127719000);
        long j14 = j8 - (jDecode246 * (-6428113));
        long j15 = j9 - (jDecode246 * 5343);
        long jDecode247 = ((decode24(bArr, 18) << 4) & 4294967295L) - (j11 * (-50998291));
        long j16 = jDecode326 - (j11 * 19280294);
        long j17 = j12 - (j11 * 127719000);
        long j18 = j13 - (j11 * (-6428113));
        long j19 = j14 - (j11 * 5343);
        long j20 = jDecode243 + (j6 >> 28);
        long j21 = j6 & M28L;
        long jDecode327 = (decode32(bArr, 14) & 4294967295L) - (j20 * (-50998291));
        long j22 = jDecode247 - (j20 * 19280294);
        long j23 = j16 - (j20 * 127719000);
        long j24 = j17 - (j20 * (-6428113));
        long j25 = j18 - (j20 * 5343);
        long j26 = j21 + (j10 >> 28);
        long j27 = j10 & M28L;
        long jDecode248 = ((decode24(bArr, 11) << 4) & 4294967295L) - (j26 * (-50998291));
        long j28 = jDecode327 - (j26 * 19280294);
        long j29 = j22 - (j26 * 127719000);
        long j30 = j23 - (j26 * (-6428113));
        long j31 = j24 - (j26 * 5343);
        long j32 = j27 + (j15 >> 28);
        long j33 = j15 & M28L;
        long jDecode328 = (decode32(bArr, 7) & 4294967295L) - (j32 * (-50998291));
        long j34 = jDecode248 - (j32 * 19280294);
        long j35 = j28 - (j32 * 127719000);
        long j36 = j29 - (j32 * (-6428113));
        long j37 = j30 - (j32 * 5343);
        long j38 = j33 + (j19 >> 28);
        long j39 = j19 & M28L;
        long jDecode249 = ((decode24(bArr, 4) << 4) & 4294967295L) - (j38 * (-50998291));
        long j40 = jDecode328 - (j38 * 19280294);
        long j41 = j34 - (j38 * 127719000);
        long j42 = j35 - (j38 * (-6428113));
        long j43 = j36 - (j38 * 5343);
        long j44 = j25 + (j31 >> 28);
        long j45 = j31 & M28L;
        long j46 = j39 + (j44 >> 28);
        long j47 = j44 & M28L;
        long j48 = j47 >>> 27;
        long j49 = j46 + j48;
        long jDecode329 = (decode32(bArr, 0) & 4294967295L) - (j49 * (-50998291));
        long j50 = jDecode249 - (j49 * 19280294);
        long j51 = j40 - (j49 * 127719000);
        long j52 = j41 - (j49 * (-6428113));
        long j53 = j42 - (j49 * 5343);
        long j54 = j50 + (jDecode329 >> 28);
        long j55 = jDecode329 & M28L;
        long j56 = j51 + (j54 >> 28);
        long j57 = j54 & M28L;
        long j58 = j52 + (j56 >> 28);
        long j59 = j56 & M28L;
        long j60 = j53 + (j58 >> 28);
        long j61 = j58 & M28L;
        long j62 = j43 + (j60 >> 28);
        long j63 = j60 & M28L;
        long j64 = j37 + (j62 >> 28);
        long j65 = j62 & M28L;
        long j66 = j45 + (j64 >> 28);
        long j67 = j64 & M28L;
        long j68 = j47 + (j66 >> 28);
        long j69 = j66 & M28L;
        long j70 = j68 >> 28;
        long j71 = j68 & M28L;
        long j72 = j70 - j48;
        long j73 = j55 + (j72 & (-50998291));
        long j74 = j57 + (j72 & 19280294);
        long j75 = j59 + (j72 & 127719000);
        long j76 = j61 + (j72 & (-6428113));
        long j77 = j63 + (j72 & 5343);
        long j78 = j74 + (j73 >> 28);
        long j79 = j73 & M28L;
        long j80 = j75 + (j78 >> 28);
        long j81 = j78 & M28L;
        long j82 = j76 + (j80 >> 28);
        long j83 = j80 & M28L;
        long j84 = j77 + (j82 >> 28);
        long j85 = j82 & M28L;
        long j86 = j65 + (j84 >> 28);
        long j87 = j84 & M28L;
        long j88 = j67 + (j86 >> 28);
        long j89 = j86 & M28L;
        long j90 = j69 + (j88 >> 28);
        long j91 = j88 & M28L;
        long j92 = j71 + (j90 >> 28);
        long j93 = j90 & M28L;
        byte[] bArr2 = new byte[32];
        encode56(j79 | (j81 << 28), bArr2, 0);
        encode56(j83 | (j85 << 28), bArr2, 7);
        encode56(j87 | (j89 << 28), bArr2, 14);
        encode56(j91 | (j93 << 28), bArr2, 21);
        encode32((int) j92, bArr2, 28);
        return bArr2;
    }

    private static void scalarMult(byte[] bArr, PointAffine pointAffine, PointAccum pointAccum) {
        precompute();
        int[] iArr = new int[8];
        decodeScalar(bArr, 0, iArr);
        Nat.shiftDownBits(8, iArr, 3, 1);
        Nat.cadd(8, (iArr[0] ^ (-1)) & 1, iArr, L, iArr);
        Nat.shiftDownBit(8, iArr, 0);
        pointCopy(pointAffine, pointAccum);
        int[] iArrPointPrecomp = pointPrecomp(pointAffine, 8);
        PointExt pointExt = new PointExt();
        pointLookup(iArrPointPrecomp, 7, pointExt);
        pointAdd(pointExt, pointAccum);
        int i = 62;
        while (true) {
            pointLookup(iArr, i, iArrPointPrecomp, pointExt);
            pointAdd(pointExt, pointAccum);
            pointDouble(pointAccum);
            pointDouble(pointAccum);
            pointDouble(pointAccum);
            i--;
            if (i < 0) {
                return;
            } else {
                pointDouble(pointAccum);
            }
        }
    }

    private static void scalarMultBase(byte[] bArr, PointAccum pointAccum) {
        precompute();
        pointSetNeutral(pointAccum);
        int[] iArr = new int[8];
        decodeScalar(bArr, 0, iArr);
        Nat.cadd(8, (iArr[0] ^ (-1)) & 1, iArr, L, iArr);
        Nat.shiftDownBit(8, iArr, 1);
        for (int i = 0; i < 8; i++) {
            iArr[i] = Interleave.shuffle2(iArr[i]);
        }
        PointPrecomp pointPrecomp = new PointPrecomp();
        int i2 = 28;
        while (true) {
            for (int i3 = 0; i3 < 8; i3++) {
                int i4 = iArr[i3] >>> i2;
                int i5 = (i4 >>> 3) & 1;
                pointLookup(i3, (i4 ^ (-i5)) & 7, pointPrecomp);
                X25519Field.cswap(i5, pointPrecomp.ypx_h, pointPrecomp.ymx_h);
                X25519Field.cnegate(i5, pointPrecomp.xyd);
                pointAddPrecomp(pointPrecomp, pointAccum);
            }
            i2 -= 4;
            if (i2 < 0) {
                return;
            } else {
                pointDouble(pointAccum);
            }
        }
    }

    private static void scalarMultBaseEncoded(byte[] bArr, byte[] bArr2, int i) {
        PointAccum pointAccum = new PointAccum();
        scalarMultBase(bArr, pointAccum);
        if (0 == encodePoint(pointAccum, bArr2, i)) {
            throw new IllegalStateException();
        }
    }

    public static void scalarMultBaseYZ(X25519.Friend friend, byte[] bArr, int i, int[] iArr, int[] iArr2) {
        if (null == friend) {
            throw new NullPointerException("This method is only for use by X25519");
        }
        byte[] bArr2 = new byte[32];
        pruneScalar(bArr, i, bArr2);
        PointAccum pointAccum = new PointAccum();
        scalarMultBase(bArr2, pointAccum);
        if (0 == checkPoint(pointAccum.x, pointAccum.y, pointAccum.z)) {
            throw new IllegalStateException();
        }
        X25519Field.copy(pointAccum.y, 0, iArr, 0);
        X25519Field.copy(pointAccum.z, 0, iArr2, 0);
    }

    private static void scalarMultStrausVar(int[] iArr, int[] iArr2, PointAffine pointAffine, PointAccum pointAccum) {
        precompute();
        byte[] wnaf = getWNAF(iArr, 7);
        byte[] wnaf2 = getWNAF(iArr2, 5);
        PointExt[] pointExtArrPointPrecompVar = pointPrecompVar(pointCopy(pointAffine), 8);
        pointSetNeutral(pointAccum);
        int i = 252;
        while (true) {
            byte b = wnaf[i];
            if (b != 0) {
                int i2 = b >> 31;
                pointAddVar(i2 != 0, precompBaseTable[(b ^ i2) >>> 1], pointAccum);
            }
            byte b2 = wnaf2[i];
            if (b2 != 0) {
                int i3 = b2 >> 31;
                pointAddVar(i3 != 0, pointExtArrPointPrecompVar[(b2 ^ i3) >>> 1], pointAccum);
            }
            i--;
            if (i < 0) {
                return;
            } else {
                pointDouble(pointAccum);
            }
        }
    }

    public static void sign(byte[] bArr, int i, byte[] bArr2, int i2, int i3, byte[] bArr3, int i4) {
        implSign(bArr, i, null, (byte) 0, bArr2, i2, i3, bArr3, i4);
    }

    public static void sign(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, int i3, int i4, byte[] bArr4, int i5) {
        implSign(bArr, i, bArr2, i2, null, (byte) 0, bArr3, i3, i4, bArr4, i5);
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

    public static void signPrehash(byte[] bArr, int i, byte[] bArr2, Digest digest, byte[] bArr3, int i2) {
        byte[] bArr4 = new byte[64];
        if (64 != digest.doFinal(bArr4, 0)) {
            throw new IllegalArgumentException("ph");
        }
        implSign(bArr, i, bArr2, (byte) 1, bArr4, 0, bArr4.length, bArr3, i2);
    }

    public static void signPrehash(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, Digest digest, byte[] bArr4, int i3) {
        byte[] bArr5 = new byte[64];
        if (64 != digest.doFinal(bArr5, 0)) {
            throw new IllegalArgumentException("ph");
        }
        implSign(bArr, i, bArr2, i2, bArr3, (byte) 1, bArr5, 0, bArr5.length, bArr4, i3);
    }

    public static boolean verify(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, int i3, int i4) {
        return implVerify(bArr, i, bArr2, i2, null, (byte) 0, bArr3, i3, i4);
    }

    public static boolean verify(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, byte[] bArr4, int i3, int i4) {
        return implVerify(bArr, i, bArr2, i2, bArr3, (byte) 0, bArr4, i3, i4);
    }

    public static boolean verifyPrehash(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, byte[] bArr4, int i3) {
        return implVerify(bArr, i, bArr2, i2, bArr3, (byte) 1, bArr4, i3, 64);
    }

    public static boolean verifyPrehash(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, Digest digest) {
        byte[] bArr4 = new byte[64];
        if (64 != digest.doFinal(bArr4, 0)) {
            throw new IllegalArgumentException("ph");
        }
        return implVerify(bArr, i, bArr2, i2, bArr3, (byte) 1, bArr4, 0, bArr4.length);
    }
}
