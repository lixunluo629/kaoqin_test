package org.hibernate.validator.internal.metadata.aggregated.rule;

import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/rule/ParallelMethodsMustNotDefineParameterConstraints.class */
public class ParallelMethodsMustNotDefineParameterConstraints extends MethodConfigurationRule {
    @Override // org.hibernate.validator.internal.metadata.aggregated.rule.MethodConfigurationRule
    public void apply(ConstrainedExecutable method, ConstrainedExecutable otherMethod) {
        if (isDefinedOnParallelType(method, otherMethod)) {
            if (method.hasParameterConstraints() || otherMethod.hasParameterConstraints()) {
                throw log.getParameterConstraintsDefinedInMethodsFromParallelTypesException(method.getLocation().getMember(), otherMethod.getLocation().getMember());
            }
        }
    }
}
