package org.springframework.cache.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CompositeCacheOperationSource.class */
public class CompositeCacheOperationSource implements CacheOperationSource, Serializable {
    private final CacheOperationSource[] cacheOperationSources;

    public CompositeCacheOperationSource(CacheOperationSource... cacheOperationSources) {
        Assert.notEmpty(cacheOperationSources, "CacheOperationSource array must not be empty");
        this.cacheOperationSources = cacheOperationSources;
    }

    public final CacheOperationSource[] getCacheOperationSources() {
        return this.cacheOperationSources;
    }

    @Override // org.springframework.cache.interceptor.CacheOperationSource
    public Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass) {
        Collection<CacheOperation> ops = null;
        for (CacheOperationSource source : this.cacheOperationSources) {
            Collection<CacheOperation> cacheOperations = source.getCacheOperations(method, targetClass);
            if (cacheOperations != null) {
                if (ops == null) {
                    ops = new ArrayList<>();
                }
                ops.addAll(cacheOperations);
            }
        }
        return ops;
    }
}
