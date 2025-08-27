package org.springframework.cache.jcache.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.util.ClassUtils;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/interceptor/AbstractFallbackJCacheOperationSource.class */
public abstract class AbstractFallbackJCacheOperationSource implements JCacheOperationSource {
    private static final Object NULL_CACHING_ATTRIBUTE = new Object();
    protected final Log logger = LogFactory.getLog(getClass());
    private final Map<MethodClassKey, Object> cache = new ConcurrentHashMap(1024);

    protected abstract JCacheOperation<?> findCacheOperation(Method method, Class<?> cls);

    @Override // org.springframework.cache.jcache.interceptor.JCacheOperationSource
    public JCacheOperation<?> getCacheOperation(Method method, Class<?> targetClass) throws SecurityException, IllegalArgumentException {
        MethodClassKey cacheKey = new MethodClassKey(method, targetClass);
        Object cached = this.cache.get(cacheKey);
        if (cached != null) {
            if (cached != NULL_CACHING_ATTRIBUTE) {
                return (JCacheOperation) cached;
            }
            return null;
        }
        JCacheOperation<?> operation = computeCacheOperation(method, targetClass);
        if (operation != null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Adding cacheable method '" + method.getName() + "' with operation: " + operation);
            }
            this.cache.put(cacheKey, operation);
        } else {
            this.cache.put(cacheKey, NULL_CACHING_ATTRIBUTE);
        }
        return operation;
    }

    private JCacheOperation<?> computeCacheOperation(Method method, Class<?> targetClass) throws SecurityException, IllegalArgumentException {
        JCacheOperation<?> operation;
        if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        Method specificMethod = BridgeMethodResolver.findBridgedMethod(ClassUtils.getMostSpecificMethod(method, targetClass));
        JCacheOperation<?> operation2 = findCacheOperation(specificMethod, targetClass);
        if (operation2 != null) {
            return operation2;
        }
        if (specificMethod != method && (operation = findCacheOperation(method, targetClass)) != null) {
            return operation;
        }
        return null;
    }

    protected boolean allowPublicMethodsOnly() {
        return false;
    }
}
