package org.hibernate.validator.internal.constraintvalidators.hv;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/hv/LengthValidator.class */
public class LengthValidator implements ConstraintValidator<Length, CharSequence> {
    private static final Log log = LoggerFactory.make();
    private int min;
    private int max;

    @Override // javax.validation.ConstraintValidator
    public void initialize(Length parameters) {
        this.min = parameters.min();
        this.max = parameters.max();
        validateParameters();
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        int length = value.length();
        return length >= this.min && length <= this.max;
    }

    private void validateParameters() {
        if (this.min < 0) {
            throw log.getMinCannotBeNegativeException();
        }
        if (this.max < 0) {
            throw log.getMaxCannotBeNegativeException();
        }
        if (this.max < this.min) {
            throw log.getLengthCannotBeNegativeException();
        }
    }
}
