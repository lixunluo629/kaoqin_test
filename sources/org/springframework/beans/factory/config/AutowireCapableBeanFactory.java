package org.springframework.beans.factory.config;

import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/AutowireCapableBeanFactory.class */
public interface AutowireCapableBeanFactory extends BeanFactory {
    public static final int AUTOWIRE_NO = 0;
    public static final int AUTOWIRE_BY_NAME = 1;
    public static final int AUTOWIRE_BY_TYPE = 2;
    public static final int AUTOWIRE_CONSTRUCTOR = 3;

    @Deprecated
    public static final int AUTOWIRE_AUTODETECT = 4;

    <T> T createBean(Class<T> cls) throws BeansException;

    void autowireBean(Object obj) throws BeansException;

    Object configureBean(Object obj, String str) throws BeansException;

    Object createBean(Class<?> cls, int i, boolean z) throws BeansException;

    Object autowire(Class<?> cls, int i, boolean z) throws BeansException;

    void autowireBeanProperties(Object obj, int i, boolean z) throws BeansException;

    void applyBeanPropertyValues(Object obj, String str) throws BeansException;

    Object initializeBean(Object obj, String str) throws BeansException;

    Object applyBeanPostProcessorsBeforeInitialization(Object obj, String str) throws BeansException;

    Object applyBeanPostProcessorsAfterInitialization(Object obj, String str) throws BeansException;

    void destroyBean(Object obj);

    <T> NamedBeanHolder<T> resolveNamedBean(Class<T> cls) throws BeansException;

    Object resolveDependency(DependencyDescriptor dependencyDescriptor, String str) throws BeansException;

    Object resolveDependency(DependencyDescriptor dependencyDescriptor, String str, Set<String> set, TypeConverter typeConverter) throws BeansException;
}
