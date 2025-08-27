package org.hibernate.validator.internal.cfg.context;

import java.lang.annotation.Annotation;
import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/ConstraintContextImplBase.class */
abstract class ConstraintContextImplBase {
    protected final DefaultConstraintMapping mapping;

    public ConstraintContextImplBase(DefaultConstraintMapping mapping) {
        this.mapping = mapping;
    }

    public <C> TypeConstraintMappingContext<C> type(Class<C> type) {
        return this.mapping.type(type);
    }

    public <A extends Annotation> ConstraintDefinitionContext<A> constraintDefinition(Class<A> annotationClass) {
        return this.mapping.constraintDefinition(annotationClass);
    }
}
