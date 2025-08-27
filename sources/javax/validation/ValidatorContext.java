package javax.validation;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ValidatorContext.class */
public interface ValidatorContext {
    ValidatorContext messageInterpolator(MessageInterpolator messageInterpolator);

    ValidatorContext traversableResolver(TraversableResolver traversableResolver);

    ValidatorContext constraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory);

    ValidatorContext parameterNameProvider(ParameterNameProvider parameterNameProvider);

    Validator getValidator();
}
