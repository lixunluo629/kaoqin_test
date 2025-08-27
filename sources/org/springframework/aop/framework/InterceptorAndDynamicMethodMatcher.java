package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.MethodMatcher;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/InterceptorAndDynamicMethodMatcher.class */
class InterceptorAndDynamicMethodMatcher {
    final MethodInterceptor interceptor;
    final MethodMatcher methodMatcher;

    public InterceptorAndDynamicMethodMatcher(MethodInterceptor interceptor, MethodMatcher methodMatcher) {
        this.interceptor = interceptor;
        this.methodMatcher = methodMatcher;
    }
}
