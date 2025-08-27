package javax.validation;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ValidatorFactory.class */
public interface ValidatorFactory {
    Validator getValidator();

    ValidatorContext usingContext();

    MessageInterpolator getMessageInterpolator();

    TraversableResolver getTraversableResolver();

    ConstraintValidatorFactory getConstraintValidatorFactory();

    ParameterNameProvider getParameterNameProvider();

    <T> T unwrap(Class<T> cls);

    void close();
}
