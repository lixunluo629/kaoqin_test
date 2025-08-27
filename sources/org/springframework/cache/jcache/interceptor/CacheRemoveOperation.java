package org.springframework.cache.jcache.interceptor;

import javax.cache.annotation.CacheMethodDetails;
import javax.cache.annotation.CacheRemove;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.ExceptionTypeFilter;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/interceptor/CacheRemoveOperation.class */
class CacheRemoveOperation extends AbstractJCacheKeyOperation<CacheRemove> {
    private final ExceptionTypeFilter exceptionTypeFilter;

    public CacheRemoveOperation(CacheMethodDetails<CacheRemove> methodDetails, CacheResolver cacheResolver, KeyGenerator keyGenerator) {
        super(methodDetails, cacheResolver, keyGenerator);
        CacheRemove ann = methodDetails.getCacheAnnotation();
        this.exceptionTypeFilter = createExceptionTypeFilter(ann.evictFor(), ann.noEvictFor());
    }

    @Override // org.springframework.cache.jcache.interceptor.AbstractJCacheOperation
    public ExceptionTypeFilter getExceptionTypeFilter() {
        return this.exceptionTypeFilter;
    }

    public boolean isEarlyRemove() {
        return !((CacheRemove) getCacheAnnotation()).afterInvocation();
    }
}
