package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;

@GwtCompatible(emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/math/DoubleMath.class */
public final class DoubleMath {
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9d;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9d;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18d;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18d;

    @VisibleForTesting
    static final int MAX_FACTORIAL = 170;
    private static final double LN_2 = Math.log(2.0d);

    @VisibleForTesting
    static final double[] everySixteenthFactorial = {1.0d, 2.0922789888E13d, 2.631308369336935E35d, 1.2413915592536073E61d, 1.2688693218588417E89d, 7.156945704626381E118d, 9.916779348709496E149d, 1.974506857221074E182d, 3.856204823625804E215d, 5.5502938327393044E249d, 4.7147236359920616E284d};

    @GwtIncompatible("#isMathematicalInteger, com.google.common.math.DoubleUtils")
    static double roundIntermediate(double x, RoundingMode mode) {
        if (!DoubleUtils.isFinite(x)) {
            throw new ArithmeticException("input is infinite or NaN");
        }
        switch (AnonymousClass1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(x));
                return x;
            case 2:
                if (x >= 0.0d || isMathematicalInteger(x)) {
                    return x;
                }
                return x - 1.0d;
            case 3:
                if (x <= 0.0d || isMathematicalInteger(x)) {
                    return x;
                }
                return x + 1.0d;
            case 4:
                return x;
            case 5:
                if (isMathematicalInteger(x)) {
                    return x;
                }
                return x + Math.copySign(1.0d, x);
            case 6:
                return Math.rint(x);
            case 7:
                double z = Math.rint(x);
                if (Math.abs(x - z) == 0.5d) {
                    return x + Math.copySign(0.5d, x);
                }
                return z;
            case 8:
                double z2 = Math.rint(x);
                if (Math.abs(x - z2) == 0.5d) {
                    return x;
                }
                return z2;
            default:
                throw new AssertionError();
        }
    }

    /* renamed from: com.google.common.math.DoubleMath$1, reason: invalid class name */
    /* loaded from: guava-18.0.jar:com/google/common/math/DoubleMath$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];

        static {
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    @GwtIncompatible("#roundIntermediate")
    public static int roundToInt(double x, RoundingMode mode) {
        double z = roundIntermediate(x, mode);
        MathPreconditions.checkInRange((z > -2.147483649E9d) & (z < 2.147483648E9d));
        return (int) z;
    }

    @GwtIncompatible("#roundIntermediate")
    public static long roundToLong(double x, RoundingMode mode) {
        double z = roundIntermediate(x, mode);
        MathPreconditions.checkInRange((MIN_LONG_AS_DOUBLE - z < 1.0d) & (z < MAX_LONG_AS_DOUBLE_PLUS_ONE));
        return (long) z;
    }

    @GwtIncompatible("#roundIntermediate, java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    public static BigInteger roundToBigInteger(double x, RoundingMode mode) {
        double x2 = roundIntermediate(x, mode);
        if ((MIN_LONG_AS_DOUBLE - x2 < 1.0d) & (x2 < MAX_LONG_AS_DOUBLE_PLUS_ONE)) {
            return BigInteger.valueOf((long) x2);
        }
        int exponent = Math.getExponent(x2);
        long significand = DoubleUtils.getSignificand(x2);
        BigInteger result = BigInteger.valueOf(significand).shiftLeft(exponent - 52);
        return x2 < 0.0d ? result.negate() : result;
    }

    @GwtIncompatible("com.google.common.math.DoubleUtils")
    public static boolean isPowerOfTwo(double x) {
        return x > 0.0d && DoubleUtils.isFinite(x) && LongMath.isPowerOfTwo(DoubleUtils.getSignificand(x));
    }

    public static double log2(double x) {
        return Math.log(x) / LN_2;
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00eb  */
    @com.google.common.annotations.GwtIncompatible("java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int log2(double r5, java.math.RoundingMode r7) {
        /*
            r0 = r5
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L11
            r0 = r5
            boolean r0 = com.google.common.math.DoubleUtils.isFinite(r0)
            if (r0 == 0) goto L11
            r0 = 1
            goto L12
        L11:
            r0 = 0
        L12:
            java.lang.String r1 = "x must be positive and finite"
            com.google.common.base.Preconditions.checkArgument(r0, r1)
            r0 = r5
            int r0 = java.lang.Math.getExponent(r0)
            r8 = r0
            r0 = r5
            boolean r0 = com.google.common.math.DoubleUtils.isNormal(r0)
            if (r0 != 0) goto L30
            r0 = r5
            r1 = 4841369599423283200(0x4330000000000000, double:4.503599627370496E15)
            double r0 = r0 * r1
            r1 = r7
            int r0 = log2(r0, r1)
            r1 = 52
            int r0 = r0 - r1
            return r0
        L30:
            int[] r0 = com.google.common.math.DoubleMath.AnonymousClass1.$SwitchMap$java$math$RoundingMode
            r1 = r7
            int r1 = r1.ordinal()
            r0 = r0[r1]
            switch(r0) {
                case 1: goto L68;
                case 2: goto L6f;
                case 3: goto L75;
                case 4: goto L86;
                case 5: goto La1;
                case 6: goto Lbc;
                case 7: goto Lbc;
                case 8: goto Lbc;
                default: goto Ld8;
            }
        L68:
            r0 = r5
            boolean r0 = isPowerOfTwo(r0)
            com.google.common.math.MathPreconditions.checkRoundingUnnecessary(r0)
        L6f:
            r0 = 0
            r9 = r0
            goto Le0
        L75:
            r0 = r5
            boolean r0 = isPowerOfTwo(r0)
            if (r0 != 0) goto L80
            r0 = 1
            goto L81
        L80:
            r0 = 0
        L81:
            r9 = r0
            goto Le0
        L86:
            r0 = r8
            if (r0 >= 0) goto L8e
            r0 = 1
            goto L8f
        L8e:
            r0 = 0
        L8f:
            r1 = r5
            boolean r1 = isPowerOfTwo(r1)
            if (r1 != 0) goto L9a
            r1 = 1
            goto L9b
        L9a:
            r1 = 0
        L9b:
            r0 = r0 & r1
            r9 = r0
            goto Le0
        La1:
            r0 = r8
            if (r0 < 0) goto La9
            r0 = 1
            goto Laa
        La9:
            r0 = 0
        Laa:
            r1 = r5
            boolean r1 = isPowerOfTwo(r1)
            if (r1 != 0) goto Lb5
            r1 = 1
            goto Lb6
        Lb5:
            r1 = 0
        Lb6:
            r0 = r0 & r1
            r9 = r0
            goto Le0
        Lbc:
            r0 = r5
            double r0 = com.google.common.math.DoubleUtils.scaleNormalize(r0)
            r10 = r0
            r0 = r10
            r1 = r10
            double r0 = r0 * r1
            r1 = 4611686018427387904(0x4000000000000000, double:2.0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto Ld2
            r0 = 1
            goto Ld3
        Ld2:
            r0 = 0
        Ld3:
            r9 = r0
            goto Le0
        Ld8:
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r1 = r0
            r1.<init>()
            throw r0
        Le0:
            r0 = r9
            if (r0 == 0) goto Leb
            r0 = r8
            r1 = 1
            int r0 = r0 + r1
            goto Lec
        Leb:
            r0 = r8
        Lec:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.DoubleMath.log2(double, java.math.RoundingMode):int");
    }

    @GwtIncompatible("java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    public static boolean isMathematicalInteger(double x) {
        return DoubleUtils.isFinite(x) && (x == 0.0d || 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x)) <= Math.getExponent(x));
    }

    public static double factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        if (n > 170) {
            return Double.POSITIVE_INFINITY;
        }
        double accum = 1.0d;
        for (int i = 1 + (n & (-16)); i <= n; i++) {
            accum *= i;
        }
        return accum * everySixteenthFactorial[n >> 4];
    }

    public static boolean fuzzyEquals(double a, double b, double tolerance) {
        MathPreconditions.checkNonNegative("tolerance", tolerance);
        return Math.copySign(a - b, 1.0d) <= tolerance || a == b || (Double.isNaN(a) && Double.isNaN(b));
    }

    public static int fuzzyCompare(double a, double b, double tolerance) {
        if (fuzzyEquals(a, b, tolerance)) {
            return 0;
        }
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        return Booleans.compare(Double.isNaN(a), Double.isNaN(b));
    }

    @GwtIncompatible("com.google.common.math.DoubleUtils")
    /* loaded from: guava-18.0.jar:com/google/common/math/DoubleMath$MeanAccumulator.class */
    private static final class MeanAccumulator {
        private long count;
        private double mean;

        private MeanAccumulator() {
            this.count = 0L;
            this.mean = 0.0d;
        }

        /* synthetic */ MeanAccumulator(AnonymousClass1 x0) {
            this();
        }

        void add(double value) {
            Preconditions.checkArgument(DoubleUtils.isFinite(value));
            this.count++;
            this.mean += (value - this.mean) / this.count;
        }

        double mean() {
            Preconditions.checkArgument(this.count > 0, "Cannot take mean of 0 values");
            return this.mean;
        }
    }

    @GwtIncompatible("MeanAccumulator")
    public static double mean(double... values) {
        MeanAccumulator accumulator = new MeanAccumulator(null);
        for (double value : values) {
            accumulator.add(value);
        }
        return accumulator.mean();
    }

    @GwtIncompatible("MeanAccumulator")
    public static double mean(int... values) {
        MeanAccumulator accumulator = new MeanAccumulator(null);
        for (int value : values) {
            accumulator.add(value);
        }
        return accumulator.mean();
    }

    @GwtIncompatible("MeanAccumulator")
    public static double mean(long... values) {
        MeanAccumulator accumulator = new MeanAccumulator(null);
        for (long value : values) {
            accumulator.add(value);
        }
        return accumulator.mean();
    }

    @GwtIncompatible("MeanAccumulator")
    public static double mean(Iterable<? extends Number> values) {
        MeanAccumulator accumulator = new MeanAccumulator(null);
        for (Number value : values) {
            accumulator.add(value.doubleValue());
        }
        return accumulator.mean();
    }

    @GwtIncompatible("MeanAccumulator")
    public static double mean(Iterator<? extends Number> values) {
        MeanAccumulator accumulator = new MeanAccumulator(null);
        while (values.hasNext()) {
            accumulator.add(values.next().doubleValue());
        }
        return accumulator.mean();
    }

    private DoubleMath() {
    }
}
