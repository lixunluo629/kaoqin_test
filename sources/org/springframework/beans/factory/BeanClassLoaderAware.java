package org.springframework.beans.factory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/BeanClassLoaderAware.class */
public interface BeanClassLoaderAware extends Aware {
    void setBeanClassLoader(ClassLoader classLoader);
}
