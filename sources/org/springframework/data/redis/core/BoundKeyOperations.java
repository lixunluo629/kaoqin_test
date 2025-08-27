package org.springframework.data.redis.core;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/BoundKeyOperations.class */
public interface BoundKeyOperations<K> {
    K getKey();

    DataType getType();

    Long getExpire();

    Boolean expire(long j, TimeUnit timeUnit);

    Boolean expireAt(Date date);

    Boolean persist();

    void rename(K k);
}
