package com.itextpdf.io.util;

import java.util.Collection;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/ArrayUtil.class */
public final class ArrayUtil {
    private ArrayUtil() {
    }

    public static byte[] shortenArray(byte[] src, int length) {
        if (length < src.length) {
            byte[] shortened = new byte[length];
            System.arraycopy(src, 0, shortened, 0, length);
            return shortened;
        }
        return src;
    }

    public static int[] toIntArray(Collection<Integer> collection) {
        int[] array = new int[collection.size()];
        int k = 0;
        for (Integer key : collection) {
            int i = k;
            k++;
            array[i] = key.intValue();
        }
        return array;
    }

    public static int hashCode(byte[] a) {
        if (a == null) {
            return 0;
        }
        int result = 1;
        for (byte element : a) {
            result = (31 * result) + element;
        }
        return result;
    }

    public static int[] fillWithValue(int[] a, int value) {
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
        return a;
    }

    public static float[] fillWithValue(float[] a, float value) {
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
        return a;
    }

    public static <T> void fillWithValue(T[] a, T value) {
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
    }

    public static int[] cloneArray(int[] src) {
        return (int[]) src.clone();
    }
}
