package org.hibernate.validator.internal.metadata.aggregated;

import java.lang.reflect.Type;
import java.util.List;
import javax.validation.ElementKind;
import javax.validation.metadata.ElementDescriptor;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/ConstraintMetaData.class */
public interface ConstraintMetaData extends Iterable<MetaConstraint<?>> {
    String getName();

    Type getType();

    ElementKind getKind();

    boolean isCascading();

    boolean isConstrained();

    ElementDescriptor asDescriptor(boolean z, List<Class<?>> list);

    UnwrapMode unwrapMode();
}
