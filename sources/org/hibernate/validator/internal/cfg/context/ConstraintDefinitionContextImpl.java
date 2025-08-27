package org.hibernate.validator.internal.cfg.context;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.validation.ConstraintValidator;
import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
import org.hibernate.validator.internal.engine.constraintdefinition.ConstraintDefinitionContribution;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/ConstraintDefinitionContextImpl.class */
class ConstraintDefinitionContextImpl<A extends Annotation> extends ConstraintContextImplBase implements ConstraintDefinitionContext<A> {
    private final Class<A> annotationType;
    private boolean includeExistingValidators;
    private final Set<Class<? extends ConstraintValidator<A, ?>>> validatorTypes;

    ConstraintDefinitionContextImpl(DefaultConstraintMapping mapping, Class<A> annotationType) {
        super(mapping);
        this.includeExistingValidators = true;
        this.validatorTypes = CollectionHelper.newHashSet();
        this.annotationType = annotationType;
    }

    @Override // org.hibernate.validator.cfg.context.ConstraintDefinitionContext
    public ConstraintDefinitionContext<A> includeExistingValidators(boolean includeExistingValidators) {
        this.includeExistingValidators = includeExistingValidators;
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.ConstraintDefinitionContext
    public ConstraintDefinitionContext<A> validatedBy(Class<? extends ConstraintValidator<A, ?>> validator) {
        this.validatorTypes.add(validator);
        return this;
    }

    ConstraintDefinitionContribution<A> build() {
        return new ConstraintDefinitionContribution<>(this.annotationType, CollectionHelper.newArrayList(this.validatorTypes), this.includeExistingValidators);
    }
}
