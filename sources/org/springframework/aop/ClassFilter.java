package org.springframework.aop;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/ClassFilter.class */
public interface ClassFilter {
    public static final ClassFilter TRUE = TrueClassFilter.INSTANCE;

    boolean matches(Class<?> cls);
}
