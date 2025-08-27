package org.springframework.cache.jcache.interceptor;

import java.lang.annotation.Annotation;
import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKeyInvocationContext;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/interceptor/DefaultCacheKeyInvocationContext.class */
class DefaultCacheKeyInvocationContext<A extends Annotation> extends DefaultCacheInvocationContext<A> implements CacheKeyInvocationContext<A> {
    private final CacheInvocationParameter[] keyParameters;
    private final CacheInvocationParameter valueParameter;

    public DefaultCacheKeyInvocationContext(AbstractJCacheKeyOperation<A> operation, Object target, Object[] args) {
        super(operation, target, args);
        this.keyParameters = operation.getKeyParameters(args);
        if (operation instanceof CachePutOperation) {
            this.valueParameter = ((CachePutOperation) operation).getValueParameter(args);
        } else {
            this.valueParameter = null;
        }
    }

    public CacheInvocationParameter[] getKeyParameters() {
        return (CacheInvocationParameter[]) this.keyParameters.clone();
    }

    public CacheInvocationParameter getValueParameter() {
        return this.valueParameter;
    }
}
