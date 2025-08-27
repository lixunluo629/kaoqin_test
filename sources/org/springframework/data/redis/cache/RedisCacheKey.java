package org.springframework.data.redis.cache;

import java.util.Arrays;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCacheKey.class */
public class RedisCacheKey {
    private final Object keyElement;
    private byte[] prefix;
    private RedisSerializer serializer;

    public RedisCacheKey(Object keyElement) {
        Assert.notNull(keyElement, "KeyElement must not be null!");
        this.keyElement = keyElement;
    }

    public byte[] getKeyBytes() {
        byte[] rawKey = serializeKeyElement();
        if (!hasPrefix()) {
            return rawKey;
        }
        byte[] prefixedKey = Arrays.copyOf(this.prefix, this.prefix.length + rawKey.length);
        System.arraycopy(rawKey, 0, prefixedKey, this.prefix.length, rawKey.length);
        return prefixedKey;
    }

    public Object getKeyElement() {
        return this.keyElement;
    }

    private byte[] serializeKeyElement() {
        if (this.serializer == null && (this.keyElement instanceof byte[])) {
            return (byte[]) this.keyElement;
        }
        return this.serializer.serialize(this.keyElement);
    }

    public void setSerializer(RedisSerializer<?> serializer) {
        this.serializer = serializer;
    }

    public boolean hasPrefix() {
        return this.prefix != null && this.prefix.length > 0;
    }

    public RedisCacheKey usePrefix(byte[] prefix) {
        this.prefix = prefix;
        return this;
    }

    public RedisCacheKey withKeySerializer(RedisSerializer serializer) {
        this.serializer = serializer;
        return this;
    }
}
