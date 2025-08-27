package org.hibernate.validator;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/HibernateValidatorContext.class */
public interface HibernateValidatorContext extends ValidatorContext {
    @Override // javax.validation.ValidatorContext
    HibernateValidatorContext messageInterpolator(MessageInterpolator messageInterpolator);

    @Override // javax.validation.ValidatorContext
    HibernateValidatorContext traversableResolver(TraversableResolver traversableResolver);

    @Override // javax.validation.ValidatorContext
    HibernateValidatorContext constraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory);

    @Override // javax.validation.ValidatorContext
    HibernateValidatorContext parameterNameProvider(ParameterNameProvider parameterNameProvider);

    HibernateValidatorContext failFast(boolean z);

    HibernateValidatorContext addValidationValueHandler(ValidatedValueUnwrapper<?> validatedValueUnwrapper);

    HibernateValidatorContext timeProvider(TimeProvider timeProvider);

    HibernateValidatorContext allowOverridingMethodAlterParameterConstraint(boolean z);

    HibernateValidatorContext allowMultipleCascadedValidationOnReturnValues(boolean z);

    HibernateValidatorContext allowParallelMethodsDefineParameterConstraints(boolean z);
}
