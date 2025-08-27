package org.springframework.beans.factory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/SmartFactoryBean.class */
public interface SmartFactoryBean<T> extends FactoryBean<T> {
    boolean isPrototype();

    boolean isEagerInit();
}
