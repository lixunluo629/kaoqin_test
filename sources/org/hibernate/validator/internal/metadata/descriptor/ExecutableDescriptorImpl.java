package org.hibernate.validator.internal.metadata.descriptor;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.CrossParameterDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import javax.validation.metadata.ReturnValueDescriptor;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/descriptor/ExecutableDescriptorImpl.class */
public class ExecutableDescriptorImpl extends ElementDescriptorImpl implements ConstructorDescriptor, MethodDescriptor {
    private final String name;
    private final List<ParameterDescriptor> parameters;
    private final CrossParameterDescriptor crossParameterDescriptor;
    private final ReturnValueDescriptor returnValueDescriptor;
    private final boolean isGetter;

    public ExecutableDescriptorImpl(Type returnType, String name, Set<ConstraintDescriptorImpl<?>> crossParameterConstraints, ReturnValueDescriptor returnValueDescriptor, List<ParameterDescriptor> parameters, boolean defaultGroupSequenceRedefined, boolean isGetter, List<Class<?>> defaultGroupSequence) {
        super(returnType, Collections.emptySet(), defaultGroupSequenceRedefined, defaultGroupSequence);
        this.name = name;
        this.parameters = Collections.unmodifiableList(parameters);
        this.returnValueDescriptor = returnValueDescriptor;
        this.crossParameterDescriptor = new CrossParameterDescriptorImpl(crossParameterConstraints, defaultGroupSequenceRedefined, defaultGroupSequence);
        this.isGetter = isGetter;
    }

    @Override // javax.validation.metadata.ExecutableDescriptor
    public String getName() {
        return this.name;
    }

    @Override // javax.validation.metadata.ExecutableDescriptor
    public List<ParameterDescriptor> getParameterDescriptors() {
        return this.parameters;
    }

    @Override // javax.validation.metadata.ExecutableDescriptor
    public ReturnValueDescriptor getReturnValueDescriptor() {
        return this.returnValueDescriptor;
    }

    @Override // javax.validation.metadata.ExecutableDescriptor
    public boolean hasConstrainedParameters() {
        if (this.crossParameterDescriptor.hasConstraints()) {
            return true;
        }
        for (ParameterDescriptor oneParameter : this.parameters) {
            if (oneParameter.hasConstraints() || oneParameter.isCascaded()) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.validation.metadata.ExecutableDescriptor
    public boolean hasConstrainedReturnValue() {
        return this.returnValueDescriptor != null && (this.returnValueDescriptor.hasConstraints() || this.returnValueDescriptor.isCascaded());
    }

    @Override // javax.validation.metadata.ExecutableDescriptor
    public CrossParameterDescriptor getCrossParameterDescriptor() {
        return this.crossParameterDescriptor;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExecutableDescriptorImpl");
        sb.append("{name='").append(this.name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean isGetter() {
        return this.isGetter;
    }
}
