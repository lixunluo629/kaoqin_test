package org.hibernate.validator.internal.engine;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.Validator;
import org.hibernate.validator.HibernateValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/ValidatorContextImpl.class */
public class ValidatorContextImpl implements HibernateValidatorContext {
    private final ValidatorFactoryImpl validatorFactory;
    private MessageInterpolator messageInterpolator;
    private TraversableResolver traversableResolver;
    private ConstraintValidatorFactory constraintValidatorFactory;
    private ParameterNameProvider parameterNameProvider;
    private boolean failFast;
    private final List<ValidatedValueUnwrapper<?>> validatedValueHandlers;
    private TimeProvider timeProvider;
    private final MethodValidationConfiguration methodValidationConfiguration = new MethodValidationConfiguration();

    public ValidatorContextImpl(ValidatorFactoryImpl validatorFactory) {
        this.validatorFactory = validatorFactory;
        this.messageInterpolator = validatorFactory.getMessageInterpolator();
        this.traversableResolver = validatorFactory.getTraversableResolver();
        this.constraintValidatorFactory = validatorFactory.getConstraintValidatorFactory();
        this.parameterNameProvider = validatorFactory.getParameterNameProvider();
        this.failFast = validatorFactory.isFailFast();
        this.validatedValueHandlers = new ArrayList(validatorFactory.getValidatedValueHandlers());
        this.timeProvider = validatorFactory.getTimeProvider();
    }

    @Override // javax.validation.ValidatorContext
    public HibernateValidatorContext messageInterpolator(MessageInterpolator messageInterpolator) {
        if (messageInterpolator == null) {
            this.messageInterpolator = this.validatorFactory.getMessageInterpolator();
        } else {
            this.messageInterpolator = messageInterpolator;
        }
        return this;
    }

    @Override // javax.validation.ValidatorContext
    public HibernateValidatorContext traversableResolver(TraversableResolver traversableResolver) {
        if (traversableResolver == null) {
            this.traversableResolver = this.validatorFactory.getTraversableResolver();
        } else {
            this.traversableResolver = traversableResolver;
        }
        return this;
    }

    @Override // javax.validation.ValidatorContext
    public HibernateValidatorContext constraintValidatorFactory(ConstraintValidatorFactory factory) {
        if (factory == null) {
            this.constraintValidatorFactory = this.validatorFactory.getConstraintValidatorFactory();
        } else {
            this.constraintValidatorFactory = factory;
        }
        return this;
    }

    @Override // javax.validation.ValidatorContext
    public HibernateValidatorContext parameterNameProvider(ParameterNameProvider parameterNameProvider) {
        if (parameterNameProvider == null) {
            this.parameterNameProvider = this.validatorFactory.getParameterNameProvider();
        } else {
            this.parameterNameProvider = parameterNameProvider;
        }
        return this;
    }

    @Override // org.hibernate.validator.HibernateValidatorContext
    public HibernateValidatorContext failFast(boolean failFast) {
        this.failFast = failFast;
        return this;
    }

    @Override // org.hibernate.validator.HibernateValidatorContext
    public HibernateValidatorContext addValidationValueHandler(ValidatedValueUnwrapper<?> handler) {
        this.validatedValueHandlers.add(handler);
        return this;
    }

    @Override // org.hibernate.validator.HibernateValidatorContext
    public HibernateValidatorContext timeProvider(TimeProvider timeProvider) {
        if (timeProvider == null) {
            this.timeProvider = this.validatorFactory.getTimeProvider();
        } else {
            this.timeProvider = timeProvider;
        }
        return this;
    }

    @Override // org.hibernate.validator.HibernateValidatorContext
    public HibernateValidatorContext allowOverridingMethodAlterParameterConstraint(boolean allow) {
        this.methodValidationConfiguration.allowOverridingMethodAlterParameterConstraint(allow);
        return this;
    }

    @Override // org.hibernate.validator.HibernateValidatorContext
    public HibernateValidatorContext allowMultipleCascadedValidationOnReturnValues(boolean allow) {
        this.methodValidationConfiguration.allowMultipleCascadedValidationOnReturnValues(allow);
        return this;
    }

    @Override // org.hibernate.validator.HibernateValidatorContext
    public HibernateValidatorContext allowParallelMethodsDefineParameterConstraints(boolean allow) {
        this.methodValidationConfiguration.allowParallelMethodsDefineParameterConstraints(allow);
        return this;
    }

    @Override // javax.validation.ValidatorContext
    public Validator getValidator() {
        return this.validatorFactory.createValidator(this.constraintValidatorFactory, this.messageInterpolator, this.traversableResolver, this.parameterNameProvider, this.failFast, this.validatedValueHandlers, this.timeProvider, this.methodValidationConfiguration);
    }
}
