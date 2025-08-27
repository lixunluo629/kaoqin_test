package org.springframework.cache.interceptor;

import java.util.Collection;
import org.springframework.cache.Cache;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheResolver.class */
public interface CacheResolver {
    Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> cacheOperationInvocationContext);
}
