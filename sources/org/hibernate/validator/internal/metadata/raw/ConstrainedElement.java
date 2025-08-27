package org.hibernate.validator.internal.metadata.raw;

import java.util.Map;
import java.util.Set;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/raw/ConstrainedElement.class */
public interface ConstrainedElement extends Iterable<MetaConstraint<?>> {

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/raw/ConstrainedElement$ConstrainedElementKind.class */
    public enum ConstrainedElementKind {
        TYPE,
        FIELD,
        CONSTRUCTOR,
        METHOD,
        PARAMETER,
        TYPE_USE
    }

    ConstrainedElementKind getKind();

    ConstraintLocation getLocation();

    Set<MetaConstraint<?>> getConstraints();

    Map<Class<?>, Class<?>> getGroupConversions();

    boolean isCascading();

    boolean isConstrained();

    UnwrapMode unwrapMode();
}
