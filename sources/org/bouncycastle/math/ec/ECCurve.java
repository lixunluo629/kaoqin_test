package org.bouncycastle.math.ec;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.endo.ECEndomorphism;
import org.bouncycastle.math.field.FiniteField;
import org.bouncycastle.math.field.FiniteFields;
import org.bouncycastle.math.raw.Nat;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECCurve.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/ECCurve.class */
public abstract class ECCurve {
    ECFieldElement a;
    ECFieldElement b;

    /* renamed from: org.bouncycastle.math.ec.ECCurve$1, reason: invalid class name */
    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECCurve$1.class */
    class AnonymousClass1 extends AbstractECLookupTable {
        final /* synthetic */ int val$len;
        final /* synthetic */ int val$FE_BYTES;
        final /* synthetic */ byte[] val$table;

        AnonymousClass1(int i, int i2, byte[] bArr) {
            this.val$len = i;
            this.val$FE_BYTES = i2;
            this.val$table = bArr;
        }

        @Override // org.bouncycastle.math.ec.ECLookupTable
        public int getSize() {
            return this.val$len;
        }

        @Override // org.bouncycastle.math.ec.ECLookupTable
        public ECPoint lookup(int i) {
            byte[] bArr = new byte[this.val$FE_BYTES];
            byte[] bArr2 = new byte[this.val$FE_BYTES];
            int i2 = 0;
            for (int i3 = 0; i3 < this.val$len; i3++) {
                int i4 = ((i3 ^ i) - 1) >> 31;
                for (int i5 = 0; i5 < this.val$FE_BYTES; i5++) {
                    int i6 = i5;
                    bArr[i6] = (byte) (bArr[i6] ^ (this.val$table[i2 + i5] & i4));
                    int i7 = i5;
                    bArr2[i7] = (byte) (bArr2[i7] ^ (this.val$table[(i2 + this.val$FE_BYTES) + i5] & i4));
                }
                i2 += this.val$FE_BYTES * 2;
            }
            return createPoint(bArr, bArr2);
        }

        @Override // org.bouncycastle.math.ec.AbstractECLookupTable, org.bouncycastle.math.ec.ECLookupTable
        public ECPoint lookupVar(int i) {
            byte[] bArr = new byte[this.val$FE_BYTES];
            byte[] bArr2 = new byte[this.val$FE_BYTES];
            int i2 = i * this.val$FE_BYTES * 2;
            for (int i3 = 0; i3 < this.val$FE_BYTES; i3++) {
                bArr[i3] = this.val$table[i2 + i3];
                bArr2[i3] = this.val$table[i2 + this.val$FE_BYTES + i3];
            }
            return createPoint(bArr, bArr2);
        }

