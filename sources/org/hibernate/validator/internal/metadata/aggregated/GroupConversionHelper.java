package org.hibernate.validator.internal.metadata.aggregated;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.validation.GroupSequence;
import javax.validation.metadata.GroupConversionDescriptor;
import org.hibernate.validator.internal.metadata.descriptor.GroupConversionDescriptorImpl;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/GroupConversionHelper.class */
public class GroupConversionHelper {
    private static final Log log = LoggerFactory.make();
    private final Map<Class<?>, Class<?>> groupConversions;

    public GroupConversionHelper(Map<Class<?>, Class<?>> groupConversions) {
        this.groupConversions = Collections.unmodifiableMap(groupConversions);
    }

    public Class<?> convertGroup(Class<?> from) {
        Class<?> to = this.groupConversions.get(from);
        return to != null ? to : from;
    }

    public Set<GroupConversionDescriptor> asDescriptors() {
        Set<GroupConversionDescriptor> descriptors = CollectionHelper.newHashSet(this.groupConversions.size());
        for (Map.Entry<Class<?>, Class<?>> conversion : this.groupConversions.entrySet()) {
            descriptors.add(new GroupConversionDescriptorImpl(conversion.getKey(), conversion.getValue()));
        }
        return Collections.unmodifiableSet(descriptors);
    }

    public void validateGroupConversions(boolean isCascaded, String location) {
        if (!isCascaded && !this.groupConversions.isEmpty()) {
            throw log.getGroupConversionOnNonCascadingElementException(location);
        }
        for (Class<?> oneGroup : this.groupConversions.keySet()) {
            if (isGroupSequence(oneGroup)) {
                throw log.getGroupConversionForSequenceException(oneGroup);
            }
        }
    }

    private boolean isGroupSequence(Class<?> oneGroup) {
        return oneGroup.isAnnotationPresent(GroupSequence.class);
    }
}
