package org.springframework.beans.factory.access;

import org.springframework.beans.BeansException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/access/BeanFactoryLocator.class */
public interface BeanFactoryLocator {
    BeanFactoryReference useBeanFactory(String str) throws BeansException;
}