        private ECPoint createPoint(byte[] bArr, byte[] bArr2) {
            return ECCurve.this.createRawPoint(ECCurve.this.fromBigInteger(new BigInteger(1, bArr)), ECCurve.this.fromBigInteger(new BigInteger(1, bArr2)));
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECCurve$AbstractF2m.class */
    public static abstract class AbstractF2m extends ECCurve {
        private BigInteger[] si;

        public static BigInteger inverse(int i, int[] iArr, BigInteger bigInteger) {
            return new LongArray(bigInteger).modInverse(i, iArr).toBigInteger();
        }

        private static FiniteField buildField(int i, int i2, int i3, int i4) {
            if (i2 == 0) {
                throw new IllegalArgumentException("k1 must be > 0");
            }
            if (i3 == 0) {
                if (i4 != 0) {
                    throw new IllegalArgumentException("k3 must be 0 if k2 == 0");
                }
                return FiniteFields.getBinaryExtensionField(new int[]{0, i2, i});
            }
            if (i3 <= i2) {
                throw new IllegalArgumentException("k2 must be > k1");
            }
            if (i4 <= i3) {
                throw new IllegalArgumentException("k3 must be > k2");
            }
            return FiniteFields.getBinaryExtensionField(new int[]{0, i2, i3, i4, i});
        }

        protected AbstractF2m(int i, int i2, int i3, int i4) {
            super(buildField(i, i2, i3, i4));
            this.si = null;
        }

        public boolean isValidFieldElement(BigInteger bigInteger) {
            return bigInteger != null && bigInteger.signum() >= 0 && bigInteger.bitLength() <= getFieldSize();
        }

        public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2) {
            ECFieldElement eCFieldElementFromBigInteger = fromBigInteger(bigInteger);
            ECFieldElement eCFieldElementFromBigInteger2 = fromBigInteger(bigInteger2);
            switch (getCoordinateSystem()) {
                case 5:
                case 6:
                    if (!eCFieldElementFromBigInteger.isZero()) {
                        eCFieldElementFromBigInteger2 = eCFieldElementFromBigInteger2.divide(eCFieldElementFromBigInteger).add(eCFieldElementFromBigInteger);
                        break;
                    } else if (!eCFieldElementFromBigInteger2.square().equals(getB())) {
                        throw new IllegalArgumentException();
                    }
                    break;
            }
            return createRawPoint(eCFieldElementFromBigInteger, eCFieldElementFromBigInteger2);
        }

        protected ECPoint decompressPoint(int i, BigInteger bigInteger) {
            ECFieldElement eCFieldElementFromBigInteger = fromBigInteger(bigInteger);
            ECFieldElement eCFieldElementMultiply = null;
            if (!eCFieldElementFromBigInteger.isZero()) {
                ECFieldElement eCFieldElementSolveQuadraticEquation = solveQuadraticEquation(eCFieldElementFromBigInteger.square().invert().multiply(getB()).add(getA()).add(eCFieldElementFromBigInteger));
                if (eCFieldElementSolveQuadraticEquation != null) {
                    if (eCFieldElementSolveQuadraticEquation.testBitZero() != (i == 1)) {
                        eCFieldElementSolveQuadraticEquation = eCFieldElementSolveQuadraticEquation.addOne();
                    }
                    switch (getCoordinateSystem()) {
                        case 5:
                        case 6:
                            eCFieldElementMultiply = eCFieldElementSolveQuadraticEquation.add(eCFieldElementFromBigInteger);
                            break;
                        default:
                            eCFieldElementMultiply = eCFieldElementSolveQuadraticEquation.multiply(eCFieldElementFromBigInteger);
                            break;
                    }
                }
            } else {
                eCFieldElementMultiply = getB().sqrt();
            }
            if (eCFieldElementMultiply == null) {
                throw new IllegalArgumentException("Invalid point compression");
            }
            return createRawPoint(eCFieldElementFromBigInteger, eCFieldElementMultiply);
        }

        protected ECFieldElement solveQuadraticEquation(ECFieldElement eCFieldElement) {
            ECFieldElement eCFieldElementAdd;
            ECFieldElement.AbstractF2m abstractF2m = (ECFieldElement.AbstractF2m) eCFieldElement;
            boolean zHasFastTrace = abstractF2m.hasFastTrace();
            if (zHasFastTrace && 0 != abstractF2m.trace()) {
                return null;
            }
            int fieldSize = getFieldSize();
            if (0 != (fieldSize & 1)) {
                ECFieldElement eCFieldElementHalfTrace = abstractF2m.halfTrace();
                if (zHasFastTrace || eCFieldElementHalfTrace.square().add(eCFieldElementHalfTrace).add(eCFieldElement).isZero()) {
                    return eCFieldElementHalfTrace;
                }
                return null;
            }
            if (eCFieldElement.isZero()) {
                return eCFieldElement;
            }
            ECFieldElement eCFieldElementFromBigInteger = fromBigInteger(ECConstants.ZERO);
            Random random = new Random();
            do {
                ECFieldElement eCFieldElementFromBigInteger2 = fromBigInteger(new BigInteger(fieldSize, random));
                eCFieldElementAdd = eCFieldElementFromBigInteger;
                ECFieldElement eCFieldElementAdd2 = eCFieldElement;
                for (int i = 1; i < fieldSize; i++) {
                    ECFieldElement eCFieldElementSquare = eCFieldElementAdd2.square();
                    eCFieldElementAdd = eCFieldElementAdd.square().add(eCFieldElementSquare.multiply(eCFieldElementFromBigInteger2));
                    eCFieldElementAdd2 = eCFieldElementSquare.add(eCFieldElement);
                }
                if (!eCFieldElementAdd2.isZero()) {
                    return null;
                }
            } while (eCFieldElementAdd.square().add(eCFieldElementAdd).isZero());
            return eCFieldElementAdd;
        }

        synchronized BigInteger[] getSi() {
            if (this.si == null) {
                this.si = Tnaf.getSi(this);
            }
            return this.si;
        }

        public boolean isKoblitz() {
            return this.order != null && this.cofactor != null && this.b.isOne() && (this.a.isZero() || this.a.isOne());
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECCurve$AbstractFp.class */
    public static abstract class AbstractFp extends ECCurve {
        protected AbstractFp(BigInteger bigInteger) {
            super(FiniteFields.getPrimeField(bigInteger));
        }

        public boolean isValidFieldElement(BigInteger bigInteger) {
            return bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(getField().getCharacteristic()) < 0;
        }

        protected ECPoint decompressPoint(int i, BigInteger bigInteger) {
            ECFieldElement eCFieldElementFromBigInteger = fromBigInteger(bigInteger);
            ECFieldElement eCFieldElementSqrt = eCFieldElementFromBigInteger.square().add(this.a).multiply(eCFieldElementFromBigInteger).add(this.b).sqrt();
            if (eCFieldElementSqrt == null) {
                throw new IllegalArgumentException("Invalid point compression");
            }
            if (eCFieldElementSqrt.testBitZero() != (i == 1)) {
                eCFieldElementSqrt = eCFieldElementSqrt.negate();
            }
            return createRawPoint(eCFieldElementFromBigInteger, eCFieldElementSqrt);
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECCurve$Config.class */
    public class Config {
        protected int coord;
        protected ECEndomorphism endomorphism;
        protected ECMultiplier multiplier;

        Config(int i, ECEndomorphism eCEndomorphism, ECMultiplier eCMultiplier) {
            this.coord = i;
            this.endomorphism = eCEndomorphism;
            this.multiplier = eCMultiplier;
        }

        public Config setCoordinateSystem(int i) {
            this.coord = i;
            return this;
        }

        public Config setEndomorphism(ECEndomorphism eCEndomorphism) {
            this.endomorphism = eCEndomorphism;
            return this;
        }

        public Config setMultiplier(ECMultiplier eCMultiplier) {
            this.multiplier = eCMultiplier;
            return this;
        }

        public ECCurve create() {
            if (!ECCurve.this.supportsCoordinateSystem(this.coord)) {
                throw new IllegalStateException("unsupported coordinate system");
            }
            ECCurve eCCurveCloneCurve = ECCurve.this.cloneCurve();
            if (eCCurveCloneCurve == ECCurve.this) {
                throw new IllegalStateException("implementation returned current curve");
            }
            synchronized (eCCurveCloneCurve) {
                eCCurveCloneCurve.coord = this.coord;
                eCCurveCloneCurve.endomorphism = this.endomorphism;
                eCCurveCloneCurve.multiplier = this.multiplier;
            }
            return eCCurveCloneCurve;
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECCurve$F2m.class
 */
    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/ECCurve$F2m.class */
    public static class F2m extends ECCurve {
        private int m;
        private int k1;
        private int k2;
        private int k3;
        private BigInteger n;
        private BigInteger h;
        private ECPoint.F2m infinity;
        private byte mu;
        private BigInteger[] si;

        /* renamed from: org.bouncycastle.math.ec.ECCurve$F2m$1, reason: invalid class name */
        /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECCurve$F2m$1.class */
        class AnonymousClass1 extends AbstractECLookupTable {
            final /* synthetic */ int val$len;
            final /* synthetic */ int val$FE_LONGS;
            final /* synthetic */ long[] val$table;
            final /* synthetic */ int[] val$ks;

            AnonymousClass1(int i, int i2, long[] jArr, int[] iArr) {
                this.val$len = i;
                this.val$FE_LONGS = i2;
                this.val$table = jArr;
                this.val$ks = iArr;
            }

            @Override // org.bouncycastle.math.ec.ECLookupTable
            public int getSize() {
                return this.val$len;
            }

            @Override // org.bouncycastle.math.ec.ECLookupTable
            public ECPoint lookup(int i) {
                long[] jArrCreate64 = Nat.create64(this.val$FE_LONGS);
                long[] jArrCreate642 = Nat.create64(this.val$FE_LONGS);
                int i2 = 0;
                for (int i3 = 0; i3 < this.val$len; i3++) {
                    long j = ((i3 ^ i) - 1) >> 31;
                    for (int i4 = 0; i4 < this.val$FE_LONGS; i4++) {
                        int i5 = i4;
                        jArrCreate64[i5] = jArrCreate64[i5] ^ (this.val$table[i2 + i4] & j);
                        int i6 = i4;
                        jArrCreate642[i6] = jArrCreate642[i6] ^ (this.val$table[(i2 + this.val$FE_LONGS) + i4] & j);
                    }
                    i2 += this.val$FE_LONGS * 2;
                }
                return createPoint(jArrCreate64, jArrCreate642);
            }

            @Override // org.bouncycastle.math.ec.AbstractECLookupTable, org.bouncycastle.math.ec.ECLookupTable
            public ECPoint lookupVar(int i) {
                long[] jArrCreate64 = Nat.create64(this.val$FE_LONGS);
                long[] jArrCreate642 = Nat.create64(this.val$FE_LONGS);
                int i2 = i * this.val$FE_LONGS * 2;
                for (int i3 = 0; i3 < this.val$FE_LONGS; i3++) {
                    jArrCreate64[i3] = this.val$table[i2 + i3];
                    jArrCreate642[i3] = this.val$table[i2 + this.val$FE_LONGS + i3];
                }
                return createPoint(jArrCreate64, jArrCreate642);
            }

            private ECPoint createPoint(long[] jArr, long[] jArr2) {
                return F2m.this.createRawPoint(new ECFieldElement.F2m(F2m.access$000(F2m.this), this.val$ks, new LongArray(jArr)), new ECFieldElement.F2m(F2m.access$000(F2m.this), this.val$ks, new LongArray(jArr2)));
            }
        }

        public F2m(int i, int i2, BigInteger bigInteger, BigInteger bigInteger2) {
            this(i, i2, 0, 0, bigInteger, bigInteger2, null, null);
        }

        public F2m(int i, int i2, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
            this(i, i2, 0, 0, bigInteger, bigInteger2, bigInteger3, bigInteger4);
        }

        public F2m(int i, int i2, int i3, int i4, BigInteger bigInteger, BigInteger bigInteger2) {
            this(i, i2, i3, i4, bigInteger, bigInteger2, null, null);
        }

        public F2m(int i, int i2, int i3, int i4, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
            this.mu = (byte) 0;
            this.si = null;
            this.m = i;
            this.k1 = i2;
            this.k2 = i3;
            this.k3 = i4;
            this.n = bigInteger3;
            this.h = bigInteger4;
            if (i2 == 0) {
                throw new IllegalArgumentException("k1 must be > 0");
            }
            if (i3 == 0) {
                if (i4 != 0) {
                    throw new IllegalArgumentException("k3 must be 0 if k2 == 0");
                }
            } else {
                if (i3 <= i2) {
                    throw new IllegalArgumentException("k2 must be > k1");
                }
                if (i4 <= i3) {
                    throw new IllegalArgumentException("k3 must be > k2");
                }
            }
            this.a = fromBigInteger(bigInteger);
            this.b = fromBigInteger(bigInteger2);
            this.infinity = new ECPoint.F2m(this, null, null);
        }

        @Override // org.bouncycastle.math.ec.ECCurve
        public int getFieldSize() {
            return this.m;
        }

        @Override // org.bouncycastle.math.ec.ECCurve
        public ECFieldElement fromBigInteger(BigInteger bigInteger) {
            return new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, bigInteger);
        }

        @Override // org.bouncycastle.math.ec.ECCurve
        public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2, boolean z) {
            return new ECPoint.F2m(this, fromBigInteger(bigInteger), fromBigInteger(bigInteger2), z);
        }

        @Override // org.bouncycastle.math.ec.ECCurve
        public ECPoint decodePoint(byte[] bArr) {
            ECPoint f2m;
            switch (bArr[0]) {
                case 0:
                    if (bArr.length <= 1) {
                        f2m = getInfinity();
                        break;
                    } else {
                        throw new RuntimeException("Invalid point encoding");
                    }
                case 1:
                case 5:
                default:
                    throw new RuntimeException("Invalid point encoding 0x" + Integer.toString(bArr[0], 16));
                case 2:
                case 3:
                    byte[] bArr2 = new byte[bArr.length - 1];
                    System.arraycopy(bArr, 1, bArr2, 0, bArr2.length);
                    if (bArr[0] != 2) {
                        f2m = decompressPoint(bArr2, 1);
                        break;
                    } else {
                        f2m = decompressPoint(bArr2, 0);
                        break;
                    }
                case 4:
                case 6:
                case 7:
                    byte[] bArr3 = new byte[(bArr.length - 1) / 2];
                    byte[] bArr4 = new byte[(bArr.length - 1) / 2];
                    System.arraycopy(bArr, 1, bArr3, 0, bArr3.length);
                    System.arraycopy(bArr, bArr3.length + 1, bArr4, 0, bArr4.length);
                    f2m = new ECPoint.F2m(this, new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(1, bArr3)), new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(1, bArr4)), false);
                    break;
            }
            return f2m;
        }

        @Override // org.bouncycastle.math.ec.ECCurve
        public ECPoint getInfinity() {
            return this.infinity;
        }

        public boolean isKoblitz() {
            return (this.n == null || this.h == null || (!this.a.toBigInteger().equals(ECConstants.ZERO) && !this.a.toBigInteger().equals(ECConstants.ONE)) || !this.b.toBigInteger().equals(ECConstants.ONE)) ? false : true;
        }

        synchronized byte getMu() {
            if (this.mu == 0) {
                this.mu = Tnaf.getMu(this);
            }
            return this.mu;
        }

        synchronized BigInteger[] getSi() {
            if (this.si == null) {
                this.si = Tnaf.getSi(this);
            }
            return this.si;
        }

        private ECPoint decompressPoint(byte[] bArr, int i) {
            ECFieldElement eCFieldElementMultiply;
            ECFieldElement.F2m f2m = new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(1, bArr));
            if (f2m.toBigInteger().equals(ECConstants.ZERO)) {
                eCFieldElementMultiply = (ECFieldElement.F2m) this.b;
                for (int i2 = 0; i2 < this.m - 1; i2++) {
                    eCFieldElementMultiply = eCFieldElementMultiply.square();
                }
            } else {
                ECFieldElement eCFieldElementSolveQuadradicEquation = solveQuadradicEquation(f2m.add(this.a).add(this.b.multiply(f2m.square().invert())));
                if (eCFieldElementSolveQuadradicEquation == null) {
                    throw new RuntimeException("Invalid point compression");
                }
                if ((eCFieldElementSolveQuadradicEquation.toBigInteger().testBit(0) ? 1 : 0) != i) {
                    eCFieldElementSolveQuadradicEquation = eCFieldElementSolveQuadradicEquation.add(new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, ECConstants.ONE));
                }
                eCFieldElementMultiply = f2m.multiply(eCFieldElementSolveQuadradicEquation);
            }
            return new ECPoint.F2m(this, f2m, eCFieldElementMultiply);
        }

        private ECFieldElement solveQuadradicEquation(ECFieldElement eCFieldElement) {
            ECFieldElement eCFieldElementAdd;
            ECFieldElement.F2m f2m = new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, ECConstants.ZERO);
            if (eCFieldElement.toBigInteger().equals(ECConstants.ZERO)) {
                return f2m;
            }
            Random random = new Random();
            do {
                ECFieldElement.F2m f2m2 = new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(this.m, random));
                eCFieldElementAdd = f2m;
                ECFieldElement eCFieldElementAdd2 = eCFieldElement;
                for (int i = 1; i <= this.m - 1; i++) {
                    ECFieldElement eCFieldElementSquare = eCFieldElementAdd2.square();
                    eCFieldElementAdd = eCFieldElementAdd.square().add(eCFieldElementSquare.multiply(f2m2));
                    eCFieldElementAdd2 = eCFieldElementSquare.add(eCFieldElement);
                }
                if (!eCFieldElementAdd2.toBigInteger().equals(ECConstants.ZERO)) {
                    return null;
                }
            } while (eCFieldElementAdd.square().add(eCFieldElementAdd).toBigInteger().equals(ECConstants.ZERO));
            return eCFieldElementAdd;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof F2m)) {
                return false;
            }
            F2m f2m = (F2m) obj;
            return this.m == f2m.m && this.k1 == f2m.k1 && this.k2 == f2m.k2 && this.k3 == f2m.k3 && this.a.equals(f2m.a) && this.b.equals(f2m.b);
        }

        public int hashCode() {
            return ((((this.a.hashCode() ^ this.b.hashCode()) ^ this.m) ^ this.k1) ^ this.k2) ^ this.k3;
        }

        public int getM() {
            return this.m;
        }

        public boolean isTrinomial() {
            return this.k2 == 0 && this.k3 == 0;
        }

        public int getK1() {
            return this.k1;
        }

        public int getK2() {
            return this.k2;
        }

        public int getK3() {
            return this.k3;
        }

        public BigInteger getN() {
            return this.n;
        }

        public BigInteger getH() {
            return this.h;
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECCurve$Fp.class
 */
    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/ECCurve$Fp.class */
    public static class Fp extends ECCurve {
        BigInteger q;
        ECPoint.Fp infinity;

        public Fp(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            this.q = bigInteger;
            this.a = fromBigInteger(bigInteger2);
            this.b = fromBigInteger(bigInteger3);
            this.infinity = new ECPoint.Fp(this, null, null);
        }

        public BigInteger getQ() {
            return this.q;
        }

        @Override // org.bouncycastle.math.ec.ECCurve
        public int getFieldSize() {
            return this.q.bitLength();
        }

        @Override // org.bouncycastle.math.ec.ECCurve
        public ECFieldElement fromBigInteger(BigInteger bigInteger) {
            return new ECFieldElement.Fp(this.q, bigInteger);
        }

        @Override // org.bouncycastle.math.ec.ECCurve
        public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2, boolean z) {
            return new ECPoint.Fp(this, fromBigInteger(bigInteger), fromBigInteger(bigInteger2), z);
        }

        @Override // org.bouncycastle.math.ec.ECCurve
        public ECPoint decodePoint(byte[] bArr) {
            ECPoint fp;
            switch (bArr[0]) {
                case 0:
                    if (bArr.length <= 1) {
                        fp = getInfinity();
                        break;
                    } else {
                        throw new RuntimeException("Invalid point encoding");
                    }
                case 1:
                case 5:
                default:
                    throw new RuntimeException("Invalid point encoding 0x" + Integer.toString(bArr[0], 16));
                case 2:
                case 3:
                    int i = bArr[0] & 1;
                    byte[] bArr2 = new byte[bArr.length - 1];
                    System.arraycopy(bArr, 1, bArr2, 0, bArr2.length);
                    ECFieldElement.Fp fp2 = new ECFieldElement.Fp(this.q, new BigInteger(1, bArr2));
                    ECFieldElement eCFieldElementSqrt = fp2.multiply(fp2.square().add(this.a)).add(this.b).sqrt();
                    if (eCFieldElementSqrt != null) {
                        if ((eCFieldElementSqrt.toBigInteger().testBit(0) ? 1 : 0) != i) {
                            fp = new ECPoint.Fp(this, fp2, new ECFieldElement.Fp(this.q, this.q.subtract(eCFieldElementSqrt.toBigInteger())), true);
                            break;
                        } else {
                            fp = new ECPoint.Fp(this, fp2, eCFieldElementSqrt, true);
                            break;
                        }
                    } else {
                        throw new RuntimeException("Invalid point compression");
                    }
                case 4:
                case 6:
                case 7:
                    byte[] bArr3 = new byte[(bArr.length - 1) / 2];
                    byte[] bArr4 = new byte[(bArr.length - 1) / 2];
                    System.arraycopy(bArr, 1, bArr3, 0, bArr3.length);
                    System.arraycopy(bArr, bArr3.length + 1, bArr4, 0, bArr4.length);
                    fp = new ECPoint.Fp(this, new ECFieldElement.Fp(this.q, new BigInteger(1, bArr3)), new ECFieldElement.Fp(this.q, new BigInteger(1, bArr4)));
                    break;
            }
            return fp;
        }

        @Override // org.bouncycastle.math.ec.ECCurve
        public ECPoint getInfinity() {
            return this.infinity;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Fp)) {
                return false;
            }
            Fp fp = (Fp) obj;
            return this.q.equals(fp.q) && this.a.equals(fp.a) && this.b.equals(fp.b);
        }

        public int hashCode() {
            return (this.a.hashCode() ^ this.b.hashCode()) ^ this.q.hashCode();
        }
    }

    public abstract int getFieldSize();

    public abstract ECFieldElement fromBigInteger(BigInteger bigInteger);

    public abstract ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2, boolean z);

    public abstract ECPoint decodePoint(byte[] bArr);

    public abstract ECPoint getInfinity();

    public ECFieldElement getA() {
        return this.a;
    }

    public ECFieldElement getB() {
        return this.b;
    }
}
