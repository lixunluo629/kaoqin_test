package org.hibernate.validator.internal.metadata.aggregated;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintDeclarationException;
import javax.validation.metadata.BeanDescriptor;
import org.hibernate.validator.internal.engine.groups.Sequence;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.facets.Validatable;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/BeanMetaData.class */
public interface BeanMetaData<T> extends Validatable {
    Class<T> getBeanClass();

    boolean hasConstraints();

    BeanDescriptor getBeanDescriptor();

    PropertyMetaData getMetaDataFor(String str);

    List<Class<?>> getDefaultGroupSequence(T t);

    Iterator<Sequence> getDefaultValidationSequence(T t);

    boolean defaultGroupSequenceIsRedefined();

    Set<MetaConstraint<?>> getMetaConstraints();

    Set<MetaConstraint<?>> getDirectMetaConstraints();

    ExecutableMetaData getMetaDataFor(ExecutableElement executableElement) throws ConstraintDeclarationException;

    List<Class<? super T>> getClassHierarchy();
}
