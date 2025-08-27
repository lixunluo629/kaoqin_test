package org.terracotta.context.query;

import java.lang.reflect.Method;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/query/Matcher.class */
public abstract class Matcher<T> {
    private Class<? extends T> boundType = (Class<? extends T>) getSafeType(getClass());

    protected abstract boolean matchesSafely(T t);

    private static <T extends Matcher<?>> Class<?> getSafeType(Class<T> fromClass) throws SecurityException {
        Class<T> superclass = fromClass;
        while (true) {
            Class<T> cls = superclass;
            if (cls != Object.class) {
                Method[] arr$ = cls.getDeclaredMethods();
                for (Method method : arr$) {
                    if (method.getName().equals("matchesSafely") && method.getParameterTypes().length == 1 && !method.isSynthetic()) {
                        return method.getParameterTypes()[0];
                    }
                }
                superclass = cls.getSuperclass();
            } else {
                throw new AssertionError();
            }
        }
    }

    public final boolean matches(Object object) {
        if (this.boundType.isAssignableFrom(object.getClass())) {
            return matchesSafely(this.boundType.cast(object));
        }
        return false;
    }
}
