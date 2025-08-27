package org.springframework.data.repository.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryConfigurationUtils.class */
public abstract class RepositoryConfigurationUtils {
    private RepositoryConfigurationUtils() {
    }

    public static void exposeRegistration(RepositoryConfigurationExtension extension, BeanDefinitionRegistry registry, RepositoryConfigurationSource configurationSource) throws BeanDefinitionStoreException {
        Assert.notNull(extension, "RepositoryConfigurationExtension must not be null!");
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
        Assert.notNull(configurationSource, "RepositoryConfigurationSource must not be null!");
        Class<?> cls = extension.getClass();
        String beanName = cls.getName().concat("#").concat("0");
        if (registry.containsBeanDefinition(beanName)) {
            return;
        }
        RootBeanDefinition definition = new RootBeanDefinition(cls);
        definition.setSource(configurationSource.getSource());
        definition.setRole(2);
        definition.setLazyInit(true);
        registry.registerBeanDefinition(beanName, definition);
    }
}
