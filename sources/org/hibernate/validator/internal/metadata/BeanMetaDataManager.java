package org.hibernate.validator.internal.metadata;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.internal.engine.DefaultParameterNameProvider;
import org.hibernate.validator.internal.engine.MethodValidationConfiguration;
import org.hibernate.validator.internal.engine.groups.ValidationOrderGenerator;
import org.hibernate.validator.internal.metadata.aggregated.BeanMetaData;
import org.hibernate.validator.internal.metadata.aggregated.BeanMetaDataImpl;
import org.hibernate.validator.internal.metadata.aggregated.UnconstrainedEntityMetaDataSingleton;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider;
import org.hibernate.validator.internal.metadata.provider.MetaDataProvider;
import org.hibernate.validator.internal.metadata.provider.TypeAnnotationAwareMetaDataProvider;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.ExecutableHelper;
import org.hibernate.validator.internal.util.Version;
import org.hibernate.validator.internal.util.logging.Messages;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/BeanMetaDataManager.class */
public class BeanMetaDataManager {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private final List<MetaDataProvider> metaDataProviders;
    private final ConstraintHelper constraintHelper;
    private final ConcurrentReferenceHashMap<Class<?>, BeanMetaData<?>> beanMetaDataCache;
    private final ExecutableHelper executableHelper;
    private final ValidationOrderGenerator validationOrderGenerator;
    private final MethodValidationConfiguration methodValidationConfiguration;

    public BeanMetaDataManager(ConstraintHelper constraintHelper, ExecutableHelper executableHelper) {
        this(constraintHelper, executableHelper, new DefaultParameterNameProvider(), Collections.emptyList());
    }

    public BeanMetaDataManager(ConstraintHelper constraintHelper, ExecutableHelper executableHelper, ParameterNameProvider parameterNameProvider, List<MetaDataProvider> optionalMetaDataProviders) {
        this(constraintHelper, executableHelper, parameterNameProvider, optionalMetaDataProviders, new MethodValidationConfiguration());
    }

    public BeanMetaDataManager(ConstraintHelper constraintHelper, ExecutableHelper executableHelper, ParameterNameProvider parameterNameProvider, List<MetaDataProvider> optionalMetaDataProviders, MethodValidationConfiguration methodValidationConfiguration) {
        AnnotationMetaDataProvider defaultProvider;
        this.validationOrderGenerator = new ValidationOrderGenerator();
        this.constraintHelper = constraintHelper;
        this.metaDataProviders = CollectionHelper.newArrayList();
        this.metaDataProviders.addAll(optionalMetaDataProviders);
        this.executableHelper = executableHelper;
        this.methodValidationConfiguration = methodValidationConfiguration;
        this.beanMetaDataCache = new ConcurrentReferenceHashMap<>(16, DEFAULT_LOAD_FACTOR, 16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT, EnumSet.of(ConcurrentReferenceHashMap.Option.IDENTITY_COMPARISONS));
        AnnotationProcessingOptions annotationProcessingOptions = getAnnotationProcessingOptionsFromNonDefaultProviders();
        if (Version.getJavaRelease() >= 8) {
            defaultProvider = new TypeAnnotationAwareMetaDataProvider(constraintHelper, parameterNameProvider, annotationProcessingOptions);
        } else {
            defaultProvider = new AnnotationMetaDataProvider(constraintHelper, parameterNameProvider, annotationProcessingOptions);
        }
        this.metaDataProviders.add(defaultProvider);
    }

    public boolean isConstrained(Class<?> beanClass) {
        return getOrCreateBeanMetaData(beanClass, true).hasConstraints();
    }

    public <T> BeanMetaData<T> getBeanMetaData(Class<T> beanClass) {
        return getOrCreateBeanMetaData(beanClass, false);
    }

    public void clear() {
        this.beanMetaDataCache.clear();
    }

    public int numberOfCachedBeanMetaDataInstances() {
        return this.beanMetaDataCache.size();
    }

    private <T> BeanMetaDataImpl<T> createBeanMetaData(Class<T> clazz) {
        BeanMetaDataImpl.BeanMetaDataBuilder<T> builder = BeanMetaDataImpl.BeanMetaDataBuilder.getInstance(this.constraintHelper, this.executableHelper, this.validationOrderGenerator, clazz, this.methodValidationConfiguration);
        for (MetaDataProvider provider : this.metaDataProviders) {
            for (BeanConfiguration<? super T> beanConfiguration : provider.getBeanConfigurationForHierarchy(clazz)) {
                builder.add(beanConfiguration);
            }
        }
        return builder.build();
    }

    private AnnotationProcessingOptions getAnnotationProcessingOptionsFromNonDefaultProviders() {
        AnnotationProcessingOptions options = new AnnotationProcessingOptionsImpl();
        for (MetaDataProvider metaDataProvider : this.metaDataProviders) {
            options.merge(metaDataProvider.getAnnotationProcessingOptions());
        }
        return options;
    }

    private <T> BeanMetaData<T> getOrCreateBeanMetaData(Class<T> beanClass, boolean allowUnconstrainedTypeSingleton) {
        Contracts.assertNotNull(beanClass, Messages.MESSAGES.beanTypeCannotBeNull());
        BeanMetaData beanMetaDataCreateBeanMetaData = this.beanMetaDataCache.get(beanClass);
        if (beanMetaDataCreateBeanMetaData == null) {
            beanMetaDataCreateBeanMetaData = createBeanMetaData(beanClass);
            if (!beanMetaDataCreateBeanMetaData.hasConstraints() && allowUnconstrainedTypeSingleton) {
                beanMetaDataCreateBeanMetaData = UnconstrainedEntityMetaDataSingleton.getSingleton();
            }
            BeanMetaData beanMetaDataPutIfAbsent = this.beanMetaDataCache.putIfAbsent(beanClass, beanMetaDataCreateBeanMetaData);
            if (beanMetaDataPutIfAbsent != null) {
                beanMetaDataCreateBeanMetaData = beanMetaDataPutIfAbsent;
            }
        }
        if ((beanMetaDataCreateBeanMetaData instanceof UnconstrainedEntityMetaDataSingleton) && !allowUnconstrainedTypeSingleton) {
            beanMetaDataCreateBeanMetaData = createBeanMetaData(beanClass);
            this.beanMetaDataCache.put(beanClass, beanMetaDataCreateBeanMetaData);
        }
        return beanMetaDataCreateBeanMetaData;
    }
}
