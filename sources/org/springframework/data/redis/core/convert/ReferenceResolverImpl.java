package org.springframework.data.redis.core.convert;

import java.io.Serializable;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.convert.BinaryConverters;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/ReferenceResolverImpl.class */
public class ReferenceResolverImpl implements ReferenceResolver {
    private final RedisOperations<?, ?> redisOps;
    private final BinaryConverters.StringToBytesConverter converter;

    public ReferenceResolverImpl(RedisOperations<?, ?> redisOperations) {
        Assert.notNull(redisOperations, "RedisOperations must not be null!");
        this.redisOps = redisOperations;
        this.converter = new BinaryConverters.StringToBytesConverter();
    }

    @Override // org.springframework.data.redis.core.convert.ReferenceResolver
    public Map<byte[], byte[]> resolveReference(Serializable id, String keyspace) {
        final byte[] key = this.converter.convert(keyspace + ":" + id);
        return (Map) this.redisOps.execute(new RedisCallback<Map<byte[], byte[]>>() { // from class: org.springframework.data.redis.core.convert.ReferenceResolverImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            public Map<byte[], byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hGetAll(key);
            }
        });
    }
}
