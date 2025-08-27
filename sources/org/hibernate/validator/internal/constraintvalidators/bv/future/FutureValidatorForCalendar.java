package org.hibernate.validator.internal.constraintvalidators.bv.future;

import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Future;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/future/FutureValidatorForCalendar.class */
public class FutureValidatorForCalendar implements ConstraintValidator<Future, Calendar> {
    @Override // javax.validation.ConstraintValidator
    public void initialize(Future constraintAnnotation) {
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Calendar cal, ConstraintValidatorContext context) {
        if (cal == null) {
            return true;
        }
        TimeProvider timeProvider = ((HibernateConstraintValidatorContext) context.unwrap(HibernateConstraintValidatorContext.class)).getTimeProvider();
        long now = timeProvider.getCurrentTime();
        return cal.getTimeInMillis() > now;
    }
}
