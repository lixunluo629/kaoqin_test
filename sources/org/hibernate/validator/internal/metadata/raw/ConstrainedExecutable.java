package org.hibernate.validator.internal.metadata.raw;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/raw/ConstrainedExecutable.class */
public class ConstrainedExecutable extends AbstractConstrainedElement {
    private static final Log log = LoggerFactory.make();
    private final ExecutableElement executable;
    private final List<ConstrainedParameter> parameterMetaData;
    private final Set<MetaConstraint<?>> typeArgumentsConstraints;
    private final boolean hasParameterConstraints;
    private final Set<MetaConstraint<?>> crossParameterConstraints;

    public ConstrainedExecutable(ConfigurationSource source, ConstraintLocation location, Set<MetaConstraint<?>> returnValueConstraints, Map<Class<?>, Class<?>> groupConversions, boolean isCascading, UnwrapMode unwrapMode) {
        this(source, location, Collections.emptyList(), Collections.emptySet(), returnValueConstraints, Collections.emptySet(), groupConversions, isCascading, unwrapMode);
    }

    public ConstrainedExecutable(ConfigurationSource source, ConstraintLocation location, List<ConstrainedParameter> parameterMetaData, Set<MetaConstraint<?>> crossParameterConstraints, Set<MetaConstraint<?>> returnValueConstraints, Set<MetaConstraint<?>> typeArgumentsConstraints, Map<Class<?>, Class<?>> groupConversions, boolean isCascading, UnwrapMode unwrapMode) {
        ExecutableElement executableElementForConstructor;
        super(source, location.getMember() instanceof Constructor ? ConstrainedElement.ConstrainedElementKind.CONSTRUCTOR : ConstrainedElement.ConstrainedElementKind.METHOD, location, returnValueConstraints, groupConversions, isCascading, unwrapMode);
        if (location.getMember() instanceof Method) {
            executableElementForConstructor = ExecutableElement.forMethod((Method) location.getMember());
        } else {
            executableElementForConstructor = ExecutableElement.forConstructor((Constructor) location.getMember());
        }
        this.executable = executableElementForConstructor;
        if (parameterMetaData.size() != this.executable.getParameterTypes().length) {
            throw log.getInvalidLengthOfParameterMetaDataListException(this.executable.getAsString(), this.executable.getParameterTypes().length, parameterMetaData.size());
        }
        this.typeArgumentsConstraints = typeArgumentsConstraints != null ? Collections.unmodifiableSet(typeArgumentsConstraints) : Collections.emptySet();
        this.crossParameterConstraints = crossParameterConstraints;
        this.parameterMetaData = Collections.unmodifiableList(parameterMetaData);
        this.hasParameterConstraints = hasParameterConstraints(parameterMetaData) || !crossParameterConstraints.isEmpty();
    }

    public ConstrainedParameter getParameterMetaData(int parameterIndex) {
        if (parameterIndex < 0 || parameterIndex > this.parameterMetaData.size() - 1) {
            throw log.getInvalidExecutableParameterIndexException(this.executable.getAsString(), parameterIndex);
        }
        return this.parameterMetaData.get(parameterIndex);
    }

    public List<ConstrainedParameter> getAllParameterMetaData() {
        return this.parameterMetaData;
    }

