package org.hibernate.validator.internal.cfg.context;

import java.util.Collections;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.cfg.context.Constrainable;
import org.hibernate.validator.cfg.context.ConstructorConstraintMappingContext;
import org.hibernate.validator.cfg.context.CrossParameterConstraintMappingContext;
import org.hibernate.validator.cfg.context.MethodConstraintMappingContext;
import org.hibernate.validator.cfg.context.ParameterConstraintMappingContext;
import org.hibernate.validator.cfg.context.ReturnValueConstraintMappingContext;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
import org.hibernate.validator.internal.util.ReflectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/ParameterConstraintMappingContextImpl.class */
final class ParameterConstraintMappingContextImpl extends CascadableConstraintMappingContextImplBase<ParameterConstraintMappingContext> implements ParameterConstraintMappingContext {
    private final ExecutableConstraintMappingContextImpl executableContext;
    private final int parameterIndex;

    @Override // org.hibernate.validator.cfg.context.Constrainable
    public /* bridge */ /* synthetic */ Constrainable constraint(ConstraintDef constraintDef) {
        return constraint((ConstraintDef<?, ?>) constraintDef);
    }

    ParameterConstraintMappingContextImpl(ExecutableConstraintMappingContextImpl executableContext, int parameterIndex) {
        super(executableContext.getTypeContext().getConstraintMapping());
        this.executableContext = executableContext;
        this.parameterIndex = parameterIndex;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.hibernate.validator.internal.cfg.context.CascadableConstraintMappingContextImplBase
    public ParameterConstraintMappingContext getThis() {
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.Constrainable
    public ParameterConstraintMappingContext constraint(ConstraintDef<?, ?> definition) {
        super.addConstraint(ConfiguredConstraint.forParameter(definition, this.executableContext.getExecutable(), this.parameterIndex));
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.AnnotationIgnoreOptions
    public ParameterConstraintMappingContext ignoreAnnotations(boolean ignoreAnnotations) {
        this.mapping.getAnnotationProcessingOptions().ignoreConstraintAnnotationsOnParameter(this.executableContext.getExecutable().getMember(), this.parameterIndex, Boolean.valueOf(ignoreAnnotations));
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.ParameterTarget
    public ParameterConstraintMappingContext parameter(int index) {
        return this.executableContext.parameter(index);
    }

    @Override // org.hibernate.validator.cfg.context.CrossParameterTarget
    public CrossParameterConstraintMappingContext crossParameter() {
        return this.executableContext.crossParameter();
    }

    @Override // org.hibernate.validator.cfg.context.ReturnValueTarget
    public ReturnValueConstraintMappingContext returnValue() {
        return this.executableContext.returnValue();
    }

    @Override // org.hibernate.validator.cfg.context.ConstructorTarget
    public ConstructorConstraintMappingContext constructor(Class<?>... parameterTypes) {
        return this.executableContext.getTypeContext().constructor(parameterTypes);
    }

    @Override // org.hibernate.validator.cfg.context.MethodTarget
    public MethodConstraintMappingContext method(String name, Class<?>... parameterTypes) {
        return this.executableContext.getTypeContext().method(name, parameterTypes);
    }

    public ConstrainedParameter build(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider) {
        return new ConstrainedParameter(ConfigurationSource.API, ConstraintLocation.forParameter(this.executableContext.getExecutable(), this.parameterIndex), ReflectionHelper.typeOf(this.executableContext.getExecutable(), this.parameterIndex), this.parameterIndex, this.executableContext.getExecutable().getParameterNames(parameterNameProvider).get(this.parameterIndex), getConstraints(constraintHelper), Collections.emptySet(), this.groupConversions, this.isCascading, unwrapMode());
    }

    @Override // org.hibernate.validator.internal.cfg.context.ConstraintMappingContextImplBase
    protected ConstraintDescriptorImpl.ConstraintType getConstraintType() {
        return ConstraintDescriptorImpl.ConstraintType.GENERIC;
    }
}
