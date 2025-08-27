package org.hibernate.validator.internal.constraintvalidators.bv.past;

import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Past;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/constraintvalidators/bv/past/PastValidatorForDate.class */
public class PastValidatorForDate implements ConstraintValidator<Past, Date> {
    @Override // javax.validation.ConstraintValidator
    public void initialize(Past constraintAnnotation) {
    }

    @Override // javax.validation.ConstraintValidator
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return true;
        }
        TimeProvider timeProvider = ((HibernateConstraintValidatorContext) constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)).getTimeProvider();
        long now = timeProvider.getCurrentTime();
        return date.getTime() < now;
    }
}
