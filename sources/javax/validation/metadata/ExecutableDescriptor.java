package javax.validation.metadata;

import java.util.List;
import java.util.Set;
import javax.validation.metadata.ElementDescriptor;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/metadata/ExecutableDescriptor.class */
public interface ExecutableDescriptor extends ElementDescriptor {
    String getName();

    List<ParameterDescriptor> getParameterDescriptors();

    CrossParameterDescriptor getCrossParameterDescriptor();

    ReturnValueDescriptor getReturnValueDescriptor();

    boolean hasConstrainedParameters();

    boolean hasConstrainedReturnValue();

    @Override // javax.validation.metadata.ElementDescriptor
    boolean hasConstraints();

    @Override // javax.validation.metadata.ElementDescriptor
    Set<ConstraintDescriptor<?>> getConstraintDescriptors();

    @Override // javax.validation.metadata.ElementDescriptor
    ElementDescriptor.ConstraintFinder findConstraints();
}
