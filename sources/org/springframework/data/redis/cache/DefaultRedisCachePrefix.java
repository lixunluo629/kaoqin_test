package org.springframework.data.redis.cache;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/DefaultRedisCachePrefix.class */
public class DefaultRedisCachePrefix implements RedisCachePrefix {
    private final RedisSerializer serializer;
    private final String delimiter;

    public DefaultRedisCachePrefix() {
        this(":");
    }

    public DefaultRedisCachePrefix(String delimiter) {
        this.serializer = new StringRedisSerializer();
        this.delimiter = delimiter;
    }

    @Override // org.springframework.data.redis.cache.RedisCachePrefix
    public byte[] prefix(String cacheName) {
        return this.serializer.serialize(this.delimiter != null ? cacheName.concat(this.delimiter) : cacheName.concat(":"));
    }
}
