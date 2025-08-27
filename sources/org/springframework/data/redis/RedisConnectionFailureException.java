package org.springframework.data.redis;

import org.springframework.dao.DataAccessResourceFailureException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/RedisConnectionFailureException.class */
public class RedisConnectionFailureException extends DataAccessResourceFailureException {
    public RedisConnectionFailureException(String msg) {
        super(msg);
    }

    public RedisConnectionFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
