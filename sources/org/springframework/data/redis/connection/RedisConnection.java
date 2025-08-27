package org.springframework.data.redis.connection;

import java.util.List;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisConnection.class */
public interface RedisConnection extends RedisCommands {
    void close() throws DataAccessException;

    boolean isClosed();

    Object getNativeConnection();

    boolean isQueueing();

    boolean isPipelined();

    void openPipeline();

    List<Object> closePipeline() throws RedisPipelineException;

    RedisSentinelConnection getSentinelConnection();
}
