package org.springframework.data.redis.serializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/serializer/SerializationUtils.class */
public abstract class SerializationUtils {
    static final byte[] EMPTY_ARRAY = new byte[0];

    static boolean isEmpty(byte[] data) {
        return data == null || data.length == 0;
    }

    static <T extends Collection<?>> T deserializeValues(Collection<byte[]> rawValues, Class<T> type, RedisSerializer<?> redisSerializer) {
        if (rawValues == null) {
            return null;
        }
        T arrayList = List.class.isAssignableFrom(type) ? new ArrayList(rawValues.size()) : new LinkedHashSet(rawValues.size());
        for (byte[] bs : rawValues) {
            arrayList.add(redisSerializer.deserialize(bs));
        }
        return arrayList;
    }

    public static <T> Set<T> deserialize(Set<byte[]> rawValues, RedisSerializer<T> redisSerializer) {
        return (Set) deserializeValues(rawValues, Set.class, redisSerializer);
    }

    public static <T> List<T> deserialize(List<byte[]> rawValues, RedisSerializer<T> redisSerializer) {
        return (List) deserializeValues(rawValues, List.class, redisSerializer);
    }

    public static <T> Collection<T> deserialize(Collection<byte[]> rawValues, RedisSerializer<T> redisSerializer) {
        return deserializeValues(rawValues, List.class, redisSerializer);
    }

    public static <T> Map<T, T> deserialize(Map<byte[], byte[]> rawValues, RedisSerializer<T> redisSerializer) {
        if (rawValues == null) {
            return null;
        }
        Map<T, T> ret = new LinkedHashMap<>(rawValues.size());
        for (Map.Entry<byte[], byte[]> entry : rawValues.entrySet()) {
            ret.put(redisSerializer.deserialize(entry.getKey()), redisSerializer.deserialize(entry.getValue()));
        }
        return ret;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <HK, HV> Map<HK, HV> deserialize(Map<byte[], byte[]> map, RedisSerializer<HK> redisSerializer, RedisSerializer<HV> redisSerializer2) {
        if (map == null) {
            return null;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap(map.size());
        for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
            linkedHashMap.put(redisSerializer != null ? redisSerializer.deserialize(entry.getKey()) : (HK) entry.getKey(), redisSerializer2 != null ? redisSerializer2.deserialize(entry.getValue()) : (HV) entry.getValue());
        }
        return linkedHashMap;
    }
}
