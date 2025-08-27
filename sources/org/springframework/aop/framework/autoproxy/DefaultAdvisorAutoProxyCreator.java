package org.springframework.aop.framework.autoproxy;

import org.springframework.beans.factory.BeanNameAware;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/autoproxy/DefaultAdvisorAutoProxyCreator.class */
public class DefaultAdvisorAutoProxyCreator extends AbstractAdvisorAutoProxyCreator implements BeanNameAware {
    public static final String SEPARATOR = ".";
    private boolean usePrefix = false;
    private String advisorBeanNamePrefix;

    public void setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
    }

    public boolean isUsePrefix() {
        return this.usePrefix;
    }

    public void setAdvisorBeanNamePrefix(String advisorBeanNamePrefix) {
        this.advisorBeanNamePrefix = advisorBeanNamePrefix;
    }

    public String getAdvisorBeanNamePrefix() {
        return this.advisorBeanNamePrefix;
    }

    @Override // org.springframework.beans.factory.BeanNameAware
    public void setBeanName(String name) {
        if (this.advisorBeanNamePrefix == null) {
            this.advisorBeanNamePrefix = name + ".";
        }
    }

    @Override // org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator
    protected boolean isEligibleAdvisorBean(String beanName) {
        return !isUsePrefix() || beanName.startsWith(getAdvisorBeanNamePrefix());
    }
}
