package org.hibernate.validator.internal.constraintvalidators.bv.size;

import javax.validation.constraints.Size;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/size/SizeValidatorForArraysOfPrimitives.class */
public abstract class SizeValidatorForArraysOfPrimitives {
    private static final Log log = LoggerFactory.make();
    protected int min;
    protected int max;

    public void initialize(Size parameters) {
        this.min = parameters.min();
        this.max = parameters.max();
        validateParameters();
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
