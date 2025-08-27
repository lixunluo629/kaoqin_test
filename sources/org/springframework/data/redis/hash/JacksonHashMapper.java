package org.springframework.data.redis.hash;

import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/hash/JacksonHashMapper.class */
public class JacksonHashMapper<T> implements HashMapper<T, String, Object> {
    private final ObjectMapper mapper;
    private final JavaType userType;
    private final JavaType mapType;

    public JacksonHashMapper(Class<T> type) {
        this(type, new ObjectMapper());
        this.mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    public JacksonHashMapper(Class<T> type, ObjectMapper mapper) {
        this.mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class);
        this.mapper = mapper;
        this.userType = TypeFactory.defaultInstance().constructType(type);
    }

    @Override // org.springframework.data.redis.hash.HashMapper
    public T fromHash(Map<String, Object> map) {
        return (T) this.mapper.convertValue(map, this.userType);
    }

    @Override // org.springframework.data.redis.hash.HashMapper
    public Map<String, Object> toHash(T object) {
        return (Map) this.mapper.convertValue(object, this.mapType);
    }
}
