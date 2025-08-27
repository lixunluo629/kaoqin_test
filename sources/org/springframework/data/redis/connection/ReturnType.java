package org.springframework.data.redis.connection;

import java.util.List;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ReturnType.class */
public enum ReturnType {
    BOOLEAN,
    INTEGER,
    MULTI,
    STATUS,
    VALUE;

    public static ReturnType fromJavaType(Class<?> javaType) {
        if (javaType == null) {
            return STATUS;
        }
        if (javaType.isAssignableFrom(List.class)) {
            return MULTI;
        }
        if (javaType.isAssignableFrom(Boolean.class)) {
            return BOOLEAN;
        }
        if (javaType.isAssignableFrom(Long.class)) {
            return INTEGER;
        }
        return VALUE;
    }
}
