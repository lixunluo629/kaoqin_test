package org.hibernate.validator.internal.metadata.aggregated.rule;

import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/rule/OverridingMethodMustNotAlterParameterConstraints.class */
public class OverridingMethodMustNotAlterParameterConstraints extends MethodConfigurationRule {
    @Override // org.hibernate.validator.internal.metadata.aggregated.rule.MethodConfigurationRule
    public void apply(ConstrainedExecutable method, ConstrainedExecutable otherMethod) {
        if (isDefinedOnSubType(method, otherMethod) && otherMethod.hasParameterConstraints() && !method.isEquallyParameterConstrained(otherMethod)) {
            throw log.getParameterConfigurationAlteredInSubTypeException(method.getLocation().getMember(), otherMethod.getLocation().getMember());
        }
    }
}
