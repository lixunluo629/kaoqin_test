package org.springframework.aop;

import java.lang.reflect.Method;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/MethodMatcher.class */
public interface MethodMatcher {
    public static final MethodMatcher TRUE = TrueMethodMatcher.INSTANCE;

    boolean matches(Method method, Class<?> cls);

    boolean isRuntime();

    boolean matches(Method method, Class<?> cls, Object... objArr);
}
