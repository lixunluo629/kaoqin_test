package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import java.math.BigInteger;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/math/MathPreconditions.class */
final class MathPreconditions {
    static int checkPositive(@Nullable String role, int x) {
        if (x <= 0) {
            String strValueOf = String.valueOf(String.valueOf(role));
            throw new IllegalArgumentException(new StringBuilder(26 + strValueOf.length()).append(strValueOf).append(" (").append(x).append(") must be > 0").toString());
        }
        return x;
    }

    static long checkPositive(@Nullable String role, long x) {
        if (x <= 0) {
            String strValueOf = String.valueOf(String.valueOf(role));
            throw new IllegalArgumentException(new StringBuilder(35 + strValueOf.length()).append(strValueOf).append(" (").append(x).append(") must be > 0").toString());
        }
        return x;
    }

    static BigInteger checkPositive(@Nullable String role, BigInteger x) {
        if (x.signum() <= 0) {
            String strValueOf = String.valueOf(String.valueOf(role));
            String strValueOf2 = String.valueOf(String.valueOf(x));
            throw new IllegalArgumentException(new StringBuilder(15 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(" (").append(strValueOf2).append(") must be > 0").toString());
        }
        return x;
    }

    static int checkNonNegative(@Nullable String role, int x) {
        if (x < 0) {
            String strValueOf = String.valueOf(String.valueOf(role));
            throw new IllegalArgumentException(new StringBuilder(27 + strValueOf.length()).append(strValueOf).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }

    static long checkNonNegative(@Nullable String role, long x) {
        if (x < 0) {
            String strValueOf = String.valueOf(String.valueOf(role));
            throw new IllegalArgumentException(new StringBuilder(36 + strValueOf.length()).append(strValueOf).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }

    static BigInteger checkNonNegative(@Nullable String role, BigInteger x) {
        if (x.signum() < 0) {
            String strValueOf = String.valueOf(String.valueOf(role));
            String strValueOf2 = String.valueOf(String.valueOf(x));
            throw new IllegalArgumentException(new StringBuilder(16 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(" (").append(strValueOf2).append(") must be >= 0").toString());
        }
        return x;
    }

    static double checkNonNegative(@Nullable String role, double x) {
        if (x < 0.0d) {
            String strValueOf = String.valueOf(String.valueOf(role));
            throw new IllegalArgumentException(new StringBuilder(40 + strValueOf.length()).append(strValueOf).append(" (").append(x).append(") must be >= 0").toString());
        }
        return x;
    }

    static void checkRoundingUnnecessary(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }

    static void checkInRange(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("not in range");
        }
    }

    static void checkNoOverflow(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("overflow");
        }
    }

    private MathPreconditions() {
    }
}
