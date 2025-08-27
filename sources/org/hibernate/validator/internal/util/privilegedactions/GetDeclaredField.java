package org.hibernate.validator.internal.util.privilegedactions;

import java.lang.reflect.Field;
import java.security.PrivilegedAction;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/GetDeclaredField.class */
public final class GetDeclaredField implements PrivilegedAction<Field> {
    private final Class<?> clazz;
    private final String fieldName;

    public static GetDeclaredField action(Class<?> clazz, String fieldName) {
        return new GetDeclaredField(clazz, fieldName);
    }

    private GetDeclaredField(Class<?> clazz, String fieldName) {
        this.clazz = clazz;
        this.fieldName = fieldName;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public Field run() throws NoSuchFieldException {
        try {
            Field field = this.clazz.getDeclaredField(this.fieldName);
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
