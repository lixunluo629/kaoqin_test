package org.hibernate.validator.internal.metadata.aggregated;

import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ElementKind;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.ReturnValueDescriptor;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.ReturnValueDescriptorImpl;
import org.hibernate.validator.internal.metadata.facets.Cascadable;
import org.hibernate.validator.internal.metadata.facets.Validatable;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/ReturnValueMetaData.class */
public class ReturnValueMetaData extends AbstractConstraintMetaData implements Validatable, Cascadable {
    private static final String RETURN_VALUE_NODE_NAME = null;
    private final List<Cascadable> cascadables;
    private final GroupConversionHelper groupConversionHelper;
    private final Set<MetaConstraint<?>> typeArgumentsConstraints;

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public /* bridge */ /* synthetic */ ElementDescriptor asDescriptor(boolean z, List list) {
        return asDescriptor(z, (List<Class<?>>) list);
    }

    public ReturnValueMetaData(Type type, Set<MetaConstraint<?>> constraints, Set<MetaConstraint<?>> typeArgumentsConstraints, boolean isCascading, Map<Class<?>, Class<?>> groupConversions, UnwrapMode unwrapMode) {
        super(RETURN_VALUE_NODE_NAME, type, constraints, ElementKind.RETURN_VALUE, isCascading, (constraints.isEmpty() && !isCascading && typeArgumentsConstraints.isEmpty()) ? false : true, unwrapMode);
        this.typeArgumentsConstraints = Collections.unmodifiableSet(typeArgumentsConstraints);
        this.cascadables = Collections.unmodifiableList(isCascading ? Arrays.asList(this) : Collections.emptyList());
        this.groupConversionHelper = new GroupConversionHelper(groupConversions);
        this.groupConversionHelper.validateGroupConversions(isCascading(), toString());
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Validatable
    public Iterable<Cascadable> getCascadables() {
        return this.cascadables;
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
        return ElementType.METHOD;
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Set<MetaConstraint<?>> getTypeArgumentsConstraints() {
        return this.typeArgumentsConstraints;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public ReturnValueDescriptor asDescriptor(boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence) {
        return new ReturnValueDescriptorImpl(getType(), asDescriptors(getConstraints()), isCascading(), defaultGroupSequenceRedefined, defaultGroupSequence, this.groupConversionHelper.asDescriptors());
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Object getValue(Object parent) {
        return parent;
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Type getCascadableType() {
        return getType();
    }
}
