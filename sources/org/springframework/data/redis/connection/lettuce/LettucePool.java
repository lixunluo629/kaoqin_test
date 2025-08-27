package org.springframework.data.redis.connection.lettuce;

import com.lambdaworks.redis.AbstractRedisClient;
import com.lambdaworks.redis.api.StatefulConnection;
import org.springframework.data.redis.connection.Pool;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettucePool.class */
public interface LettucePool extends Pool<StatefulConnection<byte[], byte[]>> {
    /* renamed from: getClient */
    AbstractRedisClient mo7763getClient();
}
