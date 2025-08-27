package org.hibernate.validator.internal.metadata.aggregated;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.ConstraintOrigin;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/MetaDataBuilder.class */
public abstract class MetaDataBuilder {
    private static final Log log = LoggerFactory.make();
    protected final ConstraintHelper constraintHelper;
    private final Class<?> beanClass;
    private final Set<MetaConstraint<?>> constraints = CollectionHelper.newHashSet();
    private final Map<Class<?>, Class<?>> groupConversions = CollectionHelper.newHashMap();
    private boolean isCascading = false;
    private UnwrapMode unwrapMode = UnwrapMode.AUTOMATIC;

    public abstract boolean accepts(ConstrainedElement constrainedElement);

    public abstract ConstraintMetaData build();

    protected MetaDataBuilder(Class<?> beanClass, ConstraintHelper constraintHelper) {
        this.beanClass = beanClass;
        this.constraintHelper = constraintHelper;
    }

    public void add(ConstrainedElement constrainedElement) {
        this.constraints.addAll(constrainedElement.getConstraints());
        this.isCascading = this.isCascading || constrainedElement.isCascading();
        this.unwrapMode = constrainedElement.unwrapMode();
        addGroupConversions(constrainedElement.getGroupConversions());
    }

    private void addGroupConversions(Map<Class<?>, Class<?>> groupConversions) {
        for (Map.Entry<Class<?>, Class<?>> oneConversion : groupConversions.entrySet()) {
            if (this.groupConversions.containsKey(oneConversion.getKey())) {
                throw log.getMultipleGroupConversionsForSameSourceException(oneConversion.getKey(), CollectionHelper.asSet(groupConversions.get(oneConversion.getKey()), oneConversion.getValue()));
            }
            this.groupConversions.put(oneConversion.getKey(), oneConversion.getValue());
        }
    }

    protected Map<Class<?>, Class<?>> getGroupConversions() {
        return this.groupConversions;
    }

    protected Set<MetaConstraint<?>> getConstraints() {
        return this.constraints;
    }

    protected boolean isCascading() {
        return this.isCascading;
    }

    protected Class<?> getBeanClass() {
        return this.beanClass;
    }

    public UnwrapMode unwrapMode() {
        return this.unwrapMode;
    }

    protected Set<MetaConstraint<?>> adaptOriginsAndImplicitGroups(Set<MetaConstraint<?>> constraints) {
        Set<MetaConstraint<?>> adaptedConstraints = CollectionHelper.newHashSet();
        for (MetaConstraint<?> oneConstraint : constraints) {
            adaptedConstraints.add(adaptOriginAndImplicitGroup(oneConstraint));
        }
        return adaptedConstraints;
    }

    private <A extends Annotation> MetaConstraint<A> adaptOriginAndImplicitGroup(MetaConstraint<A> constraint) {
        ConstraintOrigin definedIn = definedIn(this.beanClass, constraint.getLocation().getDeclaringClass());
        if (definedIn == ConstraintOrigin.DEFINED_LOCALLY) {
            return constraint;
        }
        Class<?> constraintClass = constraint.getLocation().getDeclaringClass();
        ConstraintDescriptorImpl<A> descriptor = new ConstraintDescriptorImpl<>(this.constraintHelper, constraint.getLocation().getMember(), constraint.getDescriptor().getAnnotation(), constraint.getElementType(), constraintClass.isInterface() ? constraintClass : null, definedIn, constraint.getDescriptor().getConstraintType());
        return new MetaConstraint<>(descriptor, constraint.getLocation());
    }

    private ConstraintOrigin definedIn(Class<?> rootClass, Class<?> hierarchyClass) {
        if (hierarchyClass.equals(rootClass)) {
            return ConstraintOrigin.DEFINED_LOCALLY;
        }
        return ConstraintOrigin.DEFINED_IN_HIERARCHY;
    }
}
