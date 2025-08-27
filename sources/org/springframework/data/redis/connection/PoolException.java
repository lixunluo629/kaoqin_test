package org.springframework.data.redis.connection;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/PoolException.class */
public class PoolException extends NestedRuntimeException {
    public PoolException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PoolException(String msg) {
        super(msg);
    }
}
