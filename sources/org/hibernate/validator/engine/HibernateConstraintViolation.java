package org.hibernate.validator.engine;

import javax.validation.ConstraintViolation;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/engine/HibernateConstraintViolation.class */
public interface HibernateConstraintViolation<T> extends ConstraintViolation<T> {
    <C> C getDynamicPayload(Class<C> cls);
}
