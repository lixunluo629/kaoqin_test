package org.hibernate.validator.internal.metadata.raw;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/raw/AbstractConstrainedElement.class */
public abstract class AbstractConstrainedElement implements ConstrainedElement {
    private final ConstrainedElement.ConstrainedElementKind kind;
    protected final ConfigurationSource source;
    protected final ConstraintLocation location;
    protected final Set<MetaConstraint<?>> constraints;
    protected final Map<Class<?>, Class<?>> groupConversions;
    protected final boolean isCascading;
    protected final UnwrapMode unwrapMode;

    public AbstractConstrainedElement(ConfigurationSource source, ConstrainedElement.ConstrainedElementKind kind, ConstraintLocation location, Set<MetaConstraint<?>> constraints, Map<Class<?>, Class<?>> groupConversions, boolean isCascading, UnwrapMode unwrapMode) {
        this.kind = kind;
        this.source = source;
        this.location = location;
        this.constraints = constraints != null ? Collections.unmodifiableSet(constraints) : Collections.emptySet();
        this.groupConversions = Collections.unmodifiableMap(groupConversions);
        this.isCascading = isCascading;
        this.unwrapMode = unwrapMode;
    }

    @Override // org.hibernate.validator.internal.metadata.raw.ConstrainedElement
    public ConstrainedElement.ConstrainedElementKind getKind() {
        return this.kind;
    }

    @Override // org.hibernate.validator.internal.metadata.raw.ConstrainedElement
    public ConstraintLocation getLocation() {
        return this.location;
    }

    @Override // java.lang.Iterable
    public Iterator<MetaConstraint<?>> iterator() {
        return this.constraints.iterator();
    }

    @Override // org.hibernate.validator.internal.metadata.raw.ConstrainedElement
    public Set<MetaConstraint<?>> getConstraints() {
        return this.constraints;
    }

    @Override // org.hibernate.validator.internal.metadata.raw.ConstrainedElement
    public Map<Class<?>, Class<?>> getGroupConversions() {
        return this.groupConversions;
    }

    @Override // org.hibernate.validator.internal.metadata.raw.ConstrainedElement
    public boolean isCascading() {
        return this.isCascading;
    }

    @Override // org.hibernate.validator.internal.metadata.raw.ConstrainedElement
    public boolean isConstrained() {
        return this.isCascading || !this.constraints.isEmpty();
    }

    @Override // org.hibernate.validator.internal.metadata.raw.ConstrainedElement
    public UnwrapMode unwrapMode() {
        return this.unwrapMode;
    }

    public String toString() {
        return "AbstractConstrainedElement [kind=" + this.kind + ", source=" + this.source + ", location=" + this.location + ", constraints=" + this.constraints + ", groupConversions=" + this.groupConversions + ", isCascading=" + this.isCascading + ", unwrapMode=" + this.unwrapMode + "]";
    }

    public int hashCode() {
        int result = (31 * 1) + (this.source == null ? 0 : this.source.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractConstrainedElement other = (AbstractConstrainedElement) obj;
        if (this.source != other.source) {
            return false;
        }
        return true;
    }
}
