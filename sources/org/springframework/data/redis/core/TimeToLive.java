package org.springframework.data.redis.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import org.springframework.data.annotation.ReadOnlyProperty;

@Target({ElementType.FIELD, ElementType.METHOD})
@ReadOnlyProperty
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/TimeToLive.class */
public @interface TimeToLive {
    TimeUnit unit() default TimeUnit.SECONDS;
}
