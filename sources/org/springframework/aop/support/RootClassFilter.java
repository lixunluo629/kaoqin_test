package org.springframework.aop.support;

import java.io.Serializable;
import org.springframework.aop.ClassFilter;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/support/RootClassFilter.class */
public class RootClassFilter implements ClassFilter, Serializable {
    private Class<?> clazz;

    public RootClassFilter(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override // org.springframework.aop.ClassFilter
    public boolean matches(Class<?> candidate) {
        return this.clazz.isAssignableFrom(candidate);
    }
}
