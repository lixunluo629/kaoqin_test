package org.springframework.data.redis.listener.adapter;

import org.springframework.dao.InvalidDataAccessApiUsageException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/listener/adapter/RedisListenerExecutionFailedException.class */
public class RedisListenerExecutionFailedException extends InvalidDataAccessApiUsageException {
    public RedisListenerExecutionFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RedisListenerExecutionFailedException(String msg) {
        super(msg);
    }
}
