package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.mysql.jdbc.MysqlErrorNumbers;
import java.math.RoundingMode;
import org.apache.ibatis.javassist.compiler.TokenId;
import org.apache.tomcat.jni.Time;
import org.aspectj.apache.bcel.Constants;

@GwtCompatible(emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/math/LongMath.class */
public final class LongMath {

    @VisibleForTesting
    static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;

    @VisibleForTesting
    static final long FLOOR_SQRT_MAX_LONG = 3037000499L;

    @VisibleForTesting
    static final byte[] maxLog10ForLeadingZeros = {19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0};

    @VisibleForTesting
    @GwtIncompatible("TODO")
    static final long[] powersOf10 = {1, 10, 100, 1000, 10000, 100000, Time.APR_USEC_PER_SEC, 10000000, 100000000, 1000000000, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};

    @VisibleForTesting
    @GwtIncompatible("TODO")
    static final long[] halfPowersOf10 = {3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L};
    static final long[] factorials = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    static final int[] biggestBinomials = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, MysqlErrorNumbers.ER_PARTITION_EXCHANGE_TEMP_TABLE, 887, 534, TokenId.OR_E, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66};

    @VisibleForTesting
    static final int[] biggestSimpleBinomials = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, Constants.INVOKEVIRTUAL_QUICK, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61};

    public static boolean isPowerOfTwo(long x) {
        return (x > 0) & ((x & (x - 1)) == 0);
    }

    @VisibleForTesting
    static int lessThanBranchFree(long x, long y) {
        return (int) ((((x - y) ^ (-1)) ^ (-1)) >>> 63);
    }

    /* renamed from: com.google.common.math.LongMath$1, reason: invalid class name */
    /* loaded from: guava-18.0.jar:com/google/common/math/LongMath$1.class */
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

    public static int log2(long x, RoundingMode mode) {
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
                return 64 - Long.numberOfLeadingZeros(x - 1);
            case 6:
            case 7:
            case 8:
                int leadingZeros = Long.numberOfLeadingZeros(x);
                long cmp = MAX_POWER_OF_SQRT2_UNSIGNED >>> leadingZeros;
                int logFloor = 63 - leadingZeros;
                return logFloor + lessThanBranchFree(cmp, x);
            default:
                throw new AssertionError("impossible");
        }
        return 63 - Long.numberOfLeadingZeros(x);
    }

    @GwtIncompatible("TODO")
    public static int log10(long x, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        int logFloor = log10Floor(x);
        long floorPow = powersOf10[logFloor];
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

    @GwtIncompatible("TODO")
    static int log10Floor(long x) {
        byte b = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
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
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:95)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @com.google.common.annotations.GwtIncompatible("TODO")
    public static long pow(long r5, int r7) {
        /*
            java.lang.String r0 = "exponent"
            r1 = r7
            int r0 = com.google.common.math.MathPreconditions.checkNonNegative(r0, r1)
            r0 = -2
            r1 = r5
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L85
            r0 = r5
            r1 = 2
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L85
            r0 = r5
            int r0 = (int) r0
            switch(r0) {
                case -2: goto L64;
                case -1: goto L48;
                case 0: goto L3c;
                case 1: goto L46;
                case 2: goto L56;
                default: goto L7d;
            }
        L3c:
            r0 = r7
            if (r0 != 0) goto L44
            r0 = 1
            goto L45
        L44:
            r0 = 0
        L45:
            return r0
        L46:
            r0 = 1
            return r0
        L48:
            r0 = r7
            r1 = 1
            r0 = r0 & r1
            if (r0 != 0) goto L52
            r0 = 1
            goto L55
        L52:
            r0 = -1
        L55:
            return r0
        L56:
            r0 = r7
            r1 = 64
            if (r0 >= r1) goto L62
            r0 = 1
            r1 = r7
            long r0 = r0 << r1
            goto L63
        L62:
            r0 = 0
        L63:
            return r0
        L64:
            r0 = r7
            r1 = 64
            if (r0 >= r1) goto L7b
            r0 = r7
            r1 = 1
            r0 = r0 & r1
            if (r0 != 0) goto L76
            r0 = 1
            r1 = r7
            long r0 = r0 << r1
            goto L7a
        L76:
            r0 = 1
            r1 = r7
            long r0 = r0 << r1
            long r0 = -r0
        L7a:
            return r0
        L7b:
            r0 = 0
            return r0
        L7d:
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r1 = r0
            r1.<init>()
            throw r0
        L85:
            r0 = 1
            r8 = r0
        L87:
            r0 = r7
            switch(r0) {
                case 0: goto La4;
                case 1: goto La6;
                default: goto Laa;
            }
        La4:
            r0 = r8
            return r0
        La6:
            r0 = r8
            r1 = r5
            long r0 = r0 * r1
            return r0
        Laa:
            r0 = r8
            r1 = r7
            r2 = 1
            r1 = r1 & r2
            if (r1 != 0) goto Lb5
            r1 = 1
            goto Lb6
        Lb5:
            r1 = r5
        Lb6:
            long r0 = r0 * r1
            r8 = r0
            r0 = r5
            r1 = r5
            long r0 = r0 * r1
            r5 = r0
            r0 = r7
            r1 = 1
            int r0 = r0 >> r1
            r7 = r0
            goto L87
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.LongMath.pow(long, int):long");
    }

    @GwtIncompatible("TODO")
    public static long sqrt(long x, RoundingMode mode) {
        MathPreconditions.checkNonNegative("x", x);
        if (fitsInInt(x)) {
            return IntMath.sqrt((int) x, mode);
        }
        long guess = (long) Math.sqrt(x);
        long guessSquared = guess * guess;
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(guessSquared == x);
                return guess;
            case 2:
            case 3:
                if (x < guessSquared) {
                    return guess - 1;
                }
                return guess;
            case 4:
            case 5:
                if (x > guessSquared) {
                    return guess + 1;
                }
                return guess;
            case 6:
            case 7:
            case 8:
                long sqrtFloor = guess - (x < guessSquared ? 1 : 0);
                long halfSquare = (sqrtFloor * sqrtFloor) + sqrtFloor;
                return sqrtFloor + lessThanBranchFree(halfSquare, x);
            default:
                throw new AssertionError();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0109  */
    @com.google.common.annotations.GwtIncompatible("TODO")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long divide(long r7, long r9, java.math.RoundingMode r11) {
        /*
            Method dump skipped, instructions count: 268
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.LongMath.divide(long, long, java.math.RoundingMode):long");
    }

    @GwtIncompatible("TODO")
    public static int mod(long x, int m) {
        return (int) mod(x, m);
    }

    @GwtIncompatible("TODO")
    public static long mod(long x, long m) {
        if (m <= 0) {
            throw new ArithmeticException("Modulus must be positive");
        }
        long result = x % m;
        return result >= 0 ? result : result + m;
    }

    public static long gcd(long a, long b) {
        MathPreconditions.checkNonNegative("a", a);
        MathPreconditions.checkNonNegative("b", b);
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        int aTwos = Long.numberOfTrailingZeros(a);
        long a2 = a >> aTwos;
        int bTwos = Long.numberOfTrailingZeros(b);
        long b2 = b >> bTwos;
        while (a2 != b2) {
            long delta = a2 - b2;
            long minDeltaOrZero = delta & (delta >> 63);
            long a3 = (delta - minDeltaOrZero) - minDeltaOrZero;
            b2 += minDeltaOrZero;
            a2 = a3 >> Long.numberOfTrailingZeros(a3);
        }
        return a2 << Math.min(aTwos, bTwos);
    }

    @GwtIncompatible("TODO")
    public static long checkedAdd(long a, long b) {
        long result = a + b;
        MathPreconditions.checkNoOverflow(((a ^ b) < 0) | ((a ^ result) >= 0));
        return result;
    }

    @GwtIncompatible("TODO")
    public static long checkedSubtract(long a, long b) {
        long result = a - b;
        MathPreconditions.checkNoOverflow(((a ^ b) >= 0) | ((a ^ result) >= 0));
        return result;
    }

    @GwtIncompatible("TODO")
    public static long checkedMultiply(long a, long b) {
        int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ (-1)) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(b ^ (-1));
        if (leadingZeros > 65) {
            return a * b;
        }
        MathPreconditions.checkNoOverflow(leadingZeros >= 64);
        MathPreconditions.checkNoOverflow((a >= 0) | (b != Long.MIN_VALUE));
        long result = a * b;
        MathPreconditions.checkNoOverflow(a == 0 || result / a == b);
        return result;
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
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:101)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    @com.google.common.annotations.GwtIncompatible("TODO")
    public static long checkedPow(long r6, int r8) {
        /*
            Method dump skipped, instructions count: 235
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.LongMath.checkedPow(long, int):long");
    }

    @GwtIncompatible("TODO")
    public static long factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        if (n < factorials.length) {
            return factorials[n];
        }
        return Long.MAX_VALUE;
    }

    public static long binomial(int n, int k) {
        int i;
        MathPreconditions.checkNonNegative("n", n);
        MathPreconditions.checkNonNegative("k", k);
        Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", Integer.valueOf(k), Integer.valueOf(n));
        if (k > (n >> 1)) {
            k = n - k;
        }
        switch (k) {
            case 0:
                return 1L;
            case 1:
                return n;
            default:
                if (n < factorials.length) {
                    return factorials[n] / (factorials[k] * factorials[n - k]);
                }
                if (k >= biggestBinomials.length || n > biggestBinomials[k]) {
                    return Long.MAX_VALUE;
                }
                if (k < biggestSimpleBinomials.length && n <= biggestSimpleBinomials[k]) {
                    int n2 = n - 1;
                    long result = n;
                    for (int i2 = 2; i2 <= k; i2++) {
                        result = (result * n2) / i2;
                        n2--;
                    }
                    return result;
                }
                int nBits = log2(n, RoundingMode.CEILING);
                long result2 = 1;
                int n3 = n - 1;
                long numerator = n;
                long denominator = 1;
                int numeratorBits = nBits;
                int i3 = 2;
                while (i3 <= k) {
                    if (numeratorBits + nBits < 63) {
                        numerator *= n3;
                        denominator *= i3;
                        i = numeratorBits + nBits;
                    } else {
                        result2 = multiplyFraction(result2, numerator, denominator);
                        numerator = n3;
                        denominator = i3;
                        i = nBits;
                    }
                    numeratorBits = i;
                    i3++;
                    n3--;
                }
                return multiplyFraction(result2, numerator, denominator);
        }
    }

    static long multiplyFraction(long x, long numerator, long denominator) {
        if (x == 1) {
            return numerator / denominator;
        }
        long commonDivisor = gcd(x, denominator);
        return (x / commonDivisor) * (numerator / (denominator / commonDivisor));
    }

    static boolean fitsInInt(long x) {
        return ((long) ((int) x)) == x;
    }

    public static long mean(long x, long y) {
        return (x & y) + ((x ^ y) >> 1);
    }

    private LongMath() {
    }
}
