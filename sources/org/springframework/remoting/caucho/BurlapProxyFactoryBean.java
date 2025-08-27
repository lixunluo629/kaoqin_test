package org.springframework.remoting.caucho;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.remoting.RemoteLookupFailureException;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/remoting/caucho/BurlapProxyFactoryBean.class */
public class BurlapProxyFactoryBean extends BurlapClientInterceptor implements FactoryBean<Object> {
    private Object serviceProxy;

    @Override // org.springframework.remoting.caucho.BurlapClientInterceptor, org.springframework.remoting.support.UrlBasedRemoteAccessor, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws RemoteLookupFailureException {
        super.afterPropertiesSet();
        this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
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
