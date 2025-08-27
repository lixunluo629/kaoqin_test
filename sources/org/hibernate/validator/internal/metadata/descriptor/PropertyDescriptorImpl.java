package org.hibernate.validator.internal.metadata.descriptor;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.PropertyDescriptor;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/descriptor/PropertyDescriptorImpl.class */
public class PropertyDescriptorImpl extends ElementDescriptorImpl implements PropertyDescriptor {
    private final boolean cascaded;
    private final String property;
    private final Set<GroupConversionDescriptor> groupConversions;

    public PropertyDescriptorImpl(Type returnType, String propertyName, Set<ConstraintDescriptorImpl<?>> constraints, boolean cascaded, boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence, Set<GroupConversionDescriptor> groupConversions) {
        super(returnType, constraints, defaultGroupSequenceRedefined, defaultGroupSequence);
        this.property = propertyName;
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

    @Override // javax.validation.metadata.PropertyDescriptor
    public String getPropertyName() {
        return this.property;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PropertyDescriptorImpl");
        sb.append("{property=").append(this.property);
        sb.append(", cascaded='").append(this.cascaded).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
