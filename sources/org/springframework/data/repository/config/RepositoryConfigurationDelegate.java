package org.springframework.data.repository.config;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryConfigurationDelegate.class */
public class RepositoryConfigurationDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) RepositoryConfigurationDelegate.class);
    private static final String REPOSITORY_REGISTRATION = "Spring Data {} - Registering repository: {} - Interface: {} - Factory: {}";
    private static final String MULTIPLE_MODULES = "Multiple Spring Data modules found, entering strict repository configuration mode!";
    static final String FACTORY_BEAN_OBJECT_TYPE = "factoryBeanObjectType";
    private final RepositoryConfigurationSource configurationSource;
    private final ResourceLoader resourceLoader;
    private final Environment environment;
    private final BeanNameGenerator beanNameGenerator;
    private final boolean isXml;
    private final boolean inMultiStoreMode;

    public RepositoryConfigurationDelegate(RepositoryConfigurationSource configurationSource, ResourceLoader resourceLoader, Environment environment) {
        this.isXml = configurationSource instanceof XmlRepositoryConfigurationSource;
        boolean isAnnotation = configurationSource instanceof AnnotationRepositoryConfigurationSource;
        Assert.isTrue(this.isXml || isAnnotation, "Configuration source must either be an Xml- or an AnnotationBasedConfigurationSource!");
        Assert.notNull(resourceLoader, "ResourceLoader must not be null!");
        RepositoryBeanNameGenerator generator = new RepositoryBeanNameGenerator();
        generator.setBeanClassLoader(resourceLoader.getClassLoader());
        this.beanNameGenerator = generator;
        this.configurationSource = configurationSource;
        this.resourceLoader = resourceLoader;
        this.environment = defaultEnvironment(environment, resourceLoader);
        this.inMultiStoreMode = multipleStoresDetected();
    }

    private static Environment defaultEnvironment(Environment environment, ResourceLoader resourceLoader) {
        if (environment != null) {
            return environment;
        }
        return resourceLoader instanceof EnvironmentCapable ? ((EnvironmentCapable) resourceLoader).getEnvironment() : new StandardEnvironment();
    }

    public List<BeanComponentDefinition> registerRepositoriesIn(BeanDefinitionRegistry registry, RepositoryConfigurationExtension extension) {
        extension.registerBeansForRoot(registry, this.configurationSource);
        RepositoryBeanDefinitionBuilder builder = new RepositoryBeanDefinitionBuilder(registry, extension, this.resourceLoader, this.environment);
        List<BeanComponentDefinition> definitions = new ArrayList<>();
        for (RepositoryConfiguration<? extends RepositoryConfigurationSource> configuration : extension.getRepositoryConfigurations(this.configurationSource, this.resourceLoader, this.inMultiStoreMode)) {
            BeanDefinitionBuilder definitionBuilder = builder.build(configuration);
            extension.postProcess(definitionBuilder, this.configurationSource);
            if (this.isXml) {
                extension.postProcess(definitionBuilder, (XmlRepositoryConfigurationSource) this.configurationSource);
            } else {
                extension.postProcess(definitionBuilder, (AnnotationRepositoryConfigurationSource) this.configurationSource);
            }
            AbstractBeanDefinition beanDefinition = definitionBuilder.getBeanDefinition();
            String beanName = this.beanNameGenerator.generateBeanName(beanDefinition, registry);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(REPOSITORY_REGISTRATION, extension.getModuleName(), beanName, configuration.getRepositoryInterface(), extension.getRepositoryFactoryClassName());
            }
            beanDefinition.setAttribute("factoryBeanObjectType", configuration.getRepositoryInterface());
            registry.registerBeanDefinition(beanName, beanDefinition);
            definitions.add(new BeanComponentDefinition(beanDefinition, beanName));
        }
        return definitions;
    }

    private boolean multipleStoresDetected() {
        boolean multipleModulesFound = SpringFactoriesLoader.loadFactoryNames(RepositoryFactorySupport.class, this.resourceLoader.getClassLoader()).size() > 1;
        if (multipleModulesFound) {
            LOGGER.info(MULTIPLE_MODULES);
        }
        return multipleModulesFound;
    }
}
