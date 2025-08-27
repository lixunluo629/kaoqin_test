package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/BeanDefinitionReader.class */
public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    ClassLoader getBeanClassLoader();

    BeanNameGenerator getBeanNameGenerator();

    int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException;

    int loadBeanDefinitions(Resource... resourceArr) throws BeanDefinitionStoreException;

    int loadBeanDefinitions(String str) throws BeanDefinitionStoreException;

    int loadBeanDefinitions(String... strArr) throws BeanDefinitionStoreException;
}
