package org.hibernate.validator.internal.constraintvalidators.bv;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.AssertFalse;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/AssertFalseValidator.class */
public class AssertFalseValidator implements ConstraintValidator<AssertFalse, Boolean> {
    @Override // javax.validation.ConstraintValidator
    public void initialize(AssertFalse constraintAnnotation) {
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Boolean bool, ConstraintValidatorContext constraintValidatorContext) {
        return bool == null || !bool.booleanValue();
    }
}
