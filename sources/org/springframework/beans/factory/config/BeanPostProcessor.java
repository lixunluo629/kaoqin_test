package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/BeanPostProcessor.class */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object obj, String str) throws BeansException;

    Object postProcessAfterInitialization(Object obj, String str) throws BeansException;
}
