package org.springframework.aop;

import java.lang.reflect.Method;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/IntroductionAwareMethodMatcher.class */
public interface IntroductionAwareMethodMatcher extends MethodMatcher {
    boolean matches(Method method, Class<?> cls, boolean z);
}
