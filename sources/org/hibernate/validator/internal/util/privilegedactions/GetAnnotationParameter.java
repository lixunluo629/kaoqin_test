package org.hibernate.validator.internal.util.privilegedactions;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivilegedAction;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/GetAnnotationParameter.class */
public final class GetAnnotationParameter<T> implements PrivilegedAction<T> {
    private static final Log log = LoggerFactory.make();
    private final Annotation annotation;
    private final String parameterName;
    private final Class<T> type;

    public static <T> GetAnnotationParameter<T> action(Annotation annotation, String parameterName, Class<T> type) {
        return new GetAnnotationParameter<>(annotation, parameterName, type);
    }

    private GetAnnotationParameter(Annotation annotation, String parameterName, Class<T> type) {
        this.annotation = annotation;
        this.parameterName = parameterName;
        this.type = type;
    }

    @Override // java.security.PrivilegedAction
    public T run() throws NoSuchMethodException, SecurityException {
        try {
            Method method = this.annotation.getClass().getMethod(this.parameterName, new Class[0]);
            method.setAccessible(true);
            T t = (T) method.invoke(this.annotation, new Object[0]);
            if (this.type.isAssignableFrom(t.getClass())) {
                return t;
            }
            throw log.getWrongParameterTypeException(this.type.getName(), t.getClass().getName());
        } catch (IllegalAccessException e) {
            throw log.getUnableToGetAnnotationParameterException(this.parameterName, this.annotation.getClass().getName(), e);
        } catch (NoSuchMethodException e2) {
            throw log.getUnableToFindAnnotationParameterException(this.parameterName, e2);
        } catch (InvocationTargetException e3) {
            throw log.getUnableToGetAnnotationParameterException(this.parameterName, this.annotation.getClass().getName(), e3);
        }
    }
}
