package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/DefaultBeanNameGenerator.class */
public class DefaultBeanNameGenerator implements BeanNameGenerator {
    @Override // org.springframework.beans.factory.support.BeanNameGenerator
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return BeanDefinitionReaderUtils.generateBeanName(definition, registry);
    }
}
