package org.hibernate.validator.internal.metadata.raw;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/raw/ConstrainedField.class */
public class ConstrainedField extends AbstractConstrainedElement {
    private final Set<MetaConstraint<?>> typeArgumentsConstraints;

    public ConstrainedField(ConfigurationSource source, ConstraintLocation location, Set<MetaConstraint<?>> constraints, Set<MetaConstraint<?>> typeArgumentsConstraints, Map<Class<?>, Class<?>> groupConversions, boolean isCascading, UnwrapMode unwrapMode) {
        super(source, ConstrainedElement.ConstrainedElementKind.FIELD, location, constraints, groupConversions, isCascading, unwrapMode);
        this.typeArgumentsConstraints = typeArgumentsConstraints != null ? Collections.unmodifiableSet(typeArgumentsConstraints) : Collections.emptySet();
    }

    public Set<MetaConstraint<?>> getTypeArgumentsConstraints() {
        return this.typeArgumentsConstraints;
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement, org.hibernate.validator.internal.metadata.raw.ConstrainedElement
    public boolean isConstrained() {
        return super.isConstrained() || !this.typeArgumentsConstraints.isEmpty();
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (getLocation().getMember() == null ? 0 : getLocation().getMember().hashCode());
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ConstrainedField other = (ConstrainedField) obj;
        if (getLocation().getMember() == null) {
            if (other.getLocation().getMember() != null) {
                return false;
            }
            return true;
        }
        if (!getLocation().getMember().equals(other.getLocation().getMember())) {
            return false;
        }
        return true;
    }
}
