package org.hibernate.validator.internal.metadata.aggregated.rule;

import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/rule/ParallelMethodsMustNotDefineGroupConversionForCascadedReturnValue.class */
public class ParallelMethodsMustNotDefineGroupConversionForCascadedReturnValue extends MethodConfigurationRule {
    @Override // org.hibernate.validator.internal.metadata.aggregated.rule.MethodConfigurationRule
    public void apply(ConstrainedExecutable method, ConstrainedExecutable otherMethod) {
        boolean isCascaded = method.isCascading() || otherMethod.isCascading();
        boolean hasGroupConversions = (method.getGroupConversions().isEmpty() && otherMethod.getGroupConversions().isEmpty()) ? false : true;
        if (isDefinedOnParallelType(method, otherMethod) && isCascaded && hasGroupConversions) {
            throw log.getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException(method.getLocation().getMember(), otherMethod.getLocation().getMember());
        }
    }
}
