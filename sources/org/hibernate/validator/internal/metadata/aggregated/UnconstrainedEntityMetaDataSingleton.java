package org.hibernate.validator.internal.metadata.aggregated;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintDeclarationException;
import javax.validation.metadata.BeanDescriptor;
import org.hibernate.validator.internal.engine.groups.Sequence;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.facets.Cascadable;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/UnconstrainedEntityMetaDataSingleton.class */
public final class UnconstrainedEntityMetaDataSingleton<T> implements BeanMetaData<T> {
    private static final UnconstrainedEntityMetaDataSingleton<?> singletonDummy = new UnconstrainedEntityMetaDataSingleton<>();

    private UnconstrainedEntityMetaDataSingleton() {
    }

    public static UnconstrainedEntityMetaDataSingleton<?> getSingleton() {
        return singletonDummy;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.BeanMetaData
    public Class<T> getBeanClass() {
        throw new UnsupportedOperationException();
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.BeanMetaData
    public boolean hasConstraints() {
        return false;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.BeanMetaData
    public BeanDescriptor getBeanDescriptor() {
        throw new UnsupportedOperationException();
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.BeanMetaData
    public PropertyMetaData getMetaDataFor(String propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.BeanMetaData
    public List<Class<?>> getDefaultGroupSequence(T beanState) {
        throw new UnsupportedOperationException();
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.BeanMetaData
    public boolean defaultGroupSequenceIsRedefined() {
        return false;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.BeanMetaData
    public Iterator<Sequence> getDefaultValidationSequence(T beanState) {
        throw new UnsupportedOperationException();
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.BeanMetaData
    public Set<MetaConstraint<?>> getMetaConstraints() {
        throw new UnsupportedOperationException();
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.BeanMetaData
    public Set<MetaConstraint<?>> getDirectMetaConstraints() {
        throw new UnsupportedOperationException();
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.BeanMetaData
    public ExecutableMetaData getMetaDataFor(ExecutableElement method) throws ConstraintDeclarationException {
        throw new UnsupportedOperationException();
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.BeanMetaData
    public List<Class<? super T>> getClassHierarchy() {
        throw new UnsupportedOperationException();
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Validatable
    public Iterable<Cascadable> getCascadables() {
        throw new UnsupportedOperationException();
    }
}
