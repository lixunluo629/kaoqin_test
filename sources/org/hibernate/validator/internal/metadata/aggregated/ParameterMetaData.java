package org.hibernate.validator.internal.metadata.aggregated;

import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ElementKind;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.ParameterDescriptorImpl;
import org.hibernate.validator.internal.metadata.facets.Cascadable;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/ParameterMetaData.class */
public class ParameterMetaData extends AbstractConstraintMetaData implements Cascadable {
    private final GroupConversionHelper groupConversionHelper;
    private final int index;
    private final Set<MetaConstraint<?>> typeArgumentsConstraints;

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public /* bridge */ /* synthetic */ ElementDescriptor asDescriptor(boolean z, List list) {
        return asDescriptor(z, (List<Class<?>>) list);
    }

    private ParameterMetaData(int index, String name, Type type, Set<MetaConstraint<?>> constraints, Set<MetaConstraint<?>> typeArgumentsConstraints, boolean isCascading, Map<Class<?>, Class<?>> groupConversions, UnwrapMode unwrapMode) {
        super(name, type, constraints, ElementKind.PARAMETER, isCascading, (constraints.isEmpty() && !isCascading && typeArgumentsConstraints.isEmpty()) ? false : true, unwrapMode);
        this.index = index;
        this.typeArgumentsConstraints = Collections.unmodifiableSet(typeArgumentsConstraints);
        this.groupConversionHelper = new GroupConversionHelper(groupConversions);
        this.groupConversionHelper.validateGroupConversions(isCascading(), toString());
    }

    public int getIndex() {
        return this.index;
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Class<?> convertGroup(Class<?> originalGroup) {
        return this.groupConversionHelper.convertGroup(originalGroup);
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Set<GroupConversionDescriptor> getGroupConversionDescriptors() {
        return this.groupConversionHelper.asDescriptors();
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public ElementType getElementType() {
        return ElementType.PARAMETER;
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Set<MetaConstraint<?>> getTypeArgumentsConstraints() {
        return this.typeArgumentsConstraints;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public ParameterDescriptor asDescriptor(boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence) {
        return new ParameterDescriptorImpl(getType(), this.index, getName(), asDescriptors(getConstraints()), isCascading(), defaultGroupSequenceRedefined, defaultGroupSequence, getGroupConversionDescriptors());
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Object getValue(Object parent) {
        return ((Object[]) parent)[getIndex()];
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Type getCascadableType() {
        return getType();
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/ParameterMetaData$Builder.class */
    public static class Builder extends MetaDataBuilder {
        private final Type parameterType;
        private final int parameterIndex;
        private ConstrainedParameter constrainedParameter;
        private final Set<MetaConstraint<?>> typeArgumentsConstraints;

        public Builder(Class<?> beanClass, ConstrainedParameter constrainedParameter, ConstraintHelper constraintHelper) {
            super(beanClass, constraintHelper);
            this.typeArgumentsConstraints = CollectionHelper.newHashSet();
            this.parameterType = constrainedParameter.getType();
            this.parameterIndex = constrainedParameter.getIndex();
            add(constrainedParameter);
        }

        @Override // org.hibernate.validator.internal.metadata.aggregated.MetaDataBuilder
        public boolean accepts(ConstrainedElement constrainedElement) {
            return constrainedElement.getKind() == ConstrainedElement.ConstrainedElementKind.PARAMETER && ((ConstrainedParameter) constrainedElement).getIndex() == this.parameterIndex;
        }

        @Override // org.hibernate.validator.internal.metadata.aggregated.MetaDataBuilder
        public void add(ConstrainedElement constrainedElement) {
            super.add(constrainedElement);
            ConstrainedParameter newConstrainedParameter = (ConstrainedParameter) constrainedElement;
            this.typeArgumentsConstraints.addAll(newConstrainedParameter.getTypeArgumentsConstraints());
            if (this.constrainedParameter == null) {
                this.constrainedParameter = newConstrainedParameter;
            } else if (newConstrainedParameter.getLocation().getDeclaringClass().isAssignableFrom(this.constrainedParameter.getLocation().getDeclaringClass())) {
                this.constrainedParameter = newConstrainedParameter;
            }
        }

        @Override // org.hibernate.validator.internal.metadata.aggregated.MetaDataBuilder
        public ParameterMetaData build() {
            return new ParameterMetaData(this.parameterIndex, this.constrainedParameter.getName(), this.parameterType, adaptOriginsAndImplicitGroups(getConstraints()), this.typeArgumentsConstraints, isCascading(), getGroupConversions(), unwrapMode());
        }
    }
}
