package org.springframework.cglib.core;

import org.springframework.asm.Type;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/core/MethodInfo.class */
public abstract class MethodInfo {
    public abstract ClassInfo getClassInfo();

    public abstract int getModifiers();

    public abstract Signature getSignature();

    public abstract Type[] getExceptionTypes();

    protected MethodInfo() {
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof MethodInfo)) {
            return false;
        }
        return getSignature().equals(((MethodInfo) o).getSignature());
    }

    public int hashCode() {
        return getSignature().hashCode();
    }

    public String toString() {
        return getSignature().toString();
    }
}
