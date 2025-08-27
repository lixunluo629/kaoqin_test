package org.hibernate.validator.internal.constraintvalidators.bv.past;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Past;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
import org.hibernate.validator.spi.time.TimeProvider;
import org.joda.time.ReadableInstant;

@IgnoreJava6Requirement
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/past/PastValidatorForReadableInstant.class */
public class PastValidatorForReadableInstant implements ConstraintValidator<Past, ReadableInstant> {
    @Override // javax.validation.ConstraintValidator
    public void initialize(Past constraintAnnotation) {
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(ReadableInstant value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        TimeProvider timeProvider = ((HibernateConstraintValidatorContext) context.unwrap(HibernateConstraintValidatorContext.class)).getTimeProvider();
        long now = timeProvider.getCurrentTime();
        return value.getMillis() < now;
    }
}
