package org.springframework.data.redis.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.keyvalue.annotation.KeySpace;

@Target({ElementType.TYPE})
@KeySpace
@Inherited
@Persistent
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisHash.class */
public @interface RedisHash {
    @AliasFor(annotation = KeySpace.class, attribute = "value")
    String value() default "";

    long timeToLive() default -1;
}
