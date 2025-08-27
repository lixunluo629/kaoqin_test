package org.springframework.beans.factory.serviceloader;

import java.util.ServiceLoader;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/serviceloader/AbstractServiceLoaderBasedFactoryBean.class */
public abstract class AbstractServiceLoaderBasedFactoryBean extends AbstractFactoryBean<Object> implements BeanClassLoaderAware {
    private Class<?> serviceType;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    protected abstract Object getObjectToExpose(ServiceLoader<?> serviceLoader);

    public void setServiceType(Class<?> serviceType) {
        this.serviceType = serviceType;
    }

    public Class<?> getServiceType() {
        return this.serviceType;
    }

    @Override // org.springframework.beans.factory.config.AbstractFactoryBean, org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override // org.springframework.beans.factory.config.AbstractFactoryBean
    protected Object createInstance() {
        Assert.notNull(getServiceType(), "Property 'serviceType' is required");
        return getObjectToExpose(ServiceLoader.load(getServiceType(), this.beanClassLoader));
    }
}
