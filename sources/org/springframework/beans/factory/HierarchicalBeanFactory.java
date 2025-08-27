package org.springframework.beans.factory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/HierarchicalBeanFactory.class */
public interface HierarchicalBeanFactory extends BeanFactory {
    BeanFactory getParentBeanFactory();

    boolean containsLocalBean(String str);
}
