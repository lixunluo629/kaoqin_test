package org.hibernate.validator.cfg.context;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/context/ConstraintDefinitionContext.class */
public interface ConstraintDefinitionContext<A extends Annotation> extends ConstraintMappingTarget {
    ConstraintDefinitionContext<A> includeExistingValidators(boolean z);

    ConstraintDefinitionContext<A> validatedBy(Class<? extends ConstraintValidator<A, ?>> cls);
}
