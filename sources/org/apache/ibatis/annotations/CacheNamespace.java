package org.apache.ibatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.impl.PerpetualCache;

@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/annotations/CacheNamespace.class */
public @interface CacheNamespace {
    Class<? extends Cache> implementation() default PerpetualCache.class;

    Class<? extends Cache> eviction() default LruCache.class;

    long flushInterval() default 0;

    int size() default 1024;

    boolean readWrite() default true;

    boolean blocking() default false;

    Property[] properties() default {};
}
