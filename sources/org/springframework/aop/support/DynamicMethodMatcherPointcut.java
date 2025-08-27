package org.springframework.aop.support;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/support/DynamicMethodMatcherPointcut.class */
public abstract class DynamicMethodMatcherPointcut extends DynamicMethodMatcher implements Pointcut {
    @Override // org.springframework.aop.Pointcut
    public ClassFilter getClassFilter() {
        return ClassFilter.TRUE;
    }

    @Override // org.springframework.aop.Pointcut
    public final MethodMatcher getMethodMatcher() {
        return this;
    }
}
