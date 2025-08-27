package org.springframework.data.redis.connection;

import org.springframework.dao.InvalidDataAccessResourceUsageException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisInvalidSubscriptionException.class */
public class RedisInvalidSubscriptionException extends InvalidDataAccessResourceUsageException {
    public RedisInvalidSubscriptionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RedisInvalidSubscriptionException(String msg) {
        super(msg);
    }
}
