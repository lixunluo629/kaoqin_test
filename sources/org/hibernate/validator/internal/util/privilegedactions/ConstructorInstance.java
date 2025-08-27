package org.hibernate.validator.internal.util.privilegedactions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivilegedAction;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/ConstructorInstance.class */
public final class ConstructorInstance<T> implements PrivilegedAction<T> {
    private static final Log log = LoggerFactory.make();
    private final Constructor<T> constructor;
    private final Object[] initArgs;

    public static <T> ConstructorInstance<T> action(Constructor<T> constructor, Object... initArgs) {
        return new ConstructorInstance<>(constructor, initArgs);
    }

    private ConstructorInstance(Constructor<T> constructor, Object... initArgs) {
        this.constructor = constructor;
        this.initArgs = initArgs;
    }

    @Override // java.security.PrivilegedAction
    public T run() {
        try {
            return this.constructor.newInstance(this.initArgs);
        } catch (IllegalAccessException e) {
            throw log.getUnableToInstantiateException(this.constructor.getName(), e);
        } catch (InstantiationException e2) {
            throw log.getUnableToInstantiateException(this.constructor.getName(), e2);
        } catch (RuntimeException e3) {
            throw log.getUnableToInstantiateException(this.constructor.getName(), e3);
        } catch (InvocationTargetException e4) {
            throw log.getUnableToInstantiateException(this.constructor.getName(), e4);
        }
    }
}
