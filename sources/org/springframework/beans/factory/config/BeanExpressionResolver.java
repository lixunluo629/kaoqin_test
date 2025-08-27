package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/BeanExpressionResolver.class */
public interface BeanExpressionResolver {
    Object evaluate(String str, BeanExpressionContext beanExpressionContext) throws BeansException;
}
