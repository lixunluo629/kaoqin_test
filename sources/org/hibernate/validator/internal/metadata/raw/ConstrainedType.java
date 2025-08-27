package org.hibernate.validator.internal.metadata.raw;

import java.util.Collections;
import java.util.Set;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/raw/ConstrainedType.class */
public class ConstrainedType extends AbstractConstrainedElement {
    public ConstrainedType(ConfigurationSource source, ConstraintLocation location, Set<MetaConstraint<?>> constraints) {
        super(source, ConstrainedElement.ConstrainedElementKind.TYPE, location, constraints, Collections.emptyMap(), false, UnwrapMode.AUTOMATIC);
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (getLocation().getDeclaringClass() == null ? 0 : getLocation().getDeclaringClass().hashCode());
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ConstrainedType other = (ConstrainedType) obj;
        if (getLocation().getDeclaringClass() == null) {
            if (other.getLocation().getDeclaringClass() != null) {
                return false;
            }
            return true;
        }
        if (!getLocation().getDeclaringClass().equals(other.getLocation().getDeclaringClass())) {
            return false;
        }
        return true;
    }
}
