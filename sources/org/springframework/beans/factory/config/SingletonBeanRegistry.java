package org.springframework.beans.factory.config;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/SingletonBeanRegistry.class */
public interface SingletonBeanRegistry {
    void registerSingleton(String str, Object obj);

    Object getSingleton(String str);

    boolean containsSingleton(String str);

    String[] getSingletonNames();

    int getSingletonCount();

    Object getSingletonMutex();
}
