package org.springframework.cache.annotation;

import java.lang.reflect.Method;
import java.util.Collection;
import org.springframework.cache.interceptor.CacheOperation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/annotation/CacheAnnotationParser.class */
public interface CacheAnnotationParser {
    Collection<CacheOperation> parseCacheAnnotations(Class<?> cls);

    Collection<CacheOperation> parseCacheAnnotations(Method method);
}
