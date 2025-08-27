package javax.validation;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ConstraintValidatorFactory.class */
public interface ConstraintValidatorFactory {
    <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> cls);

    void releaseInstance(ConstraintValidator<?, ?> constraintValidator);
}
