package org.hibernate.validator.internal.cfg.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.cfg.context.CrossParameterConstraintMappingContext;
import org.hibernate.validator.cfg.context.ParameterConstraintMappingContext;
import org.hibernate.validator.cfg.context.ReturnValueConstraintMappingContext;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.ReflectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/ExecutableConstraintMappingContextImpl.class */
abstract class ExecutableConstraintMappingContextImpl {
    private static final Log log = LoggerFactory.make();
    protected final TypeConstraintMappingContextImpl<?> typeContext;
    protected final ExecutableElement executable;
    private final ParameterConstraintMappingContextImpl[] parameterContexts;
    private ReturnValueConstraintMappingContextImpl returnValueContext;
    private CrossParameterConstraintMappingContextImpl crossParameterContext;

    <T> ExecutableConstraintMappingContextImpl(TypeConstraintMappingContextImpl<T> typeContext, Constructor<T> constructor) {
        this((TypeConstraintMappingContextImpl<?>) typeContext, ExecutableElement.forConstructor(constructor));
    }

    ExecutableConstraintMappingContextImpl(TypeConstraintMappingContextImpl<?> typeContext, Method method) {
        this(typeContext, ExecutableElement.forMethod(method));
    }

    private ExecutableConstraintMappingContextImpl(TypeConstraintMappingContextImpl<?> typeContext, ExecutableElement executable) {
        this.typeContext = typeContext;
        this.executable = executable;
        this.parameterContexts = new ParameterConstraintMappingContextImpl[executable.getParameterTypes().length];
    }

    public ParameterConstraintMappingContext parameter(int index) {
        if (index < 0 || index >= this.executable.getParameterTypes().length) {
            throw log.getInvalidExecutableParameterIndexException(this.executable.getAsString(), index);
        }
        if (this.parameterContexts[index] != null) {
            throw log.getParameterHasAlreadyBeConfiguredViaProgrammaticApiException(this.typeContext.getBeanClass().getName(), this.executable.getAsString(), index);
        }
        ParameterConstraintMappingContextImpl context = new ParameterConstraintMappingContextImpl(this, index);
        this.parameterContexts[index] = context;
        return context;
    }

    public CrossParameterConstraintMappingContext crossParameter() {
        if (this.crossParameterContext != null) {
            throw log.getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException(this.typeContext.getBeanClass().getName(), this.executable.getAsString());
        }
        this.crossParameterContext = new CrossParameterConstraintMappingContextImpl(this);
        return this.crossParameterContext;
    }

    public ReturnValueConstraintMappingContext returnValue() {
        if (this.returnValueContext != null) {
            throw log.getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException(this.typeContext.getBeanClass().getName(), this.executable.getAsString());
        }
        this.returnValueContext = new ReturnValueConstraintMappingContextImpl(this);
        return this.returnValueContext;
    }

    public ExecutableElement getExecutable() {
        return this.executable;
    }

    public TypeConstraintMappingContextImpl<?> getTypeContext() {
        return this.typeContext;
    }

    public ConstrainedElement build(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider) {
        return new ConstrainedExecutable(ConfigurationSource.API, ConstraintLocation.forReturnValue(this.executable), getParameters(constraintHelper, parameterNameProvider), this.crossParameterContext != null ? this.crossParameterContext.getConstraints(constraintHelper) : Collections.emptySet(), this.returnValueContext != null ? this.returnValueContext.getConstraints(constraintHelper) : Collections.emptySet(), Collections.emptySet(), this.returnValueContext != null ? this.returnValueContext.getGroupConversions() : Collections.emptyMap(), this.returnValueContext != null ? this.returnValueContext.isCascading() : false, this.returnValueContext != null ? this.returnValueContext.unwrapMode() : UnwrapMode.AUTOMATIC);
    }

    private List<ConstrainedParameter> getParameters(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider) {
        List<ConstrainedParameter> constrainedParameters = CollectionHelper.newArrayList();
        for (int i = 0; i < this.parameterContexts.length; i++) {
            ParameterConstraintMappingContextImpl parameter = this.parameterContexts[i];
            if (parameter != null) {
                constrainedParameters.add(parameter.build(constraintHelper, parameterNameProvider));
            } else {
                constrainedParameters.add(new ConstrainedParameter(ConfigurationSource.API, ConstraintLocation.forParameter(this.executable, i), ReflectionHelper.typeOf(this.executable, i), i, this.executable.getParameterNames(parameterNameProvider).get(i)));
            }
        }
        return constrainedParameters;
    }
}
