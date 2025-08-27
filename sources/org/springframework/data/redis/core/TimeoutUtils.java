package org.springframework.data.redis.core;

import java.util.concurrent.TimeUnit;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/TimeoutUtils.class */
public abstract class TimeoutUtils {
    public static long toSeconds(long timeout, TimeUnit unit) {
        return roundUpIfNecessary(timeout, unit.toSeconds(timeout));
    }

    public static long toMillis(long timeout, TimeUnit unit) {
        return roundUpIfNecessary(timeout, unit.toMillis(timeout));
    }

    private static long roundUpIfNecessary(long timeout, long convertedTimeout) {
        if (timeout > 0 && convertedTimeout == 0) {
            return 1L;
        }
        return convertedTimeout;
    }
}
