package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheOperationSource.class */
public interface CacheOperationSource {
    Collection<CacheOperation> getCacheOperations(Method method, Class<?> cls);
}
