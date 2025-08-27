package org.aspectj.internal.lang.reflect;

import org.aspectj.lang.reflect.TypePattern;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/internal/lang/reflect/TypePatternImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/internal/lang/reflect/TypePatternImpl.class */
public class TypePatternImpl implements TypePattern {
    private String typePattern;

    public TypePatternImpl(String pattern) {
        this.typePattern = pattern;
    }

    @Override // org.aspectj.lang.reflect.TypePattern
    public String asString() {
        return this.typePattern;
    }

    public String toString() {
        return asString();
    }
}
