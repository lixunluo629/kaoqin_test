package org.hibernate.validator.internal.metadata.aggregated;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ElementKind;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import org.hibernate.validator.internal.engine.MethodValidationConfiguration;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.aggregated.ParameterMetaData;
import org.hibernate.validator.internal.metadata.aggregated.rule.MethodConfigurationRule;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.ExecutableDescriptorImpl;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.ExecutableHelper;
import org.hibernate.validator.internal.util.ReflectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/ExecutableMetaData.class */
public class ExecutableMetaData extends AbstractConstraintMetaData {
    private static final Log log = LoggerFactory.make();
    private final Class<?>[] parameterTypes;
    private final List<ParameterMetaData> parameterMetaDataList;
    private final Set<MetaConstraint<?>> crossParameterConstraints;
    private final boolean isGetter;
    private final Set<String> signatures;
    private final ReturnValueMetaData returnValueMetaData;

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public /* bridge */ /* synthetic */ ElementDescriptor asDescriptor(boolean z, List list) {
        return asDescriptor(z, (List<Class<?>>) list);
    }

    private ExecutableMetaData(String name, Type returnType, Class<?>[] parameterTypes, ElementKind kind, Set<String> signatures, Set<MetaConstraint<?>> returnValueConstraints, List<ParameterMetaData> parameterMetaData, Set<MetaConstraint<?>> crossParameterConstraints, Set<MetaConstraint<?>> typeArgumentsConstraints, Map<Class<?>, Class<?>> returnValueGroupConversions, boolean isCascading, boolean isConstrained, boolean isGetter, UnwrapMode unwrapMode) {
        super(name, returnType, returnValueConstraints, kind, isCascading, isConstrained, unwrapMode);
        this.parameterTypes = parameterTypes;
        this.parameterMetaDataList = Collections.unmodifiableList(parameterMetaData);
        this.crossParameterConstraints = Collections.unmodifiableSet(crossParameterConstraints);
        this.signatures = signatures;
        this.returnValueMetaData = new ReturnValueMetaData(returnType, returnValueConstraints, typeArgumentsConstraints, isCascading, returnValueGroupConversions, unwrapMode);
        this.isGetter = isGetter;
    }

    public ParameterMetaData getParameterMetaData(int parameterIndex) {
        if (parameterIndex < 0 || parameterIndex > this.parameterMetaDataList.size() - 1) {
            throw log.getInvalidExecutableParameterIndexException(ExecutableElement.getExecutableAsString(getType().toString() + "#" + getName(), this.parameterTypes), this.parameterTypes.length);
        }
        return this.parameterMetaDataList.get(parameterIndex);
    }

    public Class<?>[] getParameterTypes() {
        return this.parameterTypes;
    }

    public Set<String> getSignatures() {
        return this.signatures;
    }

    public Set<MetaConstraint<?>> getCrossParameterConstraints() {
        return this.crossParameterConstraints;
    }

    public ValidatableParametersMetaData getValidatableParametersMetaData() {
        Set<ParameterMetaData> cascadedParameters = CollectionHelper.newHashSet();
        for (ParameterMetaData parameterMetaData : this.parameterMetaDataList) {
            if (parameterMetaData.isCascading()) {
                cascadedParameters.add(parameterMetaData);
            }
        }
        return new ValidatableParametersMetaData(cascadedParameters);
    }

