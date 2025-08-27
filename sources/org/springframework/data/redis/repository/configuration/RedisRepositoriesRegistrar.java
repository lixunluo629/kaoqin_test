package org.springframework.data.redis.repository.configuration;

import java.lang.annotation.Annotation;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/configuration/RedisRepositoriesRegistrar.class */
public class RedisRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {
    @Override // org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport
    protected Class<? extends Annotation> getAnnotation() {
        return EnableRedisRepositories.class;
    }

    @Override // org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport
    protected RepositoryConfigurationExtension getExtension() {
        return new RedisRepositoryConfigurationExtension();
    }
}
