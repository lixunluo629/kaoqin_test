package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;

@Beta
@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/primitives/UnsignedInts.class */
public final class UnsignedInts {
    static final long INT_MASK = 4294967295L;

    private UnsignedInts() {
    }

    static int flip(int value) {
        return value ^ Integer.MIN_VALUE;
    }

    public static int compare(int a, int b) {
        return Ints.compare(flip(a), flip(b));
    }

    public static long toLong(int value) {
        return value & 4294967295L;
    }

    public static int min(int... array) {
        Preconditions.checkArgument(array.length > 0);
        int min = flip(array[0]);
        for (int i = 1; i < array.length; i++) {
            int next = flip(array[i]);
            if (next < min) {
                min = next;
            }
        }
        return flip(min);
    }

    public static int max(int... array) {
        Preconditions.checkArgument(array.length > 0);
        int max = flip(array[0]);
        for (int i = 1; i < array.length; i++) {
            int next = flip(array[i]);
            if (next > max) {
                max = next;
            }
        }
        return flip(max);
    }

    public static String join(String separator, int... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(toString(array[0]));
        for (int i = 1; i < array.length; i++) {
            builder.append(separator).append(toString(array[i]));
        }
        return builder.toString();
    }

    public static Comparator<int[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    /* loaded from: guava-18.0.jar:com/google/common/primitives/UnsignedInts$LexicographicalComparator.class */
    enum LexicographicalComparator implements Comparator<int[]> {
        INSTANCE;

        @Override // java.util.Comparator
        public int compare(int[] left, int[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i = 0; i < minLength; i++) {
                if (left[i] != right[i]) {
                    return UnsignedInts.compare(left[i], right[i]);
                }
            }
            return left.length - right.length;
        }
    }

    public static int divide(int dividend, int divisor) {
        return (int) (toLong(dividend) / toLong(divisor));
    }

    public static int remainder(int dividend, int divisor) {
        return (int) (toLong(dividend) % toLong(divisor));
    }

    public static int decode(String stringValue) {
        String strConcat;
        ParseRequest request = ParseRequest.fromString(stringValue);
        try {
            return parseUnsignedInt(request.rawValue, request.radix);
        } catch (NumberFormatException e) {
            String strValueOf = String.valueOf(stringValue);
            if (strValueOf.length() != 0) {
                strConcat = "Error parsing value: ".concat(strValueOf);
            } else {
                strConcat = str;
                String str = new String("Error parsing value: ");
            }
            NumberFormatException decodeException = new NumberFormatException(strConcat);
            decodeException.initCause(e);
            throw decodeException;
        }
    }

    public static int parseUnsignedInt(String s) {
        return parseUnsignedInt(s, 10);
    }

    public static int parseUnsignedInt(String string, int radix) throws NumberFormatException {
        Preconditions.checkNotNull(string);
        long result = Long.parseLong(string, radix);
        if ((result & 4294967295L) != result) {
            String strValueOf = String.valueOf(String.valueOf(string));
            throw new NumberFormatException(new StringBuilder(69 + strValueOf.length()).append("Input ").append(strValueOf).append(" in base ").append(radix).append(" is not in the range of an unsigned integer").toString());
        }
        return (int) result;
    }

    public static String toString(int x) {
        return toString(x, 10);
    }

    public static String toString(int x, int radix) {
        long asLong = x & 4294967295L;
        return Long.toString(asLong, radix);
    }
}
