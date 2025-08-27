package org.hibernate.validator.internal.constraintvalidators.bv;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Min;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/MinValidatorForNumber.class */
public class MinValidatorForNumber implements ConstraintValidator<Min, Number> {
    private long minValue;

    @Override // javax.validation.ConstraintValidator
    public void initialize(Min minValue) {
        this.minValue = minValue.value();
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Number value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        if (value instanceof Double) {
            if (((Double) value).doubleValue() == Double.POSITIVE_INFINITY) {
                return true;
            }
            if (Double.isNaN(((Double) value).doubleValue()) || ((Double) value).doubleValue() == Double.NEGATIVE_INFINITY) {
                return false;
            }
        } else if (value instanceof Float) {
            if (((Float) value).floatValue() == Float.POSITIVE_INFINITY) {
                return true;
            }
            if (Float.isNaN(((Float) value).floatValue()) || ((Float) value).floatValue() == Float.NEGATIVE_INFINITY) {
                return false;
            }
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(BigDecimal.valueOf(this.minValue)) != -1;
        }
        if (value instanceof BigInteger) {
            return ((BigInteger) value).compareTo(BigInteger.valueOf(this.minValue)) != -1;
        }
        long longValue = value.longValue();
        return longValue >= this.minValue;
    }
}
