package org.springframework.cglib.core;

import org.springframework.asm.Type;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/core/ClassInfo.class */
public abstract class ClassInfo {
    public abstract Type getType();

    public abstract Type getSuperType();

    public abstract Type[] getInterfaces();

    public abstract int getModifiers();

    protected ClassInfo() {
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ClassInfo)) {
            return false;
        }
        return getType().equals(((ClassInfo) o).getType());
    }

    public int hashCode() {
        return getType().hashCode();
    }

    public String toString() {
        return getType().getClassName();
    }
}
