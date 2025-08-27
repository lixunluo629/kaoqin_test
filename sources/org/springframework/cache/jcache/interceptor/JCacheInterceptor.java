package org.springframework.cache.jcache.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.interceptor.CacheOperationInvoker;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/interceptor/JCacheInterceptor.class */
public class JCacheInterceptor extends JCacheAspectSupport implements MethodInterceptor, Serializable {
    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        CacheOperationInvoker aopAllianceInvoker = new CacheOperationInvoker() { // from class: org.springframework.cache.jcache.interceptor.JCacheInterceptor.1
            @Override // org.springframework.cache.interceptor.CacheOperationInvoker
            public Object invoke() {
                try {
                    return invocation.proceed();
                } catch (Throwable ex) {
                    throw new CacheOperationInvoker.ThrowableWrapper(ex);
                }
            }
        };
        try {
            return execute(aopAllianceInvoker, invocation.getThis(), method, invocation.getArguments());
        } catch (CacheOperationInvoker.ThrowableWrapper th) {
            throw th.getOriginal();
        }
    }
}
