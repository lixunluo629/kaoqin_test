package org.springframework.data.redis.connection;

import org.springframework.dao.support.PersistenceExceptionTranslator;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisConnectionFactory.class */
public interface RedisConnectionFactory extends PersistenceExceptionTranslator {
    RedisConnection getConnection();

    RedisClusterConnection getClusterConnection();

    boolean getConvertPipelineAndTxResults();

    RedisSentinelConnection getSentinelConnection();
}
