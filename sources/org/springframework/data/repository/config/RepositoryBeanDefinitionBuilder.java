package org.springframework.data.repository.config;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.data.repository.query.ExtensionAwareEvaluationContextProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryBeanDefinitionBuilder.class */
class RepositoryBeanDefinitionBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) RepositoryBeanDefinitionBuilder.class);
    private final BeanDefinitionRegistry registry;
    private final RepositoryConfigurationExtension extension;
    private final ResourceLoader resourceLoader;
    private final MetadataReaderFactory metadataReaderFactory;
    private CustomRepositoryImplementationDetector implementationDetector;

    public RepositoryBeanDefinitionBuilder(BeanDefinitionRegistry registry, RepositoryConfigurationExtension extension, ResourceLoader resourceLoader, Environment environment) {
        Assert.notNull(extension, "RepositoryConfigurationExtension must not be null!");
        Assert.notNull(resourceLoader, "ResourceLoader must not be null!");
        Assert.notNull(environment, "Environment must not be null!");
        this.registry = registry;
        this.extension = extension;
        this.resourceLoader = resourceLoader;
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
        this.implementationDetector = new CustomRepositoryImplementationDetector(this.metadataReaderFactory, environment, resourceLoader);
    }

    public BeanDefinitionBuilder build(RepositoryConfiguration<?> configuration) throws BeanDefinitionStoreException {
        Assert.notNull(this.registry, "BeanDefinitionRegistry must not be null!");
        Assert.notNull(this.resourceLoader, "ResourceLoader must not be null!");
        String factoryBeanName = configuration.getRepositoryFactoryBeanName();
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(StringUtils.hasText(factoryBeanName) ? factoryBeanName : this.extension.getRepositoryFactoryClassName());
        builder.getRawBeanDefinition().setSource(configuration.getSource());
        builder.addConstructorArgValue(configuration.getRepositoryInterface());
        builder.addPropertyValue("queryLookupStrategyKey", configuration.getQueryLookupStrategyKey());
        builder.addPropertyValue("lazyInit", Boolean.valueOf(configuration.isLazyInit()));
        builder.addPropertyValue("repositoryBaseClass", configuration.getRepositoryBaseClassName());
        NamedQueriesBeanDefinitionBuilder definitionBuilder = new NamedQueriesBeanDefinitionBuilder(this.extension.getDefaultNamedQueryLocation());
        if (StringUtils.hasText(configuration.getNamedQueriesLocation())) {
            definitionBuilder.setLocations(configuration.getNamedQueriesLocation());
        }
        builder.addPropertyValue("namedQueries", definitionBuilder.build(configuration.getSource()));
        String customImplementationBeanName = registerCustomImplementation(configuration);
        if (customImplementationBeanName != null) {
            builder.addPropertyReference("customImplementation", customImplementationBeanName);
            builder.addDependsOn(customImplementationBeanName);
        }
        RootBeanDefinition evaluationContextProviderDefinition = new RootBeanDefinition((Class<?>) ExtensionAwareEvaluationContextProvider.class);
        evaluationContextProviderDefinition.setSource(configuration.getSource());
        builder.addPropertyValue("evaluationContextProvider", evaluationContextProviderDefinition);
        return builder;
    }

    private String registerCustomImplementation(RepositoryConfiguration<?> configuration) throws BeanDefinitionStoreException {
        String beanName = configuration.getImplementationBeanName();
        if (this.registry.containsBeanDefinition(beanName)) {
            return beanName;
        }
        AbstractBeanDefinition beanDefinition = this.implementationDetector.detectCustomImplementation(configuration);
        if (null == beanDefinition) {
            return null;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Registering custom repository implementation: " + configuration.getImplementationBeanName() + SymbolConstants.SPACE_SYMBOL + beanDefinition.getBeanClassName());
        }
        beanDefinition.setSource(configuration.getSource());
        this.registry.registerBeanDefinition(beanName, beanDefinition);
        return beanName;
    }
}
