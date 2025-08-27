package org.springframework.data.redis.serializer;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/serializer/RedisSerializer.class */
public interface RedisSerializer<T> {
    byte[] serialize(T t) throws SerializationException;

    T deserialize(byte[] bArr) throws SerializationException;
}
