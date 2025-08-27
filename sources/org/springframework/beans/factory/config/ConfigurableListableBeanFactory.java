package org.springframework.beans.factory.config;

import java.util.Iterator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/ConfigurableListableBeanFactory.class */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {
    void ignoreDependencyType(Class<?> cls);

    void ignoreDependencyInterface(Class<?> cls);

    void registerResolvableDependency(Class<?> cls, Object obj);

    boolean isAutowireCandidate(String str, DependencyDescriptor dependencyDescriptor) throws NoSuchBeanDefinitionException;

    BeanDefinition getBeanDefinition(String str) throws NoSuchBeanDefinitionException;

    Iterator<String> getBeanNamesIterator();

    void clearMetadataCache();

    void freezeConfiguration();

    boolean isConfigurationFrozen();

    void preInstantiateSingletons() throws BeansException;
}
