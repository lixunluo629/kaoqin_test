package org.hibernate.validator.internal.util.privilegedactions;

import java.lang.reflect.Method;
import java.security.PrivilegedAction;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/GetDeclaredMethod.class */
public final class GetDeclaredMethod implements PrivilegedAction<Method> {
    private final Class<?> clazz;
    private final String methodName;
    private final Class<?>[] parameterTypes;

    public static GetDeclaredMethod action(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        return new GetDeclaredMethod(clazz, methodName, parameterTypes);
    }

    private GetDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public Method run() {
        try {
            return this.clazz.getDeclaredMethod(this.methodName, this.parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
