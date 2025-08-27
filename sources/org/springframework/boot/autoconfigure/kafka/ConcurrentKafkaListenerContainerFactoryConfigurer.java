package org.springframework.boot.autoconfigure.kafka;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.config.ContainerProperties;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/kafka/ConcurrentKafkaListenerContainerFactoryConfigurer.class */
public class ConcurrentKafkaListenerContainerFactoryConfigurer {
    private KafkaProperties properties;

    void setKafkaProperties(KafkaProperties properties) {
        this.properties = properties;
    }

    public void configure(ConcurrentKafkaListenerContainerFactory<Object, Object> listenerContainerFactory, ConsumerFactory<Object, Object> consumerFactory) {
        listenerContainerFactory.setConsumerFactory(consumerFactory);
        KafkaProperties.Listener container = this.properties.getListener();
        ContainerProperties containerProperties = listenerContainerFactory.getContainerProperties();
        if (container.getAckMode() != null) {
            containerProperties.setAckMode(container.getAckMode());
        }
        if (container.getAckCount() != null) {
            containerProperties.setAckCount(container.getAckCount().intValue());
        }
        if (container.getAckTime() != null) {
            containerProperties.setAckTime(container.getAckTime().longValue());
        }
        if (container.getPollTimeout() != null) {
            containerProperties.setPollTimeout(container.getPollTimeout().longValue());
        }
        if (container.getConcurrency() != null) {
            listenerContainerFactory.setConcurrency(container.getConcurrency());
        }
    }
}
