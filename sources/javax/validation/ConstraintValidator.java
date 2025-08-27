package javax.validation;

import java.lang.annotation.Annotation;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ConstraintValidator.class */
public interface ConstraintValidator<A extends Annotation, T> {
    void initialize(A a);

    boolean isValid(T t, ConstraintValidatorContext constraintValidatorContext);
}
