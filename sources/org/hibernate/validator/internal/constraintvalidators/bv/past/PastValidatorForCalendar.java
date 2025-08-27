package org.hibernate.validator.internal.constraintvalidators.bv.past;

import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Past;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/past/PastValidatorForCalendar.class */
public class PastValidatorForCalendar implements ConstraintValidator<Past, Calendar> {
    @Override // javax.validation.ConstraintValidator
    public void initialize(Past constraintAnnotation) {
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Calendar cal, ConstraintValidatorContext constraintValidatorContext) {
        if (cal == null) {
            return true;
        }
        TimeProvider timeProvider = ((HibernateConstraintValidatorContext) constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)).getTimeProvider();
        long now = timeProvider.getCurrentTime();
        return cal.getTimeInMillis() < now;
    }
}
