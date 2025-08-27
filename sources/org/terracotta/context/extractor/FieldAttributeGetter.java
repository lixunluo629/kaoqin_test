package org.terracotta.context.extractor;

import java.lang.reflect.Field;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extractor/FieldAttributeGetter.class */
abstract class FieldAttributeGetter<T> implements AttributeGetter<T> {
    private final Field field;

    abstract Object target();

    FieldAttributeGetter(Field field) {
        field.setAccessible(true);
        this.field = field;
    }

    @Override // org.terracotta.context.extractor.AttributeGetter
    public T get() {
        try {
            return (T) this.field.get(target());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e2) {
            throw new RuntimeException(e2);
        }
    }
}
