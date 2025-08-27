package org.hibernate.validator.internal.metadata.provider;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.classhierarchy.ClassHierarchyHelper;
import org.hibernate.validator.internal.util.classhierarchy.Filter;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/provider/MetaDataProviderKeyedByClassName.class */
public abstract class MetaDataProviderKeyedByClassName implements MetaDataProvider {
    protected final ConstraintHelper constraintHelper;
    private final Map<String, BeanConfiguration<?>> configuredBeans;

    public MetaDataProviderKeyedByClassName(ConstraintHelper constraintHelper, Map<String, BeanConfiguration<?>> configuredBeans) {
        this.constraintHelper = constraintHelper;
        this.configuredBeans = Collections.unmodifiableMap(configuredBeans);
    }

    @Override // org.hibernate.validator.internal.metadata.provider.MetaDataProvider
    public <T> List<BeanConfiguration<? super T>> getBeanConfigurationForHierarchy(Class<T> beanClass) {
        List<BeanConfiguration<? super T>> configurations = CollectionHelper.newArrayList();
        Iterator it = ClassHierarchyHelper.getHierarchy(beanClass, new Filter[0]).iterator();
        while (it.hasNext()) {
            BeanConfiguration<T> beanConfiguration = getBeanConfiguration((Class) it.next());
            if (beanConfiguration != null) {
                configurations.add(beanConfiguration);
            }
        }
        return configurations;
    }

    protected <T> BeanConfiguration<T> getBeanConfiguration(Class<T> beanClass) {
        Contracts.assertNotNull(beanClass);
        return (BeanConfiguration) this.configuredBeans.get(beanClass.getName());
    }

    protected static <T> BeanConfiguration<T> createBeanConfiguration(ConfigurationSource source, Class<T> beanClass, Set<? extends ConstrainedElement> constrainedElements, List<Class<?>> defaultGroupSequence, DefaultGroupSequenceProvider<? super T> defaultGroupSequenceProvider) {
        return new BeanConfiguration<>(source, beanClass, constrainedElements, defaultGroupSequence, defaultGroupSequenceProvider);
    }
}
