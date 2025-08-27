package org.springframework.boot.jta.atomikos;

import javax.jms.ConnectionFactory;
import javax.jms.XAConnectionFactory;
import org.springframework.boot.jta.XAConnectionFactoryWrapper;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/atomikos/AtomikosXAConnectionFactoryWrapper.class */
public class AtomikosXAConnectionFactoryWrapper implements XAConnectionFactoryWrapper {
    @Override // org.springframework.boot.jta.XAConnectionFactoryWrapper
    public ConnectionFactory wrapConnectionFactory(XAConnectionFactory connectionFactory) {
        AtomikosConnectionFactoryBean bean = new AtomikosConnectionFactoryBean();
        bean.setXaConnectionFactory(connectionFactory);
        return bean;
    }
}
