package javax.validation.metadata;

import java.lang.annotation.ElementType;
import java.util.Set;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/metadata/ElementDescriptor.class */
public interface ElementDescriptor {

    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/metadata/ElementDescriptor$ConstraintFinder.class */
    public interface ConstraintFinder {
        ConstraintFinder unorderedAndMatchingGroups(Class<?>... clsArr);

        ConstraintFinder lookingAt(Scope scope);

        ConstraintFinder declaredOn(ElementType... elementTypeArr);

        Set<ConstraintDescriptor<?>> getConstraintDescriptors();

        boolean hasConstraints();
    }

    boolean hasConstraints();

    Class<?> getElementClass();

    Set<ConstraintDescriptor<?>> getConstraintDescriptors();

    ConstraintFinder findConstraints();
}
