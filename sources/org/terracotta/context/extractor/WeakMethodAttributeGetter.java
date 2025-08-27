package org.terracotta.context.extractor;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extractor/WeakMethodAttributeGetter.class */
class WeakMethodAttributeGetter<T> extends MethodAttributeGetter<T> {
    private final WeakReference<Object> targetRef;

    WeakMethodAttributeGetter(Object target, Method method) {
        super(method);
        this.targetRef = new WeakReference<>(target);
    }

    @Override // org.terracotta.context.extractor.MethodAttributeGetter
    Object target() {
        return this.targetRef.get();
    }
}
