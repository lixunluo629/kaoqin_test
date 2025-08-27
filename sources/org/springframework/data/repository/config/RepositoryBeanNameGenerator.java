package org.springframework.data.repository.config;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryBeanNameGenerator.class */
public class RepositoryBeanNameGenerator implements BeanNameGenerator, BeanClassLoaderAware {
    private static final BeanNameGenerator DELEGATE = new AnnotationBeanNameGenerator();
    private ClassLoader beanClassLoader;

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override // org.springframework.beans.factory.support.BeanNameGenerator
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        AnnotatedBeanDefinition beanDefinition = definition instanceof AnnotatedBeanDefinition ? (AnnotatedBeanDefinition) definition : new AnnotatedGenericBeanDefinition(getRepositoryInterfaceFrom(definition));
        return DELEGATE.generateBeanName(beanDefinition, registry);
    }

    private Class<?> getRepositoryInterfaceFrom(BeanDefinition beanDefinition) {
        Object value = beanDefinition.getConstructorArgumentValues().getArgumentValue(0, Class.class).getValue();
        if (value instanceof Class) {
            return (Class) value;
        }
        try {
            return ClassUtils.forName(value.toString(), this.beanClassLoader);
        } catch (Exception o_O) {
            throw new RuntimeException(o_O);
        }
    }
}
