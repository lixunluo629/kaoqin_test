package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanPostProcessor;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/MergedBeanDefinitionPostProcessor.class */
public interface MergedBeanDefinitionPostProcessor extends BeanPostProcessor {
    void postProcessMergedBeanDefinition(RootBeanDefinition rootBeanDefinition, Class<?> cls, String str);
}
