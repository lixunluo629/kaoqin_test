package org.hibernate.validator.internal.constraintvalidators.bv.future;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Future;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
import org.hibernate.validator.spi.time.TimeProvider;
import org.joda.time.ReadableInstant;

@IgnoreJava6Requirement
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/future/FutureValidatorForReadableInstant.class */
public class FutureValidatorForReadableInstant implements ConstraintValidator<Future, ReadableInstant> {
    @Override // javax.validation.ConstraintValidator
    public void initialize(Future constraintAnnotation) {
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(ReadableInstant value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        TimeProvider timeProvider = ((HibernateConstraintValidatorContext) context.unwrap(HibernateConstraintValidatorContext.class)).getTimeProvider();
        long now = timeProvider.getCurrentTime();
        return value.getMillis() > now;
    }
}
