package org.springframework.data.redis;

import org.springframework.dao.UncategorizedDataAccessException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/RedisSystemException.class */
public class RedisSystemException extends UncategorizedDataAccessException {
    public RedisSystemException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
