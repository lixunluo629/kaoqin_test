package org.springframework.data.map.repository.config;

import java.lang.annotation.Annotation;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/map/repository/config/MapRepositoriesRegistrar.class */
public class MapRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {
    @Override // org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport
    protected Class<? extends Annotation> getAnnotation() {
        return EnableMapRepositories.class;
    }

    @Override // org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport
    protected RepositoryConfigurationExtension getExtension() {
        return new MapRepositoryConfigurationExtension();
    }
}
