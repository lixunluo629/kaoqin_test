package org.springframework.beans.factory.access;

import org.springframework.beans.factory.BeanFactory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/access/BeanFactoryReference.class */
public interface BeanFactoryReference {
    BeanFactory getFactory();

    void release();
}
