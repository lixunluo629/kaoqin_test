package org.springframework.data.redis.serializer;

import java.nio.charset.Charset;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/serializer/StringRedisSerializer.class */
public class StringRedisSerializer implements RedisSerializer<String> {
    private final Charset charset;

    public StringRedisSerializer() {
        this(Charset.forName("UTF8"));
    }

    public StringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.redis.serializer.RedisSerializer
    public String deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, this.charset);
    }

    @Override // org.springframework.data.redis.serializer.RedisSerializer
    public byte[] serialize(String string) {
        if (string == null) {
            return null;
        }
        return string.getBytes(this.charset);
    }
}
