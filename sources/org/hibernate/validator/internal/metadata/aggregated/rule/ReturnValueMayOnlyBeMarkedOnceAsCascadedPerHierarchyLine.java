package org.hibernate.validator.internal.metadata.aggregated.rule;

import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/rule/ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine.class */
public class ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine extends MethodConfigurationRule {
    @Override // org.hibernate.validator.internal.metadata.aggregated.rule.MethodConfigurationRule
    public void apply(ConstrainedExecutable method, ConstrainedExecutable otherMethod) {
        if (method.isCascading() && otherMethod.isCascading()) {
            if (isDefinedOnSubType(method, otherMethod) || isDefinedOnSubType(otherMethod, method)) {
                throw log.getMethodReturnValueMustNotBeMarkedMoreThanOnceForCascadedValidationException(method.getLocation().getMember(), otherMethod.getLocation().getMember());
            }
        }
    }
}
