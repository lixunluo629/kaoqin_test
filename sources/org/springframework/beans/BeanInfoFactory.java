package org.springframework.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/BeanInfoFactory.class */
public interface BeanInfoFactory {
    BeanInfo getBeanInfo(Class<?> cls) throws IntrospectionException;
}
