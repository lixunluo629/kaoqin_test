package org.hibernate.validator.internal.cfg.context;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.cfg.context.Constrainable;
import org.hibernate.validator.cfg.context.ConstructorConstraintMappingContext;
import org.hibernate.validator.cfg.context.MethodConstraintMappingContext;
import org.hibernate.validator.cfg.context.PropertyConstraintMappingContext;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/PropertyConstraintMappingContextImpl.class */
final class PropertyConstraintMappingContextImpl extends CascadableConstraintMappingContextImplBase<PropertyConstraintMappingContext> implements PropertyConstraintMappingContext {
    private final TypeConstraintMappingContextImpl<?> typeContext;
    private final Member member;

    @Override // org.hibernate.validator.cfg.context.Constrainable
    public /* bridge */ /* synthetic */ Constrainable constraint(ConstraintDef constraintDef) {
        return constraint((ConstraintDef<?, ?>) constraintDef);
    }

    PropertyConstraintMappingContextImpl(TypeConstraintMappingContextImpl<?> typeContext, Member member) {
        super(typeContext.getConstraintMapping());
        this.typeContext = typeContext;
        this.member = member;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.hibernate.validator.internal.cfg.context.CascadableConstraintMappingContextImplBase
    public PropertyConstraintMappingContextImpl getThis() {
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.Constrainable
    public PropertyConstraintMappingContext constraint(ConstraintDef<?, ?> definition) {
        if (this.member instanceof Field) {
            super.addConstraint(ConfiguredConstraint.forProperty(definition, this.member));
        } else {
            super.addConstraint(ConfiguredConstraint.forReturnValue(definition, ExecutableElement.forMethod((Method) this.member)));
        }
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.AnnotationProcessingOptions
    public PropertyConstraintMappingContext ignoreAnnotations() {
        return ignoreAnnotations(true);
    }

    @Override // org.hibernate.validator.cfg.context.AnnotationIgnoreOptions
    public PropertyConstraintMappingContext ignoreAnnotations(boolean ignoreAnnotations) {
        this.mapping.getAnnotationProcessingOptions().ignoreConstraintAnnotationsOnMember(this.member, Boolean.valueOf(ignoreAnnotations));
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.PropertyTarget
    public PropertyConstraintMappingContext property(String property, ElementType elementType) {
        return this.typeContext.property(property, elementType);
    }

    @Override // org.hibernate.validator.cfg.context.ConstructorTarget
    public ConstructorConstraintMappingContext constructor(Class<?>... parameterTypes) {
        return this.typeContext.constructor(parameterTypes);
    }

    @Override // org.hibernate.validator.cfg.context.MethodTarget
    public MethodConstraintMappingContext method(String name, Class<?>... parameterTypes) {
        return this.typeContext.method(name, parameterTypes);
    }

    ConstrainedElement build(ConstraintHelper constraintHelper) {
        if (this.member instanceof Field) {
            return new ConstrainedField(ConfigurationSource.API, ConstraintLocation.forProperty(this.member), getConstraints(constraintHelper), Collections.emptySet(), this.groupConversions, this.isCascading, unwrapMode());
        }
        return new ConstrainedExecutable(ConfigurationSource.API, ConstraintLocation.forProperty(this.member), getConstraints(constraintHelper), this.groupConversions, this.isCascading, unwrapMode());
    }

    @Override // org.hibernate.validator.internal.cfg.context.ConstraintMappingContextImplBase
    protected ConstraintDescriptorImpl.ConstraintType getConstraintType() {
        return ConstraintDescriptorImpl.ConstraintType.GENERIC;
    }
}
