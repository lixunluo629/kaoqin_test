package org.hibernate.validator.internal.constraintvalidators.bv;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.DecimalMax;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/DecimalMaxValidatorForNumber.class */
public class DecimalMaxValidatorForNumber implements ConstraintValidator<DecimalMax, Number> {
    private static final Log log = LoggerFactory.make();
    private BigDecimal maxValue;
    private boolean inclusive;

    @Override // javax.validation.ConstraintValidator
    public void initialize(DecimalMax maxValue) {
        try {
            this.maxValue = new BigDecimal(maxValue.value());
            this.inclusive = maxValue.inclusive();
        } catch (NumberFormatException nfe) {
            throw log.getInvalidBigDecimalFormatException(maxValue.value(), nfe);
        }
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Number value, ConstraintValidatorContext constraintValidatorContext) {
        int comparisonResult;
        if (value == null) {
            return true;
        }
        if (value instanceof Double) {
            if (((Double) value).doubleValue() == Double.NEGATIVE_INFINITY) {
                return true;
            }
            if (Double.isNaN(((Double) value).doubleValue()) || ((Double) value).doubleValue() == Double.POSITIVE_INFINITY) {
                return false;
            }
        } else if (value instanceof Float) {
            if (((Float) value).floatValue() == Float.NEGATIVE_INFINITY) {
                return true;
            }
            if (Float.isNaN(((Float) value).floatValue()) || ((Float) value).floatValue() == Float.POSITIVE_INFINITY) {
                return false;
            }
        }
        if (value instanceof BigDecimal) {
            comparisonResult = ((BigDecimal) value).compareTo(this.maxValue);
        } else if (value instanceof BigInteger) {
            comparisonResult = new BigDecimal((BigInteger) value).compareTo(this.maxValue);
        } else if (value instanceof Long) {
            comparisonResult = BigDecimal.valueOf(value.longValue()).compareTo(this.maxValue);
        } else {
            comparisonResult = BigDecimal.valueOf(value.doubleValue()).compareTo(this.maxValue);
        }
        return this.inclusive ? comparisonResult <= 0 : comparisonResult < 0;
    }
}
