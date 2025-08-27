package org.springframework.data.redis.hash;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.BeanUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/hash/BeanUtilsHashMapper.class */
public class BeanUtilsHashMapper<T> implements HashMapper<T, String, String> {
    private Class<T> type;

    public BeanUtilsHashMapper(Class<T> type) {
        this.type = type;
    }

    @Override // org.springframework.data.redis.hash.HashMapper
    public T fromHash(Map<String, String> map) {
        T t = (T) BeanUtils.instantiate(this.type);
        try {
            org.apache.commons.beanutils.BeanUtils.populate(t, map);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.springframework.data.redis.hash.HashMapper
    public Map<String, String> toHash(T object) {
        try {
            Map<String, String> map = org.apache.commons.beanutils.BeanUtils.describe(object);
            Map<String, String> result = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue() != null) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
            return result;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot describe object " + object, ex);
        }
    }
}
