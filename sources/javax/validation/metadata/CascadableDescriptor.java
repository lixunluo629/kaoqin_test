package javax.validation.metadata;

import java.util.Set;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/metadata/CascadableDescriptor.class */
public interface CascadableDescriptor {
    boolean isCascaded();

    Set<GroupConversionDescriptor> getGroupConversions();
}
