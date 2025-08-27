package org.springframework.expression.spel.support;

import java.lang.reflect.Method;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/support/DataBindingPropertyAccessor.class */
public class DataBindingPropertyAccessor extends ReflectivePropertyAccessor {
    private DataBindingPropertyAccessor(boolean allowWrite) {
        super(allowWrite);
    }

    @Override // org.springframework.expression.spel.support.ReflectivePropertyAccessor
    protected boolean isCandidateForProperty(Method method, Class<?> targetClass) {
        Class<?> clazz = method.getDeclaringClass();
        return (clazz == Object.class || clazz == Class.class || ClassLoader.class.isAssignableFrom(targetClass)) ? false : true;
    }

    public static DataBindingPropertyAccessor forReadOnlyAccess() {
        return new DataBindingPropertyAccessor(false);
    }

    public static DataBindingPropertyAccessor forReadWriteAccess() {
        return new DataBindingPropertyAccessor(true);
    }
}
