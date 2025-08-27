package org.springframework.boot.autoconfigure.data.redis;

import java.lang.annotation.Annotation;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.repository.configuration.RedisRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/redis/RedisRepositoriesAutoConfigureRegistrar.class */
class RedisRepositoriesAutoConfigureRegistrar extends AbstractRepositoryConfigurationSourceSupport {
    RedisRepositoriesAutoConfigureRegistrar() {
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<? extends Annotation> getAnnotation() {
        return EnableRedisRepositories.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<?> getConfiguration() {
        return EnableRedisRepositoriesConfiguration.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new RedisRepositoryConfigurationExtension();
    }

    @EnableRedisRepositories
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/redis/RedisRepositoriesAutoConfigureRegistrar$EnableRedisRepositoriesConfiguration.class */
    private static class EnableRedisRepositoriesConfiguration {
        private EnableRedisRepositoriesConfiguration() {
        }
    }
}
