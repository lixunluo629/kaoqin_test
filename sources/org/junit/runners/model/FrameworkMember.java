package org.junit.runners.model;

import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.runners.model.FrameworkMember;

/* loaded from: junit-4.12.jar:org/junit/runners/model/FrameworkMember.class */
public abstract class FrameworkMember<T extends FrameworkMember<T>> implements Annotatable {
    abstract boolean isShadowedBy(T t);

    protected abstract int getModifiers();

    public abstract String getName();

    public abstract Class<?> getType();

    public abstract Class<?> getDeclaringClass();

    boolean isShadowedBy(List<T> members) {
        for (T each : members) {
            if (isShadowedBy((FrameworkMember<T>) each)) {
                return true;
            }
        }
        return false;
    }

    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }
}
