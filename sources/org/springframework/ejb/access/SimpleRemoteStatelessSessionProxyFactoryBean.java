package org.springframework.ejb.access;

import javax.naming.NamingException;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ClassUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/ejb/access/SimpleRemoteStatelessSessionProxyFactoryBean.class */
public class SimpleRemoteStatelessSessionProxyFactoryBean extends SimpleRemoteSlsbInvokerInterceptor implements FactoryBean<Object>, BeanClassLoaderAware {
    private Class<?> businessInterface;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private Object proxy;

    public void setBusinessInterface(Class<?> businessInterface) {
        this.businessInterface = businessInterface;
    }

    public Class<?> getBusinessInterface() {
        return this.businessInterface;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override // org.springframework.ejb.access.AbstractSlsbInvokerInterceptor, org.springframework.jndi.JndiObjectLocator, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws NamingException {
        super.afterPropertiesSet();
        if (this.businessInterface == null) {
            throw new IllegalArgumentException("businessInterface is required");
        }
        this.proxy = new ProxyFactory(this.businessInterface, this).getProxy(this.beanClassLoader);
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Object getObject() {
        return this.proxy;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return this.businessInterface;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
