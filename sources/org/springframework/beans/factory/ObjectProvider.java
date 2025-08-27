package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/ObjectProvider.class */
public interface ObjectProvider<T> extends ObjectFactory<T> {
    T getObject(Object... objArr) throws BeansException;

    T getIfAvailable() throws BeansException;

    T getIfUnique() throws BeansException;
}
