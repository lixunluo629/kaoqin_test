package org.terracotta.context.extractor;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extractor/WeakFieldAttributeGetter.class */
public class WeakFieldAttributeGetter<T> extends FieldAttributeGetter<T> {
    private final WeakReference<Object> targetRef;

    @Override // org.terracotta.context.extractor.FieldAttributeGetter, org.terracotta.context.extractor.AttributeGetter
    public /* bridge */ /* synthetic */ Object get() {
        return super.get();
    }

    public WeakFieldAttributeGetter(Object target, Field field) {
        super(field);
        this.targetRef = new WeakReference<>(target);
    }

    @Override // org.terracotta.context.extractor.FieldAttributeGetter
    Object target() {
        return this.targetRef.get();
    }
}
