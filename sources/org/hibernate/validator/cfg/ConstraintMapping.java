package org.hibernate.validator.cfg;

import java.lang.annotation.Annotation;
import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/ConstraintMapping.class */
public interface ConstraintMapping {
    <C> TypeConstraintMappingContext<C> type(Class<C> cls);

    <A extends Annotation> ConstraintDefinitionContext<A> constraintDefinition(Class<A> cls);
}
