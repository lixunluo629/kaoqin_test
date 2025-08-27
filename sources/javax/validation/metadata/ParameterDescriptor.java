package javax.validation.metadata;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/metadata/ParameterDescriptor.class */
public interface ParameterDescriptor extends ElementDescriptor, CascadableDescriptor {
    int getIndex();

    String getName();
}
