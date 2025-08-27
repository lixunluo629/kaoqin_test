package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.math.RoundingMode;
import org.apache.commons.compress.compressors.bzip2.BZip2Constants;
import org.apache.poi.ddf.EscherProperties;
import org.apache.xmlbeans.SchemaType;

@GwtCompatible(emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/math/IntMath.class */
public final class IntMath {

    @VisibleForTesting
    static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;

    @VisibleForTesting
    static final int FLOOR_SQRT_MAX_INT = 46340;

    @VisibleForTesting
    static final byte[] maxLog10ForLeadingZeros = {9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0, 0};

    @VisibleForTesting
    static final int[] powersOf10 = {1, 10, 100, 1000, 10000, BZip2Constants.BASEBLOCKSIZE, SchemaType.SIZE_BIG_INTEGER, 10000000, 100000000, 1000000000};

    @VisibleForTesting
    static final int[] halfPowersOf10 = {3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE};
    private static final int[] factorials = {1, 1, 2, 6, 24, 120, EscherProperties.THREEDSTYLE__SKEWANGLE, 5040, 40320, 362880, 3628800, 39916800, 479001600};

    @VisibleForTesting
    static int[] biggestBinomials = {Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, 477, 193, 110, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33};

    public static boolean isPowerOfTwo(int x) {
        return (x > 0) & ((x & (x - 1)) == 0);
    }

    @VisibleForTesting
    static int lessThanBranchFree(int x, int y) {
        return (((x - y) ^ (-1)) ^ (-1)) >>> 31;
    }

    /* renamed from: com.google.common.math.IntMath$1, reason: invalid class name */
    /* loaded from: guava-18.0.jar:com/google/common/math/IntMath$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];

        static {
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    public static int log2(int x, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return 32 - Integer.numberOfLeadingZeros(x - 1);
            case 6:
            case 7:
            case 8:
                int leadingZeros = Integer.numberOfLeadingZeros(x);
                int cmp = MAX_POWER_OF_SQRT2_UNSIGNED >>> leadingZeros;
                int logFloor = 31 - leadingZeros;
                return logFloor + lessThanBranchFree(cmp, x);
            default:
                throw new AssertionError();
        }
        return 31 - Integer.numberOfLeadingZeros(x);
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int log10(int x, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        int logFloor = log10Floor(x);
        int floorPow = powersOf10[logFloor];
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(x == floorPow);
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return logFloor + lessThanBranchFree(floorPow, x);
            case 6:
            case 7:
            case 8:
                return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
            default:
                throw new AssertionError();
        }
        return logFloor;
    }

    private static int log10Floor(int x) {
        byte b = maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(x)];
        return b - lessThanBranchFree(x, powersOf10[b]);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @com.google.common.annotations.GwtIncompatible("failing tests")
    public static int pow(int r4, int r5) {
        /*
            java.lang.String r0 = "exponent"
            r1 = r5
            int r0 = com.google.common.math.MathPreconditions.checkNonNegative(r0, r1)
            r0 = r4
            switch(r0) {
                case -2: goto L52;
                case -1: goto L38;
                case 0: goto L2c;
                case 1: goto L36;
                case 2: goto L44;
                default: goto L6b;
            }
        L2c:
            r0 = r5
            if (r0 != 0) goto L34
            r0 = 1
            goto L35
        L34:
            r0 = 0
        L35:
            return r0
        L36:
            r0 = 1
            return r0
        L38:
            r0 = r5
            r1 = 1
            r0 = r0 & r1
            if (r0 != 0) goto L42
            r0 = 1
            goto L43
        L42:
            r0 = -1
        L43:
            return r0
        L44:
            r0 = r5
            r1 = 32
            if (r0 >= r1) goto L50
            r0 = 1
            r1 = r5
            int r0 = r0 << r1
            goto L51
        L50:
            r0 = 0
        L51:
            return r0
        L52:
            r0 = r5
            r1 = 32
            if (r0 >= r1) goto L69
            r0 = r5
            r1 = 1
            r0 = r0 & r1
            if (r0 != 0) goto L64
            r0 = 1
            r1 = r5
            int r0 = r0 << r1
            goto L68
        L64:
            r0 = 1
            r1 = r5
            int r0 = r0 << r1
            int r0 = -r0
        L68:
            return r0
        L69:
            r0 = 0
            return r0
        L6b:
            r0 = 1
            r6 = r0
        L6d:
            r0 = r5
            switch(r0) {
                case 0: goto L88;
                case 1: goto L8a;
                default: goto L8e;
            }
        L88:
            r0 = r6
            return r0
        L8a:
            r0 = r4
            r1 = r6
            int r0 = r0 * r1
            return r0
        L8e:
            r0 = r6
            r1 = r5
            r2 = 1
            r1 = r1 & r2
            if (r1 != 0) goto L99
            r1 = 1
            goto L9a
        L99:
            r1 = r4
        L9a:
            int r0 = r0 * r1
            r6 = r0
            r0 = r4
            r1 = r4
            int r0 = r0 * r1
            r4 = r0
            r0 = r5
            r1 = 1
            int r0 = r0 >> r1
            r5 = r0
            goto L6d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.IntMath.pow(int, int):int");
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int sqrt(int x, RoundingMode mode) {
        MathPreconditions.checkNonNegative("x", x);
        int sqrtFloor = sqrtFloor(x);
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(sqrtFloor * sqrtFloor == x);
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, x);
            case 6:
            case 7:
            case 8:
                int halfSquare = (sqrtFloor * sqrtFloor) + sqrtFloor;
                return sqrtFloor + lessThanBranchFree(halfSquare, x);
            default:
                throw new AssertionError();
        }
        return sqrtFloor;
    }

    private static int sqrtFloor(int x) {
        return (int) Math.sqrt(x);
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0102  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int divide(int r4, int r5, java.math.RoundingMode r6) {
        /*
            Method dump skipped, instructions count: 260
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.IntMath.divide(int, int, java.math.RoundingMode):int");
    }

    public static int mod(int x, int m) {
        if (m <= 0) {
            throw new ArithmeticException(new StringBuilder(31).append("Modulus ").append(m).append(" must be > 0").toString());
        }
        int result = x % m;
        return result >= 0 ? result : result + m;
    }

    public static int gcd(int a, int b) {
        MathPreconditions.checkNonNegative("a", a);
        MathPreconditions.checkNonNegative("b", b);
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        int aTwos = Integer.numberOfTrailingZeros(a);
        int a2 = a >> aTwos;
        int bTwos = Integer.numberOfTrailingZeros(b);
        int b2 = b >> bTwos;
        while (a2 != b2) {
            int delta = a2 - b2;
            int minDeltaOrZero = delta & (delta >> 31);
            int a3 = (delta - minDeltaOrZero) - minDeltaOrZero;
            b2 += minDeltaOrZero;
            a2 = a3 >> Integer.numberOfTrailingZeros(a3);
        }
        return a2 << Math.min(aTwos, bTwos);
    }

    public static int checkedAdd(int a, int b) {
        long result = a + b;
        MathPreconditions.checkNoOverflow(result == ((long) ((int) result)));
        return (int) result;
    }

    public static int checkedSubtract(int a, int b) {
        long result = a - b;
        MathPreconditions.checkNoOverflow(result == ((long) ((int) result)));
        return (int) result;
    }

    public static int checkedMultiply(int a, int b) {
        long result = a * b;
        MathPreconditions.checkNoOverflow(result == ((long) ((int) result)));
        return (int) result;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public static int checkedPow(int r4, int r5) {
        /*
            java.lang.String r0 = "exponent"
            r1 = r5
            int r0 = com.google.common.math.MathPreconditions.checkNonNegative(r0, r1)
            r0 = r4
            switch(r0) {
                case -2: goto L56;
                case -1: goto L38;
                case 0: goto L2c;
                case 1: goto L36;
                case 2: goto L44;
                default: goto L74;
            }
        L2c:
            r0 = r5
            if (r0 != 0) goto L34
            r0 = 1
            goto L35
        L34:
            r0 = 0
        L35:
            return r0
        L36:
            r0 = 1
            return r0
        L38:
            r0 = r5
            r1 = 1
            r0 = r0 & r1
            if (r0 != 0) goto L42
            r0 = 1
            goto L43
        L42:
            r0 = -1
        L43:
            return r0
        L44:
            r0 = r5
            r1 = 31
            if (r0 >= r1) goto L4e
            r0 = 1
            goto L4f
        L4e:
            r0 = 0
        L4f:
            com.google.common.math.MathPreconditions.checkNoOverflow(r0)
            r0 = 1
            r1 = r5
            int r0 = r0 << r1
            return r0
        L56:
            r0 = r5
            r1 = 32
            if (r0 >= r1) goto L60
            r0 = 1
            goto L61
        L60:
            r0 = 0
        L61:
            com.google.common.math.MathPreconditions.checkNoOverflow(r0)
            r0 = r5
            r1 = 1
            r0 = r0 & r1
            if (r0 != 0) goto L70
            r0 = 1
            r1 = r5
            int r0 = r0 << r1
            goto L73
        L70:
            r0 = -1
            r1 = r5
            int r0 = r0 << r1
        L73:
            return r0
        L74:
            r0 = 1
            r6 = r0
        L76:
            r0 = r5
            switch(r0) {
                case 0: goto L90;
                case 1: goto L92;
                default: goto L98;
            }
        L90:
            r0 = r6
            return r0
        L92:
            r0 = r6
            r1 = r4
            int r0 = checkedMultiply(r0, r1)
            return r0
        L98:
            r0 = r5
            r1 = 1
            r0 = r0 & r1
            if (r0 == 0) goto La4
            r0 = r6
            r1 = r4
            int r0 = checkedMultiply(r0, r1)
            r6 = r0
        La4:
            r0 = r5
            r1 = 1
            int r0 = r0 >> r1
            r5 = r0
            r0 = r5
            if (r0 <= 0) goto L76
            r0 = -46340(0xffffffffffff4afc, float:NaN)
            r1 = r4
            if (r0 > r1) goto Lb6
            r0 = 1
            goto Lb7
        Lb6:
            r0 = 0
        Lb7:
            r1 = r4
            r2 = 46340(0xb504, float:6.4936E-41)
            if (r1 > r2) goto Lc1
            r1 = 1
            goto Lc2
        Lc1:
            r1 = 0
        Lc2:
            r0 = r0 & r1
            com.google.common.math.MathPreconditions.checkNoOverflow(r0)
            r0 = r4
            r1 = r4
            int r0 = r0 * r1
            r4 = r0
            goto L76
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.IntMath.checkedPow(int, int):int");
    }

    public static int factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        if (n < factorials.length) {
            return factorials[n];
        }
        return Integer.MAX_VALUE;
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int binomial(int n, int k) {
        MathPreconditions.checkNonNegative("n", n);
        MathPreconditions.checkNonNegative("k", k);
        Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", Integer.valueOf(k), Integer.valueOf(n));
        if (k > (n >> 1)) {
            k = n - k;
        }
        if (k >= biggestBinomials.length || n > biggestBinomials[k]) {
            return Integer.MAX_VALUE;
        }
        switch (k) {
            case 0:
                return 1;
            case 1:
                return n;
            default:
                long result = 1;
                for (int i = 0; i < k; i++) {
                    result = (result * (n - i)) / (i + 1);
                }
                return (int) result;
        }
    }

    public static int mean(int x, int y) {
        return (x & y) + ((x ^ y) >> 1);
    }

    private IntMath() {
    }
}
