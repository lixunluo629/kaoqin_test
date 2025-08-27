package org.springframework.data.repository.config;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.AbstractRepositoryMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryConfigurationExtensionSupport.class */
public abstract class RepositoryConfigurationExtensionSupport implements RepositoryConfigurationExtension {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) RepositoryConfigurationExtensionSupport.class);
    private static final String CLASS_LOADING_ERROR = "%s - Could not load type %s using class loader %s.";
    private static final String MULTI_STORE_DROPPED = "Spring Data {} - Could not safely identify store assignment for repository candidate {}.";

    protected abstract String getModulePrefix();

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtension
    public String getModuleName() {
        return StringUtils.capitalize(getModulePrefix());
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtension
    public <T extends RepositoryConfigurationSource> Collection<RepositoryConfiguration<T>> getRepositoryConfigurations(T configSource, ResourceLoader loader) {
        return getRepositoryConfigurations(configSource, loader, false);
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtension
    public <T extends RepositoryConfigurationSource> Collection<RepositoryConfiguration<T>> getRepositoryConfigurations(T configSource, ResourceLoader loader, boolean strictMatchesOnly) {
        Assert.notNull(configSource, "ConfigSource must not be null!");
        Assert.notNull(loader, "Loader must not be null!");
        Set<RepositoryConfiguration<T>> result = new HashSet<>();
        for (BeanDefinition candidate : configSource.getCandidates(loader)) {
            RepositoryConfiguration<T> configuration = getRepositoryConfiguration(candidate, configSource);
            if (!strictMatchesOnly || configSource.usesExplicitFilters()) {
                result.add(configuration);
            } else {
                Class<?> repositoryInterface = loadRepositoryInterface(configuration, loader);
                if (repositoryInterface == null || isStrictRepositoryCandidate(repositoryInterface)) {
                    result.add(configuration);
                }
            }
        }
        return result;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtension
    public String getDefaultNamedQueryLocation() {
        return String.format("classpath*:META-INF/%s-named-queries.properties", getModulePrefix());
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtension
    public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource configurationSource) {
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtension
    public void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source) {
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtension
    public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtension
    public void postProcess(BeanDefinitionBuilder builder, XmlRepositoryConfigurationSource config) {
    }

    protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
        return Collections.emptySet();
    }

    protected Collection<Class<?>> getIdentifyingTypes() {
        return Collections.emptySet();
    }

    public static String registerWithSourceAndGeneratedBeanName(BeanDefinitionRegistry registry, AbstractBeanDefinition bean, Object source) throws BeanDefinitionStoreException {
        bean.setSource(source);
        String beanName = BeanDefinitionReaderUtils.generateBeanName(bean, registry);
        registry.registerBeanDefinition(beanName, bean);
        return beanName;
    }

    public static void registerIfNotAlreadyRegistered(AbstractBeanDefinition bean, BeanDefinitionRegistry registry, String beanName, Object source) throws BeanDefinitionStoreException {
        if (registry.containsBeanDefinition(beanName)) {
            return;
        }
        bean.setSource(source);
        registry.registerBeanDefinition(beanName, bean);
    }

    public static boolean hasBean(Class<?> type, BeanDefinitionRegistry registry) {
        String name = String.format("%s%s0", type.getName(), "#");
        return registry.containsBeanDefinition(name);
    }

    protected <T extends RepositoryConfigurationSource> RepositoryConfiguration<T> getRepositoryConfiguration(BeanDefinition definition, T configSource) {
        return new DefaultRepositoryConfiguration(configSource, definition);
    }

    protected boolean isStrictRepositoryCandidate(Class<?> repositoryInterface) {
        RepositoryMetadata metadata = AbstractRepositoryMetadata.getMetadata(repositoryInterface);
        Collection<Class<?>> types = getIdentifyingTypes();
        for (Class<?> type : types) {
            if (type.isAssignableFrom(repositoryInterface)) {
                return true;
            }
        }
        Class<?> domainType = metadata.getDomainType();
        Collection<Class<? extends Annotation>> annotations = getIdentifyingAnnotations();
        if (annotations.isEmpty()) {
            return true;
        }
        for (Class<? extends Annotation> annotationType : annotations) {
            if (AnnotationUtils.findAnnotation(domainType, (Class) annotationType) != null) {
                return true;
            }
        }
        LOGGER.info(MULTI_STORE_DROPPED, getModuleName(), repositoryInterface);
        return false;
    }

    private Class<?> loadRepositoryInterface(RepositoryConfiguration<?> configuration, ResourceLoader loader) {
        String repositoryInterface = configuration.getRepositoryInterface();
        ClassLoader classLoader = loader.getClassLoader();
        try {
            return ClassUtils.forName(repositoryInterface, classLoader);
        } catch (ClassNotFoundException e) {
            LOGGER.warn(String.format(CLASS_LOADING_ERROR, getModuleName(), repositoryInterface, classLoader), (Throwable) e);
            return null;
        } catch (LinkageError e2) {
            LOGGER.warn(String.format(CLASS_LOADING_ERROR, getModuleName(), repositoryInterface, classLoader), (Throwable) e2);
            return null;
        }
    }
}
