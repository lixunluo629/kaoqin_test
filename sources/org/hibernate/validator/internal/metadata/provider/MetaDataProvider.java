package org.hibernate.validator.internal.metadata.provider;

import java.util.List;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/provider/MetaDataProvider.class */
public interface MetaDataProvider {
    AnnotationProcessingOptions getAnnotationProcessingOptions();

    <T> List<BeanConfiguration<? super T>> getBeanConfigurationForHierarchy(Class<T> cls);
}
