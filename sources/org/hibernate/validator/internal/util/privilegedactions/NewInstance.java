package org.hibernate.validator.internal.util.privilegedactions;

import java.security.PrivilegedAction;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/NewInstance.class */
public final class NewInstance<T> implements PrivilegedAction<T> {
    private static final Log log = LoggerFactory.make();
    private final Class<T> clazz;
    private final String message;

    public static <T> NewInstance<T> action(Class<T> clazz, String message) {
        return new NewInstance<>(clazz, message);
    }

    private NewInstance(Class<T> clazz, String message) {
        this.clazz = clazz;
        this.message = message;
    }

    @Override // java.security.PrivilegedAction
    public T run() {
        try {
            return this.clazz.newInstance();
        } catch (IllegalAccessException e) {
            throw log.getUnableToInstantiateException((Class<?>) this.clazz, (Exception) e);
        } catch (InstantiationException e2) {
            throw log.getUnableToInstantiateException(this.message, this.clazz, e2);
        } catch (RuntimeException e3) {
            throw log.getUnableToInstantiateException((Class<?>) this.clazz, (Exception) e3);
        }
    }
}
