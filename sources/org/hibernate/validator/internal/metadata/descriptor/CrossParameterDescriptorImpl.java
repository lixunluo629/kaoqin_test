package org.hibernate.validator.internal.metadata.descriptor;

import java.util.List;
import java.util.Set;
import javax.validation.metadata.CrossParameterDescriptor;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/descriptor/CrossParameterDescriptorImpl.class */
public class CrossParameterDescriptorImpl extends ElementDescriptorImpl implements CrossParameterDescriptor {
    public CrossParameterDescriptorImpl(Set<ConstraintDescriptorImpl<?>> constraintDescriptors, boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence) {
        super(Object[].class, constraintDescriptors, defaultGroupSequenceRedefined, defaultGroupSequence);
    }
}
