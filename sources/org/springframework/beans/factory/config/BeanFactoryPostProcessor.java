package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/BeanFactoryPostProcessor.class */
public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException;
}
