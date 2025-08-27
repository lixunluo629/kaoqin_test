package org.springframework.cache.interceptor;

import java.util.Collection;
import org.springframework.cache.CacheManager;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/SimpleCacheResolver.class */
public class SimpleCacheResolver extends AbstractCacheResolver {
    public SimpleCacheResolver() {
    }

    public SimpleCacheResolver(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override // org.springframework.cache.interceptor.AbstractCacheResolver
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        return context.getOperation().getCacheNames();
    }
}
