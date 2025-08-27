package org.hibernate.validator.internal.util.privilegedactions;

import java.lang.reflect.Method;
import java.security.PrivilegedAction;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/GetDeclaredMethods.class */
public final class GetDeclaredMethods implements PrivilegedAction<Method[]> {
    private final Class<?> clazz;

    public static GetDeclaredMethods action(Class<?> clazz) {
        return new GetDeclaredMethods(clazz);
    }

    private GetDeclaredMethods(Class<?> clazz) {
        this.clazz = clazz;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public Method[] run() {
        return this.clazz.getDeclaredMethods();
    }
}
