package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/DestructionAwareBeanPostProcessor.class */
public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {
    void postProcessBeforeDestruction(Object obj, String str) throws BeansException;

    boolean requiresDestruction(Object obj);
}
