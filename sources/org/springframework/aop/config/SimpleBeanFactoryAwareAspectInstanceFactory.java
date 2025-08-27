package org.springframework.aop.config;

import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/config/SimpleBeanFactoryAwareAspectInstanceFactory.class */
public class SimpleBeanFactoryAwareAspectInstanceFactory implements AspectInstanceFactory, BeanFactoryAware {
    private String aspectBeanName;
    private BeanFactory beanFactory;

    public void setAspectBeanName(String aspectBeanName) {
        this.aspectBeanName = aspectBeanName;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        Assert.notNull(this.aspectBeanName, "'aspectBeanName' is required");
    }

    @Override // org.springframework.aop.aspectj.AspectInstanceFactory
    public Object getAspectInstance() {
        return this.beanFactory.getBean(this.aspectBeanName);
    }

    @Override // org.springframework.aop.aspectj.AspectInstanceFactory
    public ClassLoader getAspectClassLoader() {
        if (this.beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) this.beanFactory).getBeanClassLoader();
        }
        return ClassUtils.getDefaultClassLoader();
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        if (this.beanFactory.isSingleton(this.aspectBeanName) && this.beanFactory.isTypeMatch(this.aspectBeanName, Ordered.class)) {
            return ((Ordered) this.beanFactory.getBean(this.aspectBeanName)).getOrder();
        }
        return Integer.MAX_VALUE;
    }
}
