package org.hibernate.validator.internal.metadata.aggregated;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.validation.ElementKind;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/AbstractConstraintMetaData.class */
public abstract class AbstractConstraintMetaData implements ConstraintMetaData {
    private final String name;
    private final Type type;
    private final ElementKind constrainedMetaDataKind;
    private final Set<MetaConstraint<?>> constraints;
    private final boolean isCascading;
    private final boolean isConstrained;
    private final UnwrapMode unwrapMode;

    public AbstractConstraintMetaData(String name, Type type, Set<MetaConstraint<?>> constraints, ElementKind constrainedMetaDataKind, boolean isCascading, boolean isConstrained, UnwrapMode unwrapMode) {
        this.name = name;
        this.type = type;
        this.constraints = Collections.unmodifiableSet(constraints);
        this.constrainedMetaDataKind = constrainedMetaDataKind;
        this.isCascading = isCascading;
        this.isConstrained = isConstrained;
        this.unwrapMode = unwrapMode;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public String getName() {
        return this.name;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public Type getType() {
        return this.type;
    }

    @Override // java.lang.Iterable
    public Iterator<MetaConstraint<?>> iterator() {
        return this.constraints.iterator();
    }

    public Set<MetaConstraint<?>> getConstraints() {
        return this.constraints;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public ElementKind getKind() {
        return this.constrainedMetaDataKind;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public final boolean isCascading() {
        return this.isCascading;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public boolean isConstrained() {
        return this.isConstrained;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public UnwrapMode unwrapMode() {
        return this.unwrapMode;
    }

    public String toString() {
        return "AbstractConstraintMetaData [name=" + this.name + ", type=" + this.type + ", constrainedMetaDataKind=" + this.constrainedMetaDataKind + ", constraints=" + this.constraints + ", isCascading=" + this.isCascading + ", isConstrained=" + this.isConstrained + ", unwrapMode=" + this.unwrapMode + "]";
    }

    public int hashCode() {
        int result = (31 * 1) + (this.name == null ? 0 : this.name.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractConstraintMetaData other = (AbstractConstraintMetaData) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
            return true;
        }
        if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    protected Set<ConstraintDescriptorImpl<?>> asDescriptors(Set<MetaConstraint<?>> constraints) {
        HashSet hashSetNewHashSet = CollectionHelper.newHashSet();
        for (MetaConstraint<?> oneConstraint : constraints) {
            hashSetNewHashSet.add(oneConstraint.getDescriptor());
        }
        return hashSetNewHashSet;
    }
}
