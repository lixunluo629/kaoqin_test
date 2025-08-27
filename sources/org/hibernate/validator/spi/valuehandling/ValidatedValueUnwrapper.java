package org.hibernate.validator.spi.valuehandling;

import java.lang.reflect.Type;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/spi/valuehandling/ValidatedValueUnwrapper.class */
public abstract class ValidatedValueUnwrapper<T> {
    public abstract Object handleValidatedValue(T t);

    public abstract Type getValidatedValueType(Type type);
}
