package org.hibernate.validator.internal.engine.constraintdefinition;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintdefinition/ConstraintDefinitionContribution.class */
public class ConstraintDefinitionContribution<A extends Annotation> {
    private final Class<A> constraintType;
    private final List<Class<? extends ConstraintValidator<A, ?>>> constraintValidators = new ArrayList();
    private final boolean includeExisting;

    public ConstraintDefinitionContribution(Class<A> constraintType, List<Class<? extends ConstraintValidator<A, ?>>> constraintValidators, boolean includeExisting) {
        this.constraintType = constraintType;
        this.constraintValidators.addAll(constraintValidators);
        this.includeExisting = includeExisting;
    }

    public Class<A> getConstraintType() {
        return this.constraintType;
    }

    public List<Class<? extends ConstraintValidator<A, ?>>> getConstraintValidators() {
        return this.constraintValidators;
    }

    public boolean includeExisting() {
        return this.includeExisting;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConstraintDefinitionContribution<?> that = (ConstraintDefinitionContribution) o;
        if (!this.constraintType.equals(that.constraintType) || !this.constraintValidators.equals(that.constraintValidators)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.constraintType.hashCode();
        return (31 * result) + this.constraintValidators.hashCode();
    }

    public String toString() {
        return "ConstraintDefinitionContribution{constraintType=" + this.constraintType + ", constraintValidators=" + this.constraintValidators + ", includeExisting=" + this.includeExisting + '}';
    }
}
