package org.terracotta.context.extractor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extractor/MethodAttributeGetter.class */
abstract class MethodAttributeGetter<T> implements AttributeGetter<T> {
    private final Method method;

    abstract Object target();

    MethodAttributeGetter(Method method) {
        method.setAccessible(true);
        this.method = method;
    }

    @Override // org.terracotta.context.extractor.AttributeGetter
    public T get() {
        try {
            return (T) this.method.invoke(target(), new Object[0]);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        }
    }
}
