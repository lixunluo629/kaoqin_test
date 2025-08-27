package org.springframework.data.redis.support.collections;

import org.springframework.data.redis.core.BoundKeyOperations;
import org.springframework.data.redis.core.RedisOperations;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/RedisStore.class */
public interface RedisStore extends BoundKeyOperations<String> {
    RedisOperations<String, ?> getOperations();
}
