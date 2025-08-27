package org.hibernate.validator.internal.cfg.context;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Constraint;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;
import org.hibernate.validator.internal.engine.constraintdefinition.ConstraintDefinitionContribution;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.logging.Messages;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/DefaultConstraintMapping.class */
public class DefaultConstraintMapping implements ConstraintMapping {
    private static final Log log = LoggerFactory.make();
    private final AnnotationProcessingOptionsImpl annotationProcessingOptions = new AnnotationProcessingOptionsImpl();
    private final Set<Class<?>> configuredTypes = CollectionHelper.newHashSet();
    private final Set<TypeConstraintMappingContextImpl<?>> typeContexts = CollectionHelper.newHashSet();
    private final Set<Class<?>> definedConstraints = CollectionHelper.newHashSet();
    private final Set<ConstraintDefinitionContextImpl<?>> constraintContexts = CollectionHelper.newHashSet();

    @Override // org.hibernate.validator.cfg.ConstraintMapping
    public final <C> TypeConstraintMappingContext<C> type(Class<C> type) {
        Contracts.assertNotNull(type, Messages.MESSAGES.beanTypeMustNotBeNull());
        if (this.configuredTypes.contains(type)) {
            throw log.getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException(type.getName());
        }
        TypeConstraintMappingContextImpl<?> typeConstraintMappingContextImpl = new TypeConstraintMappingContextImpl<>(this, type);
        this.typeContexts.add(typeConstraintMappingContextImpl);
        this.configuredTypes.add(type);
        return typeConstraintMappingContextImpl;
    }

    public final AnnotationProcessingOptionsImpl getAnnotationProcessingOptions() {
        return this.annotationProcessingOptions;
    }

    public Set<Class<?>> getConfiguredTypes() {
        return this.configuredTypes;
    }

    public Set<BeanConfiguration<?>> getBeanConfigurations(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider) {
        Set<BeanConfiguration<?>> configurations = CollectionHelper.newHashSet();
        for (TypeConstraintMappingContextImpl<?> typeContext : this.typeContexts) {
            configurations.add(typeContext.build(constraintHelper, parameterNameProvider));
        }
        return configurations;
    }

    @Override // org.hibernate.validator.cfg.ConstraintMapping
    public <A extends Annotation> ConstraintDefinitionContext<A> constraintDefinition(Class<A> annotationClass) {
        Contracts.assertNotNull(annotationClass, Messages.MESSAGES.annotationTypeMustNotBeNull());
        Contracts.assertTrue(annotationClass.isAnnotationPresent(Constraint.class), Messages.MESSAGES.annotationTypeMustBeAnnotatedWithConstraint());
        if (this.definedConstraints.contains(annotationClass)) {
            throw log.getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException(annotationClass.getName());
        }
        ConstraintDefinitionContextImpl<?> constraintDefinitionContextImpl = new ConstraintDefinitionContextImpl<>(this, annotationClass);
        this.constraintContexts.add(constraintDefinitionContextImpl);
        this.definedConstraints.add(annotationClass);
        return constraintDefinitionContextImpl;
    }

    public Set<ConstraintDefinitionContribution<?>> getConstraintDefinitionContributions() {
        HashSet hashSetNewHashSet = CollectionHelper.newHashSet();
        for (ConstraintDefinitionContextImpl<?> constraintContext : this.constraintContexts) {
            hashSetNewHashSet.add(constraintContext.build());
        }
        return hashSetNewHashSet;
    }
}
