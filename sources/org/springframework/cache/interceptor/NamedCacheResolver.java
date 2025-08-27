package org.springframework.cache.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.cache.CacheManager;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/NamedCacheResolver.class */
public class NamedCacheResolver extends AbstractCacheResolver {
    private Collection<String> cacheNames;

    public NamedCacheResolver() {
    }

    public NamedCacheResolver(CacheManager cacheManager, String... cacheNames) {
        super(cacheManager);
        this.cacheNames = new ArrayList(Arrays.asList(cacheNames));
    }

    public void setCacheNames(Collection<String> cacheNames) {
        this.cacheNames = cacheNames;
    }

    @Override // org.springframework.cache.interceptor.AbstractCacheResolver
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        return this.cacheNames;
    }
}
