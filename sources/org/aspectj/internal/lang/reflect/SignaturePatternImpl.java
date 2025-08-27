package org.aspectj.internal.lang.reflect;

import org.aspectj.lang.reflect.SignaturePattern;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/internal/lang/reflect/SignaturePatternImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/internal/lang/reflect/SignaturePatternImpl.class */
public class SignaturePatternImpl implements SignaturePattern {
    private String sigPattern;

    public SignaturePatternImpl(String pattern) {
        this.sigPattern = pattern;
    }

    @Override // org.aspectj.lang.reflect.SignaturePattern
    public String asString() {
        return this.sigPattern;
    }

    public String toString() {
        return asString();
    }
}
