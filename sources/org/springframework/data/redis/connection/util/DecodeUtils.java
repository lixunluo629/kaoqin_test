package org.springframework.data.redis.connection.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/util/DecodeUtils.class */
public abstract class DecodeUtils {
    public static String decode(byte[] bytes) {
        return Base64.encodeToString(bytes, false);
    }

    public static String[] decodeMultiple(byte[]... bytes) {
        String[] result = new String[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = decode(bytes[i]);
        }
        return result;
    }

    public static byte[] encode(String string) {
        if (string == null) {
            return null;
        }
        return Base64.decode(string);
    }

    public static Map<byte[], byte[]> encodeMap(Map<String, byte[]> map) {
        Map<byte[], byte[]> result = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            result.put(encode(entry.getKey()), entry.getValue());
        }
        return result;
    }

    public static Map<String, byte[]> decodeMap(Map<byte[], byte[]> tuple) {
        Map<String, byte[]> result = new LinkedHashMap<>(tuple.size());
        for (Map.Entry<byte[], byte[]> entry : tuple.entrySet()) {
            result.put(decode(entry.getKey()), entry.getValue());
        }
        return result;
    }

    public static Set<byte[]> convertToSet(Collection<String> keys) {
        Set<byte[]> set = new LinkedHashSet<>(keys.size());
        for (String string : keys) {
            set.add(encode(string));
        }
        return set;
    }

    public static List<byte[]> convertToList(Collection<String> keys) {
        List<byte[]> set = new ArrayList<>(keys.size());
        for (String string : keys) {
            set.add(encode(string));
        }
        return set;
    }
}
