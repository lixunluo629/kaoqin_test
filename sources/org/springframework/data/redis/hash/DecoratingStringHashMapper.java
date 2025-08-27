package org.springframework.data.redis.hash;

import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/hash/DecoratingStringHashMapper.class */
public class DecoratingStringHashMapper<T> implements HashMapper<T, String, String> {
    private final HashMapper<T, ?, ?> delegate;

    public DecoratingStringHashMapper(HashMapper<T, ?, ?> mapper) {
        this.delegate = mapper;
    }

    @Override // org.springframework.data.redis.hash.HashMapper
    public T fromHash(Map<String, String> map) {
        return this.delegate.fromHash(map);
    }

    @Override // org.springframework.data.redis.hash.HashMapper
    public Map<String, String> toHash(T object) {
        Map<?, ?> hash = this.delegate.toHash(object);
        Map<String, String> flatten = new LinkedHashMap<>(hash.size());
        for (Map.Entry<?, ?> entry : hash.entrySet()) {
            flatten.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return flatten;
    }
}
