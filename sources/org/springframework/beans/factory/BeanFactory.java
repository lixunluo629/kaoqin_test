package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/BeanFactory.class */
public interface BeanFactory {
    public static final String FACTORY_BEAN_PREFIX = "&";

    Object getBean(String str) throws BeansException;

    <T> T getBean(String str, Class<T> cls) throws BeansException;

    Object getBean(String str, Object... objArr) throws BeansException;

    <T> T getBean(Class<T> cls) throws BeansException;

    <T> T getBean(Class<T> cls, Object... objArr) throws BeansException;

    boolean containsBean(String str);

    boolean isSingleton(String str) throws NoSuchBeanDefinitionException;

    boolean isPrototype(String str) throws NoSuchBeanDefinitionException;

    boolean isTypeMatch(String str, ResolvableType resolvableType) throws NoSuchBeanDefinitionException;

    boolean isTypeMatch(String str, Class<?> cls) throws NoSuchBeanDefinitionException;

    Class<?> getType(String str) throws NoSuchBeanDefinitionException;

    String[] getAliases(String str);
}
