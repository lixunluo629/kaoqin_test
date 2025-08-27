package org.springframework.cache.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheOperationSourcePointcut.class */
abstract class CacheOperationSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {
    protected abstract CacheOperationSource getCacheOperationSource();

    CacheOperationSourcePointcut() {
    }

    @Override // org.springframework.aop.MethodMatcher
    public boolean matches(Method method, Class<?> targetClass) {
        CacheOperationSource cas = getCacheOperationSource();
        return (cas == null || CollectionUtils.isEmpty(cas.getCacheOperations(method, targetClass))) ? false : true;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CacheOperationSourcePointcut)) {
            return false;
        }
        CacheOperationSourcePointcut otherPc = (CacheOperationSourcePointcut) other;
        return ObjectUtils.nullSafeEquals(getCacheOperationSource(), otherPc.getCacheOperationSource());
    }

    public int hashCode() {
        return CacheOperationSourcePointcut.class.hashCode();
    }

    public String toString() {
        return getClass().getName() + ": " + getCacheOperationSource();
    }
}
