package org.hibernate.validator.constraintvalidation;

import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraintvalidation/HibernateConstraintValidatorContext.class */
public interface HibernateConstraintValidatorContext extends ConstraintValidatorContext {
    HibernateConstraintValidatorContext addExpressionVariable(String str, Object obj);

    TimeProvider getTimeProvider();

    HibernateConstraintValidatorContext withDynamicPayload(Object obj);
}
