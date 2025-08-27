package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/InstantiationStrategy.class */
public interface InstantiationStrategy {
    Object instantiate(RootBeanDefinition rootBeanDefinition, String str, BeanFactory beanFactory) throws BeansException;

    Object instantiate(RootBeanDefinition rootBeanDefinition, String str, BeanFactory beanFactory, Constructor<?> constructor, Object... objArr) throws BeansException;

    Object instantiate(RootBeanDefinition rootBeanDefinition, String str, BeanFactory beanFactory, Object obj, Method method, Object... objArr) throws BeansException;
}
