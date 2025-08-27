package org.springframework.data.redis.support.collections;

import java.util.List;
import java.util.concurrent.BlockingDeque;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/RedisList.class */
public interface RedisList<E> extends RedisCollection<E>, List<E>, BlockingDeque<E> {
    List<E> range(long j, long j2);

    RedisList<E> trim(int i, int i2);
}
