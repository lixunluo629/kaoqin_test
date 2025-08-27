package org.hibernate.validator.internal.metadata.aggregated.rule;

import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/rule/VoidMethodsMustNotBeReturnValueConstrained.class */
public class VoidMethodsMustNotBeReturnValueConstrained extends MethodConfigurationRule {
    @Override // org.hibernate.validator.internal.metadata.aggregated.rule.MethodConfigurationRule
    public void apply(ConstrainedExecutable method, ConstrainedExecutable otherMethod) {
        if (method.getExecutable().getReturnType() == Void.TYPE) {
            if (!method.getConstraints().isEmpty() || method.isCascading()) {
                throw log.getVoidMethodsMustNotBeConstrainedException(method.getLocation().getMember());
            }
        }
    }
}
