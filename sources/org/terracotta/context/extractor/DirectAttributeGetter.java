package org.terracotta.context.extractor;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extractor/DirectAttributeGetter.class */
class DirectAttributeGetter<T> implements AttributeGetter<T> {
    private final T object;

    DirectAttributeGetter(T object) {
        this.object = object;
    }

    @Override // org.terracotta.context.extractor.AttributeGetter
    public T get() {
        return this.object;
    }
}
