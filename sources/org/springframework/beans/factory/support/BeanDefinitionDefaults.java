package org.springframework.beans.factory.support;

import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/BeanDefinitionDefaults.class */
public class BeanDefinitionDefaults {
    private boolean lazyInit;
    private int autowireMode = 0;
    private int dependencyCheck = 0;
    private String initMethodName;
    private String destroyMethodName;

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public boolean isLazyInit() {
        return this.lazyInit;
    }

    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    public int getAutowireMode() {
        return this.autowireMode;
    }

    public void setDependencyCheck(int dependencyCheck) {
        this.dependencyCheck = dependencyCheck;
    }

    public int getDependencyCheck() {
        return this.dependencyCheck;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = StringUtils.hasText(initMethodName) ? initMethodName : null;
    }

    public String getInitMethodName() {
        return this.initMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = StringUtils.hasText(destroyMethodName) ? destroyMethodName : null;
    }

    public String getDestroyMethodName() {
        return this.destroyMethodName;
    }
}
