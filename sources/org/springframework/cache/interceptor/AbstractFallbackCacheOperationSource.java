package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.util.ClassUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/AbstractFallbackCacheOperationSource.class */
public abstract class AbstractFallbackCacheOperationSource implements CacheOperationSource {
    private static final Collection<CacheOperation> NULL_CACHING_ATTRIBUTE = Collections.emptyList();
    protected final Log logger = LogFactory.getLog(getClass());
    private final Map<Object, Collection<CacheOperation>> attributeCache = new ConcurrentHashMap(1024);

    protected abstract Collection<CacheOperation> findCacheOperations(Class<?> cls);

    protected abstract Collection<CacheOperation> findCacheOperations(Method method);

    @Override // org.springframework.cache.interceptor.CacheOperationSource
    public Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass) throws SecurityException, IllegalArgumentException {
        if (method.getDeclaringClass() == Object.class) {
            return null;
        }
        Object cacheKey = getCacheKey(method, targetClass);
        Collection<CacheOperation> cached = this.attributeCache.get(cacheKey);
        if (cached != null) {
            if (cached != NULL_CACHING_ATTRIBUTE) {
                return cached;
            }
            return null;
        }
        Collection<CacheOperation> cacheOps = computeCacheOperations(method, targetClass);
        if (cacheOps != null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Adding cacheable method '" + method.getName() + "' with attribute: " + cacheOps);
            }
            this.attributeCache.put(cacheKey, cacheOps);
        } else {
            this.attributeCache.put(cacheKey, NULL_CACHING_ATTRIBUTE);
        }
        return cacheOps;
    }

    protected Object getCacheKey(Method method, Class<?> targetClass) {
        return new MethodClassKey(method, targetClass);
    }

    private Collection<CacheOperation> computeCacheOperations(Method method, Class<?> targetClass) throws SecurityException, IllegalArgumentException {
        if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        Method specificMethod = BridgeMethodResolver.findBridgedMethod(ClassUtils.getMostSpecificMethod(method, targetClass));
        Collection<CacheOperation> opDef = findCacheOperations(specificMethod);
        if (opDef != null) {
            return opDef;
        }
        Collection<CacheOperation> opDef2 = findCacheOperations(specificMethod.getDeclaringClass());
        if (opDef2 != null && ClassUtils.isUserLevelMethod(method)) {
            return opDef2;
        }
        if (specificMethod != method) {
            Collection<CacheOperation> opDef3 = findCacheOperations(method);
            if (opDef3 != null) {
                return opDef3;
            }
            Collection<CacheOperation> opDef4 = findCacheOperations(method.getDeclaringClass());
            if (opDef4 != null && ClassUtils.isUserLevelMethod(method)) {
                return opDef4;
            }
            return null;
        }
        return null;
    }

    protected boolean allowPublicMethodsOnly() {
        return false;
    }
}
