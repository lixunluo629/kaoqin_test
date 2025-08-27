package org.hibernate.validator.internal.constraintvalidators.bv.future;

import java.time.OffsetDateTime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Future;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
import org.hibernate.validator.spi.time.TimeProvider;

@IgnoreJava6Requirement
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/future/FutureValidatorForOffsetDateTime.class */
public class FutureValidatorForOffsetDateTime implements ConstraintValidator<Future, OffsetDateTime> {
    @Override // javax.validation.ConstraintValidator
    public void initialize(Future constraintAnnotation) {
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(OffsetDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        TimeProvider timeProvider = ((HibernateConstraintValidatorContext) context.unwrap(HibernateConstraintValidatorContext.class)).getTimeProvider();
        long now = timeProvider.getCurrentTime();
        return value.toInstant().toEpochMilli() > now;
    }
}
