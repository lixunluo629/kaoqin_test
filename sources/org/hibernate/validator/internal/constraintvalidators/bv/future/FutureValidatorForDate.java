package org.hibernate.validator.internal.constraintvalidators.bv.future;

import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Future;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/future/FutureValidatorForDate.class */
public class FutureValidatorForDate implements ConstraintValidator<Future, Date> {
    @Override // javax.validation.ConstraintValidator
    public void initialize(Future constraintAnnotation) {
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return true;
        }
        TimeProvider timeProvider = ((HibernateConstraintValidatorContext) constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)).getTimeProvider();
        long now = timeProvider.getCurrentTime();
        return date.getTime() > now;
    }
}
