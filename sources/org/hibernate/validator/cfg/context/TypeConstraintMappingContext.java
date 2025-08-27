package org.hibernate.validator.cfg.context;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/context/TypeConstraintMappingContext.class */
public interface TypeConstraintMappingContext<C> extends Constrainable<TypeConstraintMappingContext<C>>, ConstraintMappingTarget, PropertyTarget, MethodTarget, ConstructorTarget, AnnotationProcessingOptions<TypeConstraintMappingContext<C>>, AnnotationIgnoreOptions<TypeConstraintMappingContext<C>> {
    TypeConstraintMappingContext<C> ignoreAllAnnotations();

    TypeConstraintMappingContext<C> defaultGroupSequence(Class<?>... clsArr);

    TypeConstraintMappingContext<C> defaultGroupSequenceProviderClass(Class<? extends DefaultGroupSequenceProvider<? super C>> cls);
}
