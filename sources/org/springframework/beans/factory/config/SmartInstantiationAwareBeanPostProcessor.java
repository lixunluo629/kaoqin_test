package org.springframework.beans.factory.config;

import java.lang.reflect.Constructor;
import org.springframework.beans.BeansException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/SmartInstantiationAwareBeanPostProcessor.class */
public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {
    Class<?> predictBeanType(Class<?> cls, String str) throws BeansException;

    Constructor<?>[] determineCandidateConstructors(Class<?> cls, String str) throws BeansException;

    Object getEarlyBeanReference(Object obj, String str) throws BeansException;
}
