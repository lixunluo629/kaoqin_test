package org.springframework.aop;

import java.io.Serializable;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/TrueClassFilter.class */
class TrueClassFilter implements ClassFilter, Serializable {
    public static final TrueClassFilter INSTANCE = new TrueClassFilter();

    private TrueClassFilter() {
    }

    @Override // org.springframework.aop.ClassFilter
    public boolean matches(Class<?> clazz) {
        return true;
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public String toString() {
        return "ClassFilter.TRUE";
    }
}
