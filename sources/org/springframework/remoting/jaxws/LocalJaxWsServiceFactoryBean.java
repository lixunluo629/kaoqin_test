package org.springframework.remoting.jaxws;

import javax.xml.ws.Service;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/remoting/jaxws/LocalJaxWsServiceFactoryBean.class */
public class LocalJaxWsServiceFactoryBean extends LocalJaxWsServiceFactory implements FactoryBean<Service>, InitializingBean {
    private Service service;

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        this.service = createJaxWsService();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public Service getObject() {
        return this.service;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<? extends Service> getObjectType() {
        return this.service != null ? this.service.getClass() : Service.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
