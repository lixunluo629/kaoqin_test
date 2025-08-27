package org.springframework.data.redis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/util/ByteUtils.class */
public final class ByteUtils {
    private ByteUtils() {
    }

    public static byte[] concat(byte[] arg1, byte[] arg2) {
        byte[] result = Arrays.copyOf(arg1, arg1.length + arg2.length);
        System.arraycopy(arg2, 0, result, arg1.length, arg2.length);
        return result;
    }

    public static byte[] concatAll(byte[]... args) {
        if (args.length == 0) {
            return new byte[0];
        }
        if (args.length == 1) {
            return args[0];
        }
        byte[] cur = concat(args[0], args[1]);
        for (int i = 2; i < args.length; i++) {
            cur = concat(cur, args[i]);
        }
        return cur;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [byte[], byte[][]] */
    public static byte[][] split(byte[] source, int c) {
        if (source == null || source.length == 0) {
            return new byte[0];
        }
        List<byte[]> bytes = new ArrayList<>();
        int offset = 0;
        int i = 0;
        while (true) {
            if (i > source.length) {
                break;
            }
            if (i == source.length) {
                bytes.add(Arrays.copyOfRange(source, offset, i));
                break;
            }
            if (source[i] == c) {
                bytes.add(Arrays.copyOfRange(source, offset, i));
                offset = i + 1;
            }
            i++;
        }
        return (byte[][]) bytes.toArray((Object[]) new byte[bytes.size()]);
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [byte[], byte[][], java.lang.Object] */
    public static byte[][] mergeArrays(byte[] firstArray, byte[]... additionalArrays) {
        Assert.notNull(firstArray, "first array must not be null");
        Assert.notNull(additionalArrays, "additional arrays must not be null");
        ?? r0 = new byte[additionalArrays.length + 1];
        r0[0] = firstArray;
        System.arraycopy(additionalArrays, 0, r0, 1, additionalArrays.length);
        return r0;
    }

    public static boolean startsWith(byte[] haystack, byte[] prefix) {
        return startsWith(haystack, prefix, 0);
    }

    public static boolean startsWith(byte[] haystack, byte[] prefix, int offset) {
        int i;
        int i2;
        int to = offset;
        int prefixOffset = 0;
        int prefixLength = prefix.length;
        if (offset < 0 || offset > haystack.length - prefixLength) {
            return false;
        }
        do {
            prefixLength--;
            if (prefixLength < 0) {
                return true;
            }
            i = to;
            to++;
            i2 = prefixOffset;
            prefixOffset++;
        } while (haystack[i] == prefix[i2]);
        return false;
    }

    public static int indexOf(byte[] haystack, byte needle) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] == needle) {
                return i;
            }
        }
        return -1;
    }
}
