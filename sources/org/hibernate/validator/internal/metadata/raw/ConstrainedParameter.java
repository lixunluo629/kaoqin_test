package org.hibernate.validator.internal.metadata.raw;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/raw/ConstrainedParameter.class */
public class ConstrainedParameter extends AbstractConstrainedElement {
    private final Type type;
    private final String name;
    private final int index;
    private final Set<MetaConstraint<?>> typeArgumentsConstraints;

    public ConstrainedParameter(ConfigurationSource source, ConstraintLocation location, Type type, int index, String name) {
        this(source, location, type, index, name, Collections.emptySet(), Collections.emptySet(), Collections.emptyMap(), false, UnwrapMode.AUTOMATIC);
    }

    public ConstrainedParameter(ConfigurationSource source, ConstraintLocation location, Type type, int index, String name, Set<MetaConstraint<?>> constraints, Set<MetaConstraint<?>> typeArgumentsConstraints, Map<Class<?>, Class<?>> groupConversions, boolean isCascading, UnwrapMode unwrapMode) {
        super(source, ConstrainedElement.ConstrainedElementKind.PARAMETER, location, constraints, groupConversions, isCascading, unwrapMode);
        this.type = type;
        this.name = name;
        this.index = index;
        this.typeArgumentsConstraints = typeArgumentsConstraints != null ? Collections.unmodifiableSet(typeArgumentsConstraints) : Collections.emptySet();
    }

    public Type getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    public Set<MetaConstraint<?>> getTypeArgumentsConstraints() {
        return this.typeArgumentsConstraints;
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement, org.hibernate.validator.internal.metadata.raw.ConstrainedElement
    public boolean isConstrained() {
        return super.isConstrained() || !this.typeArgumentsConstraints.isEmpty();
    }

    public ConstrainedParameter merge(ConstrainedParameter other) {
        String mergedName;
        UnwrapMode mergedUnwrapMode;
        ConfigurationSource mergedSource = ConfigurationSource.max(this.source, other.source);
        if (this.source.getPriority() > other.source.getPriority()) {
            mergedName = this.name;
        } else {
            mergedName = other.name;
        }
        if (this.source.getPriority() > other.source.getPriority()) {
            mergedUnwrapMode = this.unwrapMode;
        } else {
            mergedUnwrapMode = other.unwrapMode;
        }
        Set<MetaConstraint<?>> mergedConstraints = CollectionHelper.newHashSet((Collection) this.constraints);
        mergedConstraints.addAll(other.constraints);
        Set<MetaConstraint<?>> mergedTypeArgumentsConstraints = CollectionHelper.newHashSet((Collection) this.typeArgumentsConstraints);
        mergedTypeArgumentsConstraints.addAll(other.typeArgumentsConstraints);
        Map<Class<?>, Class<?>> mergedGroupConversions = CollectionHelper.newHashMap(this.groupConversions);
        mergedGroupConversions.putAll(other.groupConversions);
        return new ConstrainedParameter(mergedSource, getLocation(), this.type, this.index, mergedName, mergedConstraints, mergedTypeArgumentsConstraints, mergedGroupConversions, this.isCascading || other.isCascading, mergedUnwrapMode);
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MetaConstraint<?> oneConstraint : getConstraints()) {
            sb.append(oneConstraint.getDescriptor().getAnnotation().annotationType().getSimpleName());
            sb.append(", ");
        }
        String constraintsAsString = sb.length() > 0 ? sb.substring(0, sb.length() - 2) : sb.toString();
        return "ParameterMetaData [location=" + getLocation() + "], name=" + this.name + "], constraints=[" + constraintsAsString + "], isCascading=" + isCascading() + "]";
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * result) + this.index)) + (getLocation().getMember() == null ? 0 : getLocation().getMember().hashCode());
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ConstrainedParameter other = (ConstrainedParameter) obj;
        if (this.index != other.index) {
            return false;
        }
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
