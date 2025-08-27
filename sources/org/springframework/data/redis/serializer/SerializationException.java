package org.springframework.data.redis.serializer;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/serializer/SerializationException.class */
public class SerializationException extends NestedRuntimeException {
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SerializationException(String msg) {
        super(msg);
    }
}
