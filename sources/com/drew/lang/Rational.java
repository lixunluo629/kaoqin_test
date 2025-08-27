package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.io.Serializable;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/Rational.class */
public class Rational extends Number implements Comparable<Rational>, Serializable {
    private static final long serialVersionUID = 510688928138848770L;
    private final long _numerator;
    private final long _denominator;

    public Rational(long numerator, long denominator) {
        this._numerator = numerator;
        this._denominator = denominator;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        if (this._numerator == 0) {
            return 0.0d;
        }
        return this._numerator / this._denominator;
    }

    @Override // java.lang.Number
    public float floatValue() {
        if (this._numerator == 0) {
            return 0.0f;
        }
        return this._numerator / this._denominator;
    }

    @Override // java.lang.Number
    public final byte byteValue() {
        return (byte) doubleValue();
    }

    @Override // java.lang.Number
    public final int intValue() {
        return (int) doubleValue();
    }

    @Override // java.lang.Number
    public final long longValue() {
        return (long) doubleValue();
    }

    @Override // java.lang.Number
    public final short shortValue() {
        return (short) doubleValue();
    }

    public final long getDenominator() {
        return this._denominator;
    }

    public final long getNumerator() {
        return this._numerator;
    }

    @NotNull
    public Rational getReciprocal() {
        return new Rational(this._denominator, this._numerator);
    }

    public boolean isInteger() {
        return this._denominator == 1 || (this._denominator != 0 && this._numerator % this._denominator == 0) || (this._denominator == 0 && this._numerator == 0);
    }

    public boolean isZero() {
        return this._numerator == 0 || this._denominator == 0;
    }

    @NotNull
    public String toString() {
        return this._numerator + "/" + this._denominator;
    }

    @NotNull
    public String toSimpleString(boolean allowDecimal) {
        if (this._denominator == 0 && this._numerator != 0) {
            return toString();
        }
        if (isInteger()) {
            return Integer.toString(intValue());
        }
        if (this._numerator != 1 && this._denominator % this._numerator == 0) {
            long newDenominator = this._denominator / this._numerator;
            return new Rational(1L, newDenominator).toSimpleString(allowDecimal);
        }
        Rational simplifiedInstance = getSimplifiedInstance();
        if (allowDecimal) {
            String doubleString = Double.toString(simplifiedInstance.doubleValue());
            if (doubleString.length() < 5) {
                return doubleString;
            }
        }
        return simplifiedInstance.toString();
    }

    @Override // java.lang.Comparable
    public int compareTo(@NotNull Rational that) {
        return Double.compare(doubleValue(), that.doubleValue());
    }

    public boolean equals(Rational other) {
        return other.doubleValue() == doubleValue();
    }

    public boolean equalsExact(Rational other) {
        return getDenominator() == other.getDenominator() && getNumerator() == other.getNumerator();
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Rational)) {
            return false;
        }
        Rational that = (Rational) obj;
        return doubleValue() == that.doubleValue();
    }

    public int hashCode() {
        return (23 * ((int) this._denominator)) + ((int) this._numerator);
    }

    @NotNull
    public Rational getSimplifiedInstance() {
        long gcd = GCD(this._numerator, this._denominator);
        return new Rational(this._numerator / gcd, this._denominator / gcd);
    }

    private static long GCD(long a, long b) {
        if (a < 0) {
            a = -a;
        }
        if (b < 0) {
            b = -b;
        }
        while (a != 0 && b != 0) {
            if (a > b) {
                a %= b;
            } else {
                b %= a;
            }
        }
        return a == 0 ? b : a;
    }
}
