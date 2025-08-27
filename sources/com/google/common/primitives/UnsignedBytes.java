package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Comparator;
import sun.misc.Unsafe;

/* loaded from: guava-18.0.jar:com/google/common/primitives/UnsignedBytes.class */
public final class UnsignedBytes {
    public static final byte MAX_POWER_OF_TWO = Byte.MIN_VALUE;
    public static final byte MAX_VALUE = -1;
    private static final int UNSIGNED_MASK = 255;

    private UnsignedBytes() {
    }

    public static int toInt(byte value) {
        return value & 255;
    }

    public static byte checkedCast(long value) {
        if ((value >> 8) != 0) {
            throw new IllegalArgumentException(new StringBuilder(34).append("Out of range: ").append(value).toString());
        }
        return (byte) value;
    }

    public static byte saturatedCast(long value) {
        if (value > toInt((byte) -1)) {
            return (byte) -1;
        }
        if (value < 0) {
            return (byte) 0;
        }
        return (byte) value;
    }

    public static int compare(byte a, byte b) {
        return toInt(a) - toInt(b);
    }

    public static byte min(byte... array) {
        Preconditions.checkArgument(array.length > 0);
        int min = toInt(array[0]);
        for (int i = 1; i < array.length; i++) {
            int next = toInt(array[i]);
            if (next < min) {
                min = next;
            }
        }
        return (byte) min;
    }

    public static byte max(byte... array) {
        Preconditions.checkArgument(array.length > 0);
        int max = toInt(array[0]);
        for (int i = 1; i < array.length; i++) {
            int next = toInt(array[i]);
            if (next > max) {
                max = next;
            }
        }
        return (byte) max;
    }

    @Beta
    public static String toString(byte x) {
        return toString(x, 10);
    }

    @Beta
    public static String toString(byte x, int radix) {
        Preconditions.checkArgument(radix >= 2 && radix <= 36, "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", Integer.valueOf(radix));
        return Integer.toString(toInt(x), radix);
    }

    @Beta
    public static byte parseUnsignedByte(String string) {
        return parseUnsignedByte(string, 10);
    }

    @Beta
    public static byte parseUnsignedByte(String string, int radix) throws NumberFormatException {
        int parse = Integer.parseInt((String) Preconditions.checkNotNull(string), radix);
        if ((parse >> 8) == 0) {
            return (byte) parse;
        }
        throw new NumberFormatException(new StringBuilder(25).append("out of range: ").append(parse).toString());
    }

    public static String join(String separator, byte... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * (3 + separator.length()));
        builder.append(toInt(array[0]));
        for (int i = 1; i < array.length; i++) {
            builder.append(separator).append(toString(array[i]));
        }
        return builder.toString();
    }

    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparatorHolder.BEST_COMPARATOR;
    }

    @VisibleForTesting
    static Comparator<byte[]> lexicographicalComparatorJavaImpl() {
        return LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
    }

    @VisibleForTesting
    /* loaded from: guava-18.0.jar:com/google/common/primitives/UnsignedBytes$LexicographicalComparatorHolder.class */
    static class LexicographicalComparatorHolder {
        static final String UNSAFE_COMPARATOR_NAME = String.valueOf(LexicographicalComparatorHolder.class.getName()).concat("$UnsafeComparator");
        static final Comparator<byte[]> BEST_COMPARATOR = getBestComparator();

        LexicographicalComparatorHolder() {
        }

        @VisibleForTesting
        /* loaded from: guava-18.0.jar:com/google/common/primitives/UnsignedBytes$LexicographicalComparatorHolder$UnsafeComparator.class */
        enum UnsafeComparator implements Comparator<byte[]> {
            INSTANCE;

            static final boolean BIG_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
            static final Unsafe theUnsafe = getUnsafe();
            static final int BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);

            static {
                if (theUnsafe.arrayIndexScale(byte[].class) != 1) {
                    throw new AssertionError();
                }
            }

            private static Unsafe getUnsafe() {
                try {
                    return Unsafe.getUnsafe();
                } catch (SecurityException e) {
                    try {
                        return (Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() { // from class: com.google.common.primitives.UnsignedBytes.LexicographicalComparatorHolder.UnsafeComparator.1
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedExceptionAction
                            public Unsafe run() throws Exception {
                                Field[] arr$ = Unsafe.class.getDeclaredFields();
                                for (Field f : arr$) {
                                    f.setAccessible(true);
                                    Object x = f.get(null);
                                    if (Unsafe.class.isInstance(x)) {
                                        return (Unsafe) Unsafe.class.cast(x);
                                    }
                                }
                                throw new NoSuchFieldError("the Unsafe");
                            }
                        });
                    } catch (PrivilegedActionException e2) {
                        throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
                    }
                }
            }

            @Override // java.util.Comparator
            public int compare(byte[] left, byte[] right) {
                int minLength = Math.min(left.length, right.length);
                int minWords = minLength / 8;
                for (int i = 0; i < minWords * 8; i += 8) {
                    long lw = theUnsafe.getLong(left, BYTE_ARRAY_BASE_OFFSET + i);
                    long rw = theUnsafe.getLong(right, BYTE_ARRAY_BASE_OFFSET + i);
                    if (lw != rw) {
                        if (BIG_ENDIAN) {
                            return UnsignedLongs.compare(lw, rw);
                        }
                        int n = Long.numberOfTrailingZeros(lw ^ rw) & (-8);
                        return (int) (((lw >>> n) & 255) - ((rw >>> n) & 255));
                    }
                }
                for (int i2 = minWords * 8; i2 < minLength; i2++) {
                    int result = UnsignedBytes.compare(left[i2], right[i2]);
                    if (result != 0) {
                        return result;
                    }
                }
                return left.length - right.length;
            }
        }

        /* loaded from: guava-18.0.jar:com/google/common/primitives/UnsignedBytes$LexicographicalComparatorHolder$PureJavaComparator.class */
        enum PureJavaComparator implements Comparator<byte[]> {
            INSTANCE;

            @Override // java.util.Comparator
            public int compare(byte[] left, byte[] right) {
                int minLength = Math.min(left.length, right.length);
                for (int i = 0; i < minLength; i++) {
                    int result = UnsignedBytes.compare(left[i], right[i]);
                    if (result != 0) {
                        return result;
                    }
                }
                return left.length - right.length;
            }
        }

        static Comparator<byte[]> getBestComparator() {
            try {
                Class<?> theClass = Class.forName(UNSAFE_COMPARATOR_NAME);
                Comparator<byte[]> comparator = (Comparator) theClass.getEnumConstants()[0];
                return comparator;
            } catch (Throwable th) {
                return UnsignedBytes.lexicographicalComparatorJavaImpl();
            }
        }
    }
}
