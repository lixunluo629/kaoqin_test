package org.hibernate.validator.internal.metadata.facets;

import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
import java.util.Set;
import javax.validation.ElementKind;
import javax.validation.metadata.GroupConversionDescriptor;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/facets/Cascadable.class */
public interface Cascadable {
    Class<?> convertGroup(Class<?> cls);

    Set<GroupConversionDescriptor> getGroupConversionDescriptors();

    ElementType getElementType();

    String getName();

    ElementKind getKind();

    Set<MetaConstraint<?>> getTypeArgumentsConstraints();

    UnwrapMode unwrapMode();

    Type getCascadableType();

    Object getValue(Object obj);
}
