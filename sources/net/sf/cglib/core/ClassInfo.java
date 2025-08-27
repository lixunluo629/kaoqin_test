package net.sf.cglib.core;

import org.objectweb.asm.Type;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/ClassInfo.class */
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
