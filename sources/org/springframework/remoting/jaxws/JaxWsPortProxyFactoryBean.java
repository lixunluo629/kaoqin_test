package org.springframework.remoting.jaxws;

import javax.xml.ws.BindingProvider;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/remoting/jaxws/JaxWsPortProxyFactoryBean.class */
public class JaxWsPortProxyFactoryBean extends JaxWsPortClientInterceptor implements FactoryBean<Object> {
    private Object serviceProxy;

    @Override // org.springframework.remoting.jaxws.JaxWsPortClientInterceptor, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        ProxyFactory pf = new ProxyFactory();
        pf.addInterface(getServiceInterface());
        pf.addInterface(BindingProvider.class);
        pf.addAdvice(this);
        this.serviceProxy = pf.getProxy(getBeanClassLoader());
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Object getObject() {
        return this.serviceProxy;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return getServiceInterface();
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
