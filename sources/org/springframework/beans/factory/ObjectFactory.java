package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/ObjectFactory.class */
public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}
