package org.hibernate.validator.internal.constraintvalidators.bv.size;

import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Size;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/size/SizeValidatorForMap.class */
public class SizeValidatorForMap implements ConstraintValidator<Size, Map<?, ?>> {
    private static final Log log = LoggerFactory.make();
    private int min;
    private int max;

    @Override // javax.validation.ConstraintValidator
    public void initialize(Size parameters) {
        this.min = parameters.min();
        this.max = parameters.max();
        validateParameters();
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Map<?, ?> map, ConstraintValidatorContext constraintValidatorContext) {
        if (map == null) {
            return true;
        }
        int size = map.size();
        return size >= this.min && size <= this.max;
    }

    private void validateParameters() {
        if (this.min < 0) {
            throw log.getMaxCannotBeNegativeException();
        }
        if (this.max < 0) {
            throw log.getMaxCannotBeNegativeException();
        }
        if (this.max < this.min) {
            throw log.getLengthCannotBeNegativeException();
        }
    }
}
