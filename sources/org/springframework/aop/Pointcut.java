package org.springframework.aop;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/Pointcut.class */
public interface Pointcut {
    public static final Pointcut TRUE = TruePointcut.INSTANCE;

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();
}
