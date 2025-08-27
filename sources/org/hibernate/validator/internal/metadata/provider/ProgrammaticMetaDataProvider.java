package org.hibernate.validator.internal.metadata.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.internal.cfg.context.DefaultConstraintMapping;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/provider/ProgrammaticMetaDataProvider.class */
public class ProgrammaticMetaDataProvider extends MetaDataProviderKeyedByClassName {
    private static final Log log = LoggerFactory.make();
    private final AnnotationProcessingOptions annotationProcessingOptions;

    public ProgrammaticMetaDataProvider(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, Set<DefaultConstraintMapping> constraintMappings) {
        super(constraintHelper, createBeanConfigurations(constraintMappings, constraintHelper, parameterNameProvider));
        Contracts.assertNotNull(constraintMappings);
        assertUniquenessOfConfiguredTypes(constraintMappings);
        this.annotationProcessingOptions = mergeAnnotationProcessingOptions(constraintMappings);
    }

    private void assertUniquenessOfConfiguredTypes(Set<DefaultConstraintMapping> mappings) {
        Set<Class<?>> allConfiguredTypes = CollectionHelper.newHashSet();
        for (DefaultConstraintMapping constraintMapping : mappings) {
            for (Class<?> configuredType : constraintMapping.getConfiguredTypes()) {
                if (allConfiguredTypes.contains(configuredType)) {
                    throw log.getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException(configuredType.getName());
                }
            }
            allConfiguredTypes.addAll(constraintMapping.getConfiguredTypes());
        }
    }

    private static Map<String, BeanConfiguration<?>> createBeanConfigurations(Set<DefaultConstraintMapping> mappings, ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider) {
        Map<String, BeanConfiguration<?>> configuredBeans = new HashMap<>();
        for (DefaultConstraintMapping mapping : mappings) {
            Set<BeanConfiguration<?>> beanConfigurations = mapping.getBeanConfigurations(constraintHelper, parameterNameProvider);
            for (BeanConfiguration<?> beanConfiguration : beanConfigurations) {
                configuredBeans.put(beanConfiguration.getBeanClass().getName(), beanConfiguration);
            }
        }
        return configuredBeans;
    }

    private AnnotationProcessingOptions mergeAnnotationProcessingOptions(Set<DefaultConstraintMapping> mappings) {
        if (mappings.size() == 1) {
            return mappings.iterator().next().getAnnotationProcessingOptions();
        }
        AnnotationProcessingOptions options = new AnnotationProcessingOptionsImpl();
        for (DefaultConstraintMapping mapping : mappings) {
            options.merge(mapping.getAnnotationProcessingOptions());
        }
        return options;
    }

    @Override // org.hibernate.validator.internal.metadata.provider.MetaDataProvider
    public AnnotationProcessingOptions getAnnotationProcessingOptions() {
        return this.annotationProcessingOptions;
    }
}
