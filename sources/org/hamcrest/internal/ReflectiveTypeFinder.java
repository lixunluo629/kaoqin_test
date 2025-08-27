package org.hamcrest.internal;

import java.lang.reflect.Method;

/* loaded from: hamcrest-core-1.3.jar:org/hamcrest/internal/ReflectiveTypeFinder.class */
public class ReflectiveTypeFinder {
    private final String methodName;
    private final int expectedNumberOfParameters;
    private final int typedParameter;

    public ReflectiveTypeFinder(String methodName, int expectedNumberOfParameters, int typedParameter) {
        this.methodName = methodName;
        this.expectedNumberOfParameters = expectedNumberOfParameters;
        this.typedParameter = typedParameter;
    }

    public Class<?> findExpectedType(Class<?> fromClass) throws SecurityException {
        Class<?> superclass = fromClass;
        while (true) {
            Class<?> c = superclass;
            if (c != Object.class) {
                Method[] arr$ = c.getDeclaredMethods();
                for (Method method : arr$) {
                    if (canObtainExpectedTypeFrom(method)) {
                        return expectedTypeFrom(method);
                    }
                }
                superclass = c.getSuperclass();
            } else {
                throw new Error("Cannot determine correct type for " + this.methodName + "() method.");
            }
        }
    }

    protected boolean canObtainExpectedTypeFrom(Method method) {
        return method.getName().equals(this.methodName) && method.getParameterTypes().length == this.expectedNumberOfParameters && !method.isSynthetic();
    }

    protected Class<?> expectedTypeFrom(Method method) {
        return method.getParameterTypes()[this.typedParameter];
    }
}