    public ReturnValueMetaData getReturnValueMetaData() {
        return this.returnValueMetaData;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public ExecutableDescriptorImpl asDescriptor(boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence) {
        return new ExecutableDescriptorImpl(getType(), getName(), asDescriptors(getCrossParameterConstraints()), this.returnValueMetaData.asDescriptor(defaultGroupSequenceRedefined, defaultGroupSequence), parametersAsDescriptors(defaultGroupSequenceRedefined, defaultGroupSequence), defaultGroupSequenceRedefined, this.isGetter, defaultGroupSequence);
    }

    private List<ParameterDescriptor> parametersAsDescriptors(boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence) {
        List<ParameterDescriptor> parameterDescriptorList = CollectionHelper.newArrayList();
        for (ParameterMetaData parameterMetaData : this.parameterMetaDataList) {
            parameterDescriptorList.add(parameterMetaData.asDescriptor(defaultGroupSequenceRedefined, defaultGroupSequence));
        }
        return parameterDescriptorList;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.AbstractConstraintMetaData
    public String toString() {
        String string;
        StringBuilder parameterBuilder = new StringBuilder();
        for (Class<?> oneParameterType : getParameterTypes()) {
            parameterBuilder.append(oneParameterType.getSimpleName());
            parameterBuilder.append(", ");
        }
        if (parameterBuilder.length() > 0) {
            string = parameterBuilder.substring(0, parameterBuilder.length() - 2);
        } else {
            string = parameterBuilder.toString();
        }
        String parameters = string;
        return "ExecutableMetaData [executable=" + getType() + SymbolConstants.SPACE_SYMBOL + getName() + "(" + parameters + "), isCascading=" + isCascading() + ", isConstrained=" + isConstrained() + "]";
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.AbstractConstraintMetaData
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + Arrays.hashCode(this.parameterTypes);
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.AbstractConstraintMetaData
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ExecutableMetaData other = (ExecutableMetaData) obj;
        if (!Arrays.equals(this.parameterTypes, other.parameterTypes)) {
            return false;
        }
        return true;
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/ExecutableMetaData$Builder.class */
    public static class Builder extends MetaDataBuilder {
        private final Set<String> signatures;
        private final ConstrainedElement.ConstrainedElementKind kind;
        private final Set<ConstrainedExecutable> constrainedExecutables;
        private ExecutableElement executable;
        private final Set<MetaConstraint<?>> crossParameterConstraints;
        private final Set<MetaConstraint<?>> typeArgumentsConstraints;
        private final Set<MethodConfigurationRule> rules;
        private boolean isConstrained;
        private final Map<Class<?>, ConstrainedExecutable> executablesByDeclaringType;
        private final ExecutableHelper executableHelper;

        public Builder(Class<?> beanClass, ConstrainedExecutable constrainedExecutable, ConstraintHelper constraintHelper, ExecutableHelper executableHelper, MethodValidationConfiguration methodValidationConfiguration) {
            super(beanClass, constraintHelper);
            this.signatures = CollectionHelper.newHashSet();
            this.constrainedExecutables = CollectionHelper.newHashSet();
            this.crossParameterConstraints = CollectionHelper.newHashSet();
            this.typeArgumentsConstraints = CollectionHelper.newHashSet();
            this.isConstrained = false;
            this.executablesByDeclaringType = CollectionHelper.newHashMap();
            this.executableHelper = executableHelper;
            this.kind = constrainedExecutable.getKind();
            this.executable = constrainedExecutable.getExecutable();
            this.rules = new HashSet(methodValidationConfiguration.getConfiguredRuleSet());
            add(constrainedExecutable);
        }

        @Override // org.hibernate.validator.internal.metadata.aggregated.MetaDataBuilder
        public boolean accepts(ConstrainedElement constrainedElement) {
            if (this.kind != constrainedElement.getKind()) {
                return false;
            }
            ExecutableElement executableElement = ((ConstrainedExecutable) constrainedElement).getExecutable();
            return this.executable.equals(executableElement) || this.executableHelper.overrides(this.executable, executableElement) || this.executableHelper.overrides(executableElement, this.executable);
        }

        @Override // org.hibernate.validator.internal.metadata.aggregated.MetaDataBuilder
        public final void add(ConstrainedElement constrainedElement) {
            super.add(constrainedElement);
            ConstrainedExecutable constrainedExecutable = (ConstrainedExecutable) constrainedElement;
            this.signatures.add(constrainedExecutable.getExecutable().getSignature());
            this.constrainedExecutables.add(constrainedExecutable);
            this.isConstrained = this.isConstrained || constrainedExecutable.isConstrained();
            this.crossParameterConstraints.addAll(constrainedExecutable.getCrossParameterConstraints());
            this.typeArgumentsConstraints.addAll(constrainedExecutable.getTypeArgumentsConstraints());
            addToExecutablesByDeclaringType(constrainedExecutable);
            if (this.executable != null && this.executableHelper.overrides(constrainedExecutable.getExecutable(), this.executable)) {
                this.executable = constrainedExecutable.getExecutable();
            }
        }

        private void addToExecutablesByDeclaringType(ConstrainedExecutable executable) {
            ConstrainedExecutable mergedExecutable;
            Class<?> beanClass = executable.getLocation().getDeclaringClass();
            ConstrainedExecutable mergedExecutable2 = this.executablesByDeclaringType.get(beanClass);
            if (mergedExecutable2 != null) {
                mergedExecutable = mergedExecutable2.merge(executable);
            } else {
                mergedExecutable = executable;
            }
            this.executablesByDeclaringType.put(beanClass, mergedExecutable.merge(executable));
        }

        @Override // org.hibernate.validator.internal.metadata.aggregated.MetaDataBuilder
        public ExecutableMetaData build() {
            assertCorrectnessOfConfiguration();
            return new ExecutableMetaData(this.executable.getSimpleName(), ReflectionHelper.typeOf(this.executable.getMember()), this.executable.getParameterTypes(), this.kind == ConstrainedElement.ConstrainedElementKind.CONSTRUCTOR ? ElementKind.CONSTRUCTOR : ElementKind.METHOD, this.kind == ConstrainedElement.ConstrainedElementKind.CONSTRUCTOR ? Collections.singleton(this.executable.getSignature()) : this.signatures, adaptOriginsAndImplicitGroups(getConstraints()), findParameterMetaData(), adaptOriginsAndImplicitGroups(this.crossParameterConstraints), this.typeArgumentsConstraints, getGroupConversions(), isCascading(), this.isConstrained, this.executable.isGetterMethod(), unwrapMode());
        }

        private List<ParameterMetaData> findParameterMetaData() {
            List<ParameterMetaData.Builder> parameterBuilders = null;
            for (ConstrainedExecutable oneExecutable : this.constrainedExecutables) {
                if (parameterBuilders == null) {
                    parameterBuilders = CollectionHelper.newArrayList();
                    for (ConstrainedParameter oneParameter : oneExecutable.getAllParameterMetaData()) {
                        parameterBuilders.add(new ParameterMetaData.Builder(this.executable.getMember().getDeclaringClass(), oneParameter, this.constraintHelper));
                    }
                } else {
                    int i = 0;
                    for (ConstrainedParameter oneParameter2 : oneExecutable.getAllParameterMetaData()) {
                        parameterBuilders.get(i).add(oneParameter2);
                        i++;
                    }
                }
            }
            List<ParameterMetaData> parameterMetaDatas = CollectionHelper.newArrayList();
            for (ParameterMetaData.Builder oneBuilder : parameterBuilders) {
                parameterMetaDatas.add(oneBuilder.build());
            }
            return parameterMetaDatas;
        }

        private void assertCorrectnessOfConfiguration() {
            for (Map.Entry<Class<?>, ConstrainedExecutable> entry : this.executablesByDeclaringType.entrySet()) {
                for (Map.Entry<Class<?>, ConstrainedExecutable> otherEntry : this.executablesByDeclaringType.entrySet()) {
                    for (MethodConfigurationRule rule : this.rules) {
                        rule.apply(entry.getValue(), otherEntry.getValue());
                    }
                }
            }
        }

        private <T> T run(PrivilegedAction<T> privilegedAction) {
            return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
        }
    }
}
