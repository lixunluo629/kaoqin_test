package org.springframework.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/ListableBeanFactory.class */
public interface ListableBeanFactory extends BeanFactory {
    boolean containsBeanDefinition(String str);

    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();

    String[] getBeanNamesForType(ResolvableType resolvableType);

    String[] getBeanNamesForType(Class<?> cls);

    String[] getBeanNamesForType(Class<?> cls, boolean z, boolean z2);

    <T> Map<String, T> getBeansOfType(Class<T> cls) throws BeansException;

    <T> Map<String, T> getBeansOfType(Class<T> cls, boolean z, boolean z2) throws BeansException;

    String[] getBeanNamesForAnnotation(Class<? extends Annotation> cls);

    Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> cls) throws BeansException;

    <A extends Annotation> A findAnnotationOnBean(String str, Class<A> cls) throws NoSuchBeanDefinitionException;
}
