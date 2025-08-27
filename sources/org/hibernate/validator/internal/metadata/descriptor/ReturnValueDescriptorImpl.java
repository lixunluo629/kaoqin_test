package org.hibernate.validator.internal.metadata.descriptor;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.ReturnValueDescriptor;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/descriptor/ReturnValueDescriptorImpl.class */
public class ReturnValueDescriptorImpl extends ElementDescriptorImpl implements ReturnValueDescriptor {
    private final boolean cascaded;
    private final Set<GroupConversionDescriptor> groupConversions;

    public ReturnValueDescriptorImpl(Type returnType, Set<ConstraintDescriptorImpl<?>> returnValueConstraints, boolean cascaded, boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence, Set<GroupConversionDescriptor> groupConversions) {
        super(returnType, returnValueConstraints, defaultGroupSequenceRedefined, defaultGroupSequence);
        this.cascaded = cascaded;
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReturnValueDescriptorImpl");
        sb.append("{cascaded=").append(this.cascaded);
        sb.append('}');
        return sb.toString();
    }
}
