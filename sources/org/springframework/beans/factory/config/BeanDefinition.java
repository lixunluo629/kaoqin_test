package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/BeanDefinition.class */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {
    public static final String SCOPE_SINGLETON = "singleton";
    public static final String SCOPE_PROTOTYPE = "prototype";
    public static final int ROLE_APPLICATION = 0;
    public static final int ROLE_SUPPORT = 1;
    public static final int ROLE_INFRASTRUCTURE = 2;

    void setParentName(String str);

    String getParentName();

    void setBeanClassName(String str);

    String getBeanClassName();

    void setScope(String str);

    String getScope();

    void setLazyInit(boolean z);

    boolean isLazyInit();

    void setDependsOn(String... strArr);

    String[] getDependsOn();

    void setAutowireCandidate(boolean z);

    boolean isAutowireCandidate();

    void setPrimary(boolean z);

    boolean isPrimary();

    void setFactoryBeanName(String str);

    String getFactoryBeanName();

    void setFactoryMethodName(String str);

    String getFactoryMethodName();

    ConstructorArgumentValues getConstructorArgumentValues();

    MutablePropertyValues getPropertyValues();

    boolean isSingleton();

    boolean isPrototype();

    boolean isAbstract();

    int getRole();

    String getDescription();

    String getResourceDescription();

    BeanDefinition getOriginatingBeanDefinition();
}
