package org.springframework.cache.jcache.interceptor;

import java.lang.reflect.Method;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/interceptor/JCacheOperationSource.class */
public interface JCacheOperationSource {
    JCacheOperation<?> getCacheOperation(Method method, Class<?> cls);
}
