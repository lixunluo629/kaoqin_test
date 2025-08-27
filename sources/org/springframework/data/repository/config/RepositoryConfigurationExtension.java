package org.springframework.data.repository.config;

import java.util.Collection;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.ResourceLoader;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryConfigurationExtension.class */
public interface RepositoryConfigurationExtension {
    String getModuleName();

    @Deprecated
    <T extends RepositoryConfigurationSource> Collection<RepositoryConfiguration<T>> getRepositoryConfigurations(T t, ResourceLoader resourceLoader);

    <T extends RepositoryConfigurationSource> Collection<RepositoryConfiguration<T>> getRepositoryConfigurations(T t, ResourceLoader resourceLoader, boolean z);

    String getDefaultNamedQueryLocation();

    String getRepositoryFactoryClassName();

    void registerBeansForRoot(BeanDefinitionRegistry beanDefinitionRegistry, RepositoryConfigurationSource repositoryConfigurationSource);

    void postProcess(BeanDefinitionBuilder beanDefinitionBuilder, RepositoryConfigurationSource repositoryConfigurationSource);

    void postProcess(BeanDefinitionBuilder beanDefinitionBuilder, AnnotationRepositoryConfigurationSource annotationRepositoryConfigurationSource);

    void postProcess(BeanDefinitionBuilder beanDefinitionBuilder, XmlRepositoryConfigurationSource xmlRepositoryConfigurationSource);
}
