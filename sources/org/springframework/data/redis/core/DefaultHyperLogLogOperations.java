package org.springframework.data.redis.core;

import java.util.Arrays;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultHyperLogLogOperations.class */
public class DefaultHyperLogLogOperations<K, V> extends AbstractOperations<K, V> implements HyperLogLogOperations<K, V> {
    @Override // org.springframework.data.redis.core.AbstractOperations
    public /* bridge */ /* synthetic */ RedisOperations getOperations() {
        return super.getOperations();
    }

    public DefaultHyperLogLogOperations(RedisTemplate<K, V> template) {
        super(template);
    }

    @Override // org.springframework.data.redis.core.HyperLogLogOperations
    public Long add(K key, V... values) {
        final byte[] rawKey = rawKey(key);
        final byte[][] rawValues = rawValues(values);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultHyperLogLogOperations.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.pfAdd(rawKey, rawValues);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.HyperLogLogOperations
    public Long size(K... keys) {
        final byte[][] rawKeys = rawKeys(Arrays.asList(keys));
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultHyperLogLogOperations.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.pfCount(rawKeys);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.HyperLogLogOperations
    public Long union(K destination, K... sourceKeys) {
        final byte[] rawDestinationKey = rawKey(destination);
        final byte[][] rawSourceKeys = rawKeys(Arrays.asList(sourceKeys));
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.DefaultHyperLogLogOperations.3
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r1v3, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.pfMerge(rawDestinationKey, rawSourceKeys);
                return redisConnection.pfCount(new byte[]{rawDestinationKey});
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.HyperLogLogOperations
    public void delete(K key) {
        this.template.delete((RedisTemplate<K, V>) key);
    }
}
