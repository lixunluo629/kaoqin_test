package org.hibernate.validator.cfg.context;

import java.lang.annotation.Annotation;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/context/ConstraintDefinitionTarget.class */
public interface ConstraintDefinitionTarget {
    <A extends Annotation> ConstraintDefinitionContext<A> constraintDefinition(Class<A> cls);
}
