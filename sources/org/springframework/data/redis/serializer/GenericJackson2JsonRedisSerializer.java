package org.springframework.data.redis.serializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.springframework.cache.support.NullValue;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/serializer/GenericJackson2JsonRedisSerializer.class */
public class GenericJackson2JsonRedisSerializer implements RedisSerializer<Object> {
    private final ObjectMapper mapper;

    public GenericJackson2JsonRedisSerializer() {
        this((String) null);
    }

    public GenericJackson2JsonRedisSerializer(String classPropertyTypeName) {
        this(new ObjectMapper());
        this.mapper.registerModule(new SimpleModule().addSerializer(new NullValueSerializer(classPropertyTypeName)));
        if (StringUtils.hasText(classPropertyTypeName)) {
            this.mapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, classPropertyTypeName);
        } else {
            this.mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
    }

    public GenericJackson2JsonRedisSerializer(ObjectMapper mapper) {
        Assert.notNull(mapper, "ObjectMapper must not be null!");
        this.mapper = mapper;
    }

    @Override // org.springframework.data.redis.serializer.RedisSerializer
    public byte[] serialize(Object source) throws SerializationException {
        if (source == null) {
            return SerializationUtils.EMPTY_ARRAY;
        }
        try {
            return this.mapper.writeValueAsBytes(source);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
        }
    }

    @Override // org.springframework.data.redis.serializer.RedisSerializer
    public Object deserialize(byte[] source) throws SerializationException {
        return deserialize(source, Object.class);
    }

    public <T> T deserialize(byte[] bArr, Class<T> cls) throws SerializationException {
        Assert.notNull(cls, "Deserialization type must not be null! Pleaes provide Object.class to make use of Jackson2 default typing.");
        if (SerializationUtils.isEmpty(bArr)) {
            return null;
        }
        try {
            return (T) this.mapper.readValue(bArr, cls);
        } catch (Exception e) {
            throw new SerializationException("Could not read JSON: " + e.getMessage(), e);
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/serializer/GenericJackson2JsonRedisSerializer$NullValueSerializer.class */
    private class NullValueSerializer extends StdSerializer<NullValue> {
        private static final long serialVersionUID = 1999052150548658808L;
        private final String classIdentifier;

        NullValueSerializer(String classIdentifier) {
            super(NullValue.class);
            this.classIdentifier = StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
        }

        @Override // com.fasterxml.jackson.databind.ser.std.StdSerializer, com.fasterxml.jackson.databind.JsonSerializer
        public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStartObject();
            jgen.writeStringField(this.classIdentifier, NullValue.class.getName());
            jgen.writeEndObject();
        }
    }
}
