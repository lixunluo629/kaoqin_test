package org.springframework.boot.jta.bitronix;

import javax.jms.ConnectionFactory;
import javax.jms.XAConnectionFactory;
import org.springframework.boot.jta.XAConnectionFactoryWrapper;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/bitronix/BitronixXAConnectionFactoryWrapper.class */
public class BitronixXAConnectionFactoryWrapper implements XAConnectionFactoryWrapper {
    @Override // org.springframework.boot.jta.XAConnectionFactoryWrapper
    public ConnectionFactory wrapConnectionFactory(XAConnectionFactory connectionFactory) {
        PoolingConnectionFactoryBean pool = new PoolingConnectionFactoryBean();
        pool.setConnectionFactory(connectionFactory);
        return pool;
    }
}
