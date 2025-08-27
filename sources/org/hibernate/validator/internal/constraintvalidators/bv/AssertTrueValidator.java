package org.hibernate.validator.internal.constraintvalidators.bv;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.AssertTrue;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/AssertTrueValidator.class */
public class AssertTrueValidator implements ConstraintValidator<AssertTrue, Boolean> {
    @Override // javax.validation.ConstraintValidator
    public void initialize(AssertTrue constraintAnnotation) {
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Boolean bool, ConstraintValidatorContext constraintValidatorContext) {
        return bool == null || bool.booleanValue();
    }
}
