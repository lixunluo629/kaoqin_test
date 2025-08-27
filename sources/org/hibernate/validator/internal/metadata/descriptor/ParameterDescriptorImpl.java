package org.hibernate.validator.internal.metadata.descriptor;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.ParameterDescriptor;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/descriptor/ParameterDescriptorImpl.class */
public class ParameterDescriptorImpl extends ElementDescriptorImpl implements ParameterDescriptor {
    private final int index;
    private final String name;
    private final boolean cascaded;
    private final Set<GroupConversionDescriptor> groupConversions;

    public ParameterDescriptorImpl(Type type, int index, String name, Set<ConstraintDescriptorImpl<?>> constraints, boolean isCascaded, boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence, Set<GroupConversionDescriptor> groupConversions) {
        super(type, constraints, defaultGroupSequenceRedefined, defaultGroupSequence);
        this.index = index;
        this.name = name;
        this.cascaded = isCascaded;
        this.groupConversions = Collections.unmodifiableSet(groupConversions);
    }

    @Override // javax.validation.metadata.CascadableDescriptor
    public boolean isCascaded() {
        return this.cascaded;
    }

    @Override // javax.validation.metadata.CascadableDescriptor
    public Set<GroupConversionDescriptor> getGroupConversions() {
        return this.groupConversions;
    }

    @Override // javax.validation.metadata.ParameterDescriptor
    public int getIndex() {
        return this.index;
    }

    @Override // javax.validation.metadata.ParameterDescriptor
    public String getName() {
        return this.name;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParameterDescriptorImpl");
        sb.append("{cascaded=").append(this.cascaded);
        sb.append(", index=").append(this.index);
        sb.append(", name=").append(this.name);
        sb.append('}');
        return sb.toString();
    }
}
