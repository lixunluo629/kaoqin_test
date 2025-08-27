package org.springframework.data.redis.core.index;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/Indexed.class */
public @interface Indexed {
}
