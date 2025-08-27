package org.hibernate.validator.internal.engine;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.validator.internal.metadata.aggregated.rule.MethodConfigurationRule;
import org.hibernate.validator.internal.metadata.aggregated.rule.OverridingMethodMustNotAlterParameterConstraints;
import org.hibernate.validator.internal.metadata.aggregated.rule.ParallelMethodsMustNotDefineGroupConversionForCascadedReturnValue;
import org.hibernate.validator.internal.metadata.aggregated.rule.ParallelMethodsMustNotDefineParameterConstraints;
import org.hibernate.validator.internal.metadata.aggregated.rule.ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine;
import org.hibernate.validator.internal.metadata.aggregated.rule.VoidMethodsMustNotBeReturnValueConstrained;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/MethodValidationConfiguration.class */
public class MethodValidationConfiguration {
    private boolean allowOverridingMethodAlterParameterConstraint = false;
    private boolean allowMultipleCascadedValidationOnReturnValues = false;
    private boolean allowParallelMethodsDefineParameterConstraints = false;

    public MethodValidationConfiguration allowOverridingMethodAlterParameterConstraint(boolean allow) {
        this.allowOverridingMethodAlterParameterConstraint = allow;
        return this;
    }

    public MethodValidationConfiguration allowMultipleCascadedValidationOnReturnValues(boolean allow) {
        this.allowMultipleCascadedValidationOnReturnValues = allow;
        return this;
    }

    public MethodValidationConfiguration allowParallelMethodsDefineParameterConstraints(boolean allow) {
        this.allowParallelMethodsDefineParameterConstraints = allow;
        return this;
    }

    public boolean isAllowOverridingMethodAlterParameterConstraint() {
        return this.allowOverridingMethodAlterParameterConstraint;
    }

    public boolean isAllowMultipleCascadedValidationOnReturnValues() {
        return this.allowMultipleCascadedValidationOnReturnValues;
    }

    public boolean isAllowParallelMethodsDefineParameterConstraints() {
        return this.allowParallelMethodsDefineParameterConstraints;
    }

    public Set<MethodConfigurationRule> getConfiguredRuleSet() {
        HashSet<MethodConfigurationRule> result = CollectionHelper.newHashSet();
        if (!isAllowOverridingMethodAlterParameterConstraint()) {
            result.add(new OverridingMethodMustNotAlterParameterConstraints());
        }
        if (!isAllowParallelMethodsDefineParameterConstraints()) {
            result.add(new ParallelMethodsMustNotDefineParameterConstraints());
        }
        result.add(new VoidMethodsMustNotBeReturnValueConstrained());
        if (!isAllowMultipleCascadedValidationOnReturnValues()) {
            result.add(new ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine());
        }
        result.add(new ParallelMethodsMustNotDefineGroupConversionForCascadedReturnValue());
        return Collections.unmodifiableSet(result);
    }
}
