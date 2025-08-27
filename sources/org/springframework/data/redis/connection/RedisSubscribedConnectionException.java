package org.springframework.data.redis.connection;

import org.springframework.dao.InvalidDataAccessApiUsageException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisSubscribedConnectionException.class */
public class RedisSubscribedConnectionException extends InvalidDataAccessApiUsageException {
    public RedisSubscribedConnectionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RedisSubscribedConnectionException(String msg) {
        super(msg);
    }
}
