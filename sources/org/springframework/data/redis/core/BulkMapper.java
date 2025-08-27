package org.springframework.data.redis.core;

import java.util.List;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/BulkMapper.class */
public interface BulkMapper<T, V> {
    T mapBulk(List<V> list);
}
