package org.springframework.aop.aspectj.annotation;

import java.io.Serializable;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/annotation/BeanFactoryAspectInstanceFactory.class */
public class BeanFactoryAspectInstanceFactory implements MetadataAwareAspectInstanceFactory, Serializable {
    private final BeanFactory beanFactory;
    private final String name;
    private final AspectMetadata aspectMetadata;

    public BeanFactoryAspectInstanceFactory(BeanFactory beanFactory, String name) {
        this(beanFactory, name, beanFactory.getType(name));
    }

    public BeanFactoryAspectInstanceFactory(BeanFactory beanFactory, String name, Class<?> type) {
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        Assert.notNull(name, "Bean name must not be null");
        this.beanFactory = beanFactory;
        this.name = name;
        this.aspectMetadata = new AspectMetadata(type, name);
    }

    @Override // org.springframework.aop.aspectj.AspectInstanceFactory
    public Object getAspectInstance() {
        return this.beanFactory.getBean(this.name);
    }

    @Override // org.springframework.aop.aspectj.AspectInstanceFactory
    public ClassLoader getAspectClassLoader() {
        if (this.beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) this.beanFactory).getBeanClassLoader();
        }
        return ClassUtils.getDefaultClassLoader();
    }

    @Override // org.springframework.aop.aspectj.annotation.MetadataAwareAspectInstanceFactory
    public AspectMetadata getAspectMetadata() {
        return this.aspectMetadata;
    }

    @Override // org.springframework.aop.aspectj.annotation.MetadataAwareAspectInstanceFactory
    public Object getAspectCreationMutex() {
        if (this.beanFactory != null) {
            if (this.beanFactory.isSingleton(this.name)) {
                return null;
            }
            if (this.beanFactory instanceof ConfigurableBeanFactory) {
                return ((ConfigurableBeanFactory) this.beanFactory).getSingletonMutex();
            }
        }
        return this;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() throws NoSuchBeanDefinitionException {
        Class<?> type = this.beanFactory.getType(this.name);
        if (type != null) {
            if (Ordered.class.isAssignableFrom(type) && this.beanFactory.isSingleton(this.name)) {
                return ((Ordered) this.beanFactory.getBean(this.name)).getOrder();
            }
            return OrderUtils.getOrder(type, Integer.MAX_VALUE).intValue();
        }
        return Integer.MAX_VALUE;
    }

    public String toString() {
        return getClass().getSimpleName() + ": bean name '" + this.name + "'";
    }
}
