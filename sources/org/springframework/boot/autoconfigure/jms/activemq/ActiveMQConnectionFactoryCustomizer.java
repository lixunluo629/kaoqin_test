package org.springframework.boot.autoconfigure.jms.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/activemq/ActiveMQConnectionFactoryCustomizer.class */
public interface ActiveMQConnectionFactoryCustomizer {
    void customize(ActiveMQConnectionFactory activeMQConnectionFactory);
}
