package org.springframework.data.repository.config;

import java.lang.annotation.Annotation;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryBeanDefinitionRegistrarSupport.class */
public abstract class RepositoryBeanDefinitionRegistrarSupport implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    private ResourceLoader resourceLoader;
    private Environment environment;

    protected abstract Class<? extends Annotation> getAnnotation();

    protected abstract RepositoryConfigurationExtension getExtension();

    @Override // org.springframework.context.ResourceLoaderAware
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override // org.springframework.context.EnvironmentAware
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override // org.springframework.context.annotation.ImportBeanDefinitionRegistrar
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        Assert.notNull(this.resourceLoader, "ResourceLoader must not be null!");
        Assert.notNull(annotationMetadata, "AnnotationMetadata must not be null!");
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
        if (annotationMetadata.getAnnotationAttributes(getAnnotation().getName()) == null) {
            return;
        }
        AnnotationRepositoryConfigurationSource configurationSource = new AnnotationRepositoryConfigurationSource(annotationMetadata, getAnnotation(), this.resourceLoader, this.environment, registry);
        RepositoryConfigurationExtension extension = getExtension();
        RepositoryConfigurationUtils.exposeRegistration(extension, registry, configurationSource);
        RepositoryConfigurationDelegate delegate = new RepositoryConfigurationDelegate(configurationSource, this.resourceLoader, this.environment);
        delegate.registerRepositoriesIn(registry, extension);
    }
}
