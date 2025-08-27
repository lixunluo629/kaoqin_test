package org.springframework.data.redis.connection;

import java.io.Closeable;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisSentinelConnection.class */
public interface RedisSentinelConnection extends RedisSentinelCommands, Closeable {
    boolean isOpen();
}
