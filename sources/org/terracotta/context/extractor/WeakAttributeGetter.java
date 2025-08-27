package org.terracotta.context.extractor;

import java.lang.ref.WeakReference;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extractor/WeakAttributeGetter.class */
class WeakAttributeGetter<T> implements AttributeGetter<T> {
    private final WeakReference<T> reference;

    public WeakAttributeGetter(T object) {
        this.reference = new WeakReference<>(object);
    }

    @Override // org.terracotta.context.extractor.AttributeGetter
    public T get() {
        return this.reference.get();
    }
}
