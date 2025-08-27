package org.springframework.data.redis.serializer;

import java.nio.charset.Charset;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.springframework.util.Assert;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/serializer/JacksonJsonRedisSerializer.class */
public class JacksonJsonRedisSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private final JavaType javaType;
    private ObjectMapper objectMapper = new ObjectMapper();

    public JacksonJsonRedisSerializer(Class<T> type) {
        this.javaType = getJavaType(type);
    }

    public JacksonJsonRedisSerializer(JavaType javaType) {
        this.javaType = javaType;
    }

    @Override // org.springframework.data.redis.serializer.RedisSerializer
    public T deserialize(byte[] bArr) throws SerializationException {
        if (SerializationUtils.isEmpty(bArr)) {
            return null;
        }
        try {
            return (T) this.objectMapper.readValue(bArr, 0, bArr.length, this.javaType);
        } catch (Exception e) {
            throw new SerializationException("Could not read JSON: " + e.getMessage(), e);
        }
    }

    @Override // org.springframework.data.redis.serializer.RedisSerializer
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return SerializationUtils.EMPTY_ARRAY;
        }
        try {
            return this.objectMapper.writeValueAsBytes(t);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "'objectMapper' must not be null");
        this.objectMapper = objectMapper;
    }

    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}
