package org.springframework.data.redis.cache;

import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCacheElement.class */
public class RedisCacheElement extends SimpleValueWrapper {
    private final RedisCacheKey cacheKey;
    private long timeToLive;

    public RedisCacheElement(RedisCacheKey cacheKey, Object value) {
        super(value);
        Assert.notNull(cacheKey, "CacheKey must not be null!");
        this.cacheKey = cacheKey;
    }

    public byte[] getKeyBytes() {
        return this.cacheKey.getKeyBytes();
    }

    public RedisCacheKey getKey() {
        return this.cacheKey;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public long getTimeToLive() {
        return this.timeToLive;
    }

    public boolean hasKeyPrefix() {
        return this.cacheKey.hasPrefix();
    }

    public boolean isEternal() {
        return 0 == this.timeToLive;
    }

    public RedisCacheElement expireAfter(long seconds) {
        setTimeToLive(seconds);
        return this;
    }
}
