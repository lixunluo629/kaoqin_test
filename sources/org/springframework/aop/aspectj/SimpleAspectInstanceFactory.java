package org.springframework.aop.aspectj;

import org.springframework.aop.framework.AopConfigException;
import org.springframework.util.Assert;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/aspectj/SimpleAspectInstanceFactory.class */
public class SimpleAspectInstanceFactory implements AspectInstanceFactory {
    private final Class<?> aspectClass;

    public SimpleAspectInstanceFactory(Class<?> aspectClass) {
        Assert.notNull(aspectClass, "Aspect class must not be null");
        this.aspectClass = aspectClass;
    }

    public final Class<?> getAspectClass() {
        return this.aspectClass;
    }

    @Override // org.springframework.aop.aspectj.AspectInstanceFactory
    public final Object getAspectInstance() {
        try {
            return this.aspectClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new AopConfigException("Could not access aspect constructor: " + this.aspectClass.getName(), ex);
        } catch (InstantiationException ex2) {
            throw new AopConfigException("Unable to instantiate aspect class: " + this.aspectClass.getName(), ex2);
        }
    }

    @Override // org.springframework.aop.aspectj.AspectInstanceFactory
    public ClassLoader getAspectClassLoader() {
        return this.aspectClass.getClassLoader();
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return getOrderForAspectClass(this.aspectClass);
    }

    protected int getOrderForAspectClass(Class<?> aspectClass) {
        return Integer.MAX_VALUE;
    }
}
