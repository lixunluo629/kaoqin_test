package org.springframework.boot.jta;

import javax.jms.ConnectionFactory;
import javax.jms.XAConnectionFactory;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/XAConnectionFactoryWrapper.class */
public interface XAConnectionFactoryWrapper {
    ConnectionFactory wrapConnectionFactory(XAConnectionFactory xAConnectionFactory) throws Exception;
}