    public Set<MetaConstraint<?>> getCrossParameterConstraints() {
        return this.crossParameterConstraints;
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement, org.hibernate.validator.internal.metadata.raw.ConstrainedElement
    public boolean isConstrained() {
        return super.isConstrained() || !this.typeArgumentsConstraints.isEmpty() || this.hasParameterConstraints;
    }

    public boolean hasParameterConstraints() {
        return this.hasParameterConstraints;
    }

    public boolean isGetterMethod() {
        return this.executable.isGetterMethod();
    }

    public ExecutableElement getExecutable() {
        return this.executable;
    }

    public Set<MetaConstraint<?>> getTypeArgumentsConstraints() {
        return this.typeArgumentsConstraints;
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement
    public String toString() {
        return "ConstrainedExecutable [location=" + getLocation() + ", parameterMetaData=" + this.parameterMetaData + ", hasParameterConstraints=" + this.hasParameterConstraints + "]";
    }

    private boolean hasParameterConstraints(List<ConstrainedParameter> parameterMetaData) {
        for (ConstrainedParameter oneParameter : parameterMetaData) {
            if (oneParameter.isConstrained()) {
                return true;
            }
        }
        return false;
    }

    public boolean isEquallyParameterConstrained(ConstrainedExecutable other) {
        if (!getDescriptors(this.crossParameterConstraints).equals(getDescriptors(other.crossParameterConstraints))) {
            return false;
        }
        int i = 0;
        for (ConstrainedParameter parameter : this.parameterMetaData) {
            ConstrainedParameter otherParameter = other.getParameterMetaData(i);
            if (parameter.isCascading != otherParameter.isCascading || !getDescriptors(parameter.getConstraints()).equals(getDescriptors(otherParameter.getConstraints()))) {
                return false;
            }
            i++;
        }
        return true;
    }

    public ConstrainedExecutable merge(ConstrainedExecutable other) {
        UnwrapMode mergedUnwrapMode;
        ConfigurationSource mergedSource = ConfigurationSource.max(this.source, other.source);
        List<ConstrainedParameter> mergedParameterMetaData = CollectionHelper.newArrayList(this.parameterMetaData.size());
        int i = 0;
        for (ConstrainedParameter parameter : this.parameterMetaData) {
            mergedParameterMetaData.add(parameter.merge(other.getParameterMetaData(i)));
            i++;
        }
        Set<MetaConstraint<?>> mergedCrossParameterConstraints = CollectionHelper.newHashSet((Collection) this.crossParameterConstraints);
        mergedCrossParameterConstraints.addAll(other.crossParameterConstraints);
        Set<MetaConstraint<?>> mergedReturnValueConstraints = CollectionHelper.newHashSet((Collection) this.constraints);
        mergedReturnValueConstraints.addAll(other.constraints);
        Set<MetaConstraint<?>> mergedTypeArgumentsConstraints = CollectionHelper.newHashSet((Collection) this.typeArgumentsConstraints);
        mergedTypeArgumentsConstraints.addAll(other.typeArgumentsConstraints);
        Map<Class<?>, Class<?>> mergedGroupConversions = CollectionHelper.newHashMap(this.groupConversions);
        mergedGroupConversions.putAll(other.groupConversions);
        if (this.source.getPriority() > other.source.getPriority()) {
            mergedUnwrapMode = this.unwrapMode;
        } else {
            mergedUnwrapMode = other.unwrapMode;
        }
        return new ConstrainedExecutable(mergedSource, getLocation(), mergedParameterMetaData, mergedCrossParameterConstraints, mergedReturnValueConstraints, mergedTypeArgumentsConstraints, mergedGroupConversions, this.isCascading || other.isCascading, mergedUnwrapMode);
    }

    private Set<ConstraintDescriptor<?>> getDescriptors(Iterable<MetaConstraint<?>> constraints) {
        Set<ConstraintDescriptor<?>> descriptors = CollectionHelper.newHashSet();
        for (MetaConstraint<?> constraint : constraints) {
            descriptors.add(constraint.getDescriptor());
        }
        return descriptors;
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.executable == null ? 0 : this.executable.hashCode());
    }

    @Override // org.hibernate.validator.internal.metadata.raw.AbstractConstrainedElement
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ConstrainedExecutable other = (ConstrainedExecutable) obj;
        if (this.executable == null) {
            if (other.executable != null) {
                return false;
            }
            return true;
        }
        if (!this.executable.equals(other.executable)) {
            return false;
        }
        return true;
    }
}
