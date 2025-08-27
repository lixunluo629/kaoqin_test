package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.DefaultTuple;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/AbstractOperations.class */
abstract class AbstractOperations<K, V> {
    RedisTemplate<K, V> template;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/AbstractOperations$ValueDeserializingRedisCallback.class */
    abstract class ValueDeserializingRedisCallback implements RedisCallback<V> {
        private Object key;

        protected abstract byte[] inRedis(byte[] bArr, RedisConnection redisConnection);

        public ValueDeserializingRedisCallback(Object key) {
            this.key = key;
        }

        @Override // org.springframework.data.redis.core.RedisCallback
        /* renamed from: doInRedis */
        public final V doInRedis2(RedisConnection redisConnection) {
            return (V) AbstractOperations.this.deserializeValue(inRedis(AbstractOperations.this.rawKey(this.key), redisConnection));
        }
    }

    AbstractOperations(RedisTemplate<K, V> template) {
        this.template = template;
    }

    RedisSerializer keySerializer() {
        return this.template.getKeySerializer();
    }

    RedisSerializer valueSerializer() {
        return this.template.getValueSerializer();
    }

    RedisSerializer hashKeySerializer() {
        return this.template.getHashKeySerializer();
    }

    RedisSerializer hashValueSerializer() {
        return this.template.getHashValueSerializer();
    }

    RedisSerializer stringSerializer() {
        return this.template.getStringSerializer();
    }

    <T> T execute(RedisCallback<T> redisCallback, boolean z) {
        return (T) this.template.execute(redisCallback, z);
    }

    public RedisOperations<K, V> getOperations() {
        return this.template;
    }

    byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");
        if (keySerializer() == null && (key instanceof byte[])) {
            return (byte[]) key;
        }
        return keySerializer().serialize(key);
    }

    byte[] rawString(String key) {
        return stringSerializer().serialize(key);
    }

    byte[] rawValue(Object value) {
        if (valueSerializer() == null && (value instanceof byte[])) {
            return (byte[]) value;
        }
        return valueSerializer().serialize(value);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [byte[], byte[][]] */
    byte[][] rawValues(Object... values) {
        ?? r0 = new byte[values.length];
        int i = 0;
        for (Object value : values) {
            int i2 = i;
            i++;
            r0[i2] = rawValue(value);
        }
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [byte[], byte[][]] */
    byte[][] rawValues(Collection<V> values) {
        Assert.notEmpty((Collection<?>) values, "Values must not be 'null' or empty.");
        Assert.noNullElements(values.toArray(), "Values must not contain 'null' value.");
        ?? r0 = new byte[values.size()];
        int i = 0;
        for (V value : values) {
            int i2 = i;
            i++;
            r0[i2] = rawValue(value);
        }
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    <HK> byte[] rawHashKey(HK hk) {
        Assert.notNull(hk, "non null hash key required");
        if (hashKeySerializer() == null && (hk instanceof byte[])) {
            return (byte[]) hk;
        }
        return hashKeySerializer().serialize(hk);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [byte[], byte[][]] */
    <HK> byte[][] rawHashKeys(HK... hashKeys) {
        ?? r0 = new byte[hashKeys.length];
        int i = 0;
        for (HK hashKey : hashKeys) {
            int i2 = i;
            i++;
            r0[i2] = rawHashKey(hashKey);
        }
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    <HV> byte[] rawHashValue(HV hv) {
        if ((hashValueSerializer() == null) & (hv instanceof byte[])) {
            return (byte[]) hv;
        }
        return hashValueSerializer().serialize(hv);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    byte[][] rawKeys(K key, K otherKey) {
        return new byte[]{rawKey(key), rawKey(key)};
    }

    byte[][] rawKeys(Collection<K> keys) {
        return rawKeys((AbstractOperations<K, V>) null, (Collection<AbstractOperations<K, V>>) keys);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    byte[][] rawKeys(K key, Collection<K> keys) {
        ?? r0 = new byte[keys.size() + (key != null ? 1 : 0)];
        int i = 0;
        if (key != null) {
            i = 0 + 1;
            r0[0] = rawKey(key);
        }
        for (K k : keys) {
            int i2 = i;
            i++;
            r0[i2] = rawKey(k);
        }
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    Set<V> deserializeValues(Set<byte[]> set) {
        if (valueSerializer() == null) {
            return set;
        }
        return SerializationUtils.deserialize(set, valueSerializer());
    }

    Set<ZSetOperations.TypedTuple<V>> deserializeTupleValues(Collection<RedisZSetCommands.Tuple> rawValues) {
        if (rawValues == null) {
            return null;
        }
        Set<ZSetOperations.TypedTuple<V>> set = new LinkedHashSet<>(rawValues.size());
        for (RedisZSetCommands.Tuple rawValue : rawValues) {
            set.add(deserializeTuple(rawValue));
        }
        return set;
    }

    ZSetOperations.TypedTuple<V> deserializeTuple(RedisZSetCommands.Tuple tuple) throws SerializationException {
        Object value = tuple.getValue();
        if (valueSerializer() != null) {
            value = valueSerializer().deserialize(tuple.getValue());
        }
        return new DefaultTypedTuple(value, tuple.getScore());
    }

    Set<RedisZSetCommands.Tuple> rawTupleValues(Set<ZSetOperations.TypedTuple<V>> set) throws SerializationException {
        byte[] bArrSerialize;
        if (set == null) {
            return null;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet(set.size());
        for (ZSetOperations.TypedTuple<V> typedTuple : set) {
            if (valueSerializer() == null && (typedTuple.getValue() instanceof byte[])) {
                bArrSerialize = (byte[]) typedTuple.getValue();
            } else {
                bArrSerialize = valueSerializer().serialize(typedTuple.getValue());
            }
            linkedHashSet.add(new DefaultTuple(bArrSerialize, typedTuple.getScore()));
        }
        return linkedHashSet;
    }

    /* JADX WARN: Multi-variable type inference failed */
    List<V> deserializeValues(List<byte[]> list) {
        if (valueSerializer() == null) {
            return list;
        }
        return SerializationUtils.deserialize(list, valueSerializer());
    }

    /* JADX WARN: Multi-variable type inference failed */
    <T> Set<T> deserializeHashKeys(Set<byte[]> set) {
        if (hashKeySerializer() == null) {
            return set;
        }
        return SerializationUtils.deserialize(set, hashKeySerializer());
    }

    /* JADX WARN: Multi-variable type inference failed */
    <T> List<T> deserializeHashValues(List<byte[]> list) {
        if (hashValueSerializer() == null) {
            return list;
        }
        return SerializationUtils.deserialize(list, hashValueSerializer());
    }

    /* JADX WARN: Multi-variable type inference failed */
    <HK, HV> Map<HK, HV> deserializeHashMap(Map<byte[], byte[]> map) {
        if (map == null) {
            return null;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap(map.size());
        for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
            linkedHashMap.put(deserializeHashKey(entry.getKey()), deserializeHashValue(entry.getValue()));
        }
        return linkedHashMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    K deserializeKey(byte[] bArr) {
        if (keySerializer() == null) {
            return bArr;
        }
        return (K) keySerializer().deserialize(bArr);
    }

    Set<K> deserializeKeys(Set<byte[]> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }
        Set<K> result = new LinkedHashSet<>(keys.size());
        for (byte[] key : keys) {
            result.add(deserializeKey(key));
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    V deserializeValue(byte[] bArr) {
        if (valueSerializer() == null) {
            return bArr;
        }
        return (V) valueSerializer().deserialize(bArr);
    }

    String deserializeString(byte[] value) {
        return (String) stringSerializer().deserialize(value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    <HK> HK deserializeHashKey(byte[] bArr) {
        if (hashKeySerializer() == null) {
            return bArr;
        }
        return (HK) hashKeySerializer().deserialize(bArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    <HV> HV deserializeHashValue(byte[] bArr) {
        if (hashValueSerializer() == null) {
            return bArr;
        }
        return (HV) hashValueSerializer().deserialize(bArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    GeoResults<RedisGeoCommands.GeoLocation<V>> deserializeGeoResults(GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoResults) {
        if (valueSerializer() == null) {
            return geoResults;
        }
        return (GeoResults) Converters.deserializingGeoResultsConverter(valueSerializer()).convert2(geoResults);
    }
}
