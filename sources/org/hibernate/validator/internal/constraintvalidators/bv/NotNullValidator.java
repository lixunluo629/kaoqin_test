package org.hibernate.validator.internal.constraintvalidators.bv;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/NotNullValidator.class */
public class NotNullValidator implements ConstraintValidator<NotNull, Object> {
    @Override // javax.validation.ConstraintValidator
    public void initialize(NotNull parameters) {
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        return object != null;
    }
}
