package org.springframework.boot.autoconfigure.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
@ConditionalOnClass({EnableKafka.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/kafka/KafkaAnnotationDrivenConfiguration.class */
class KafkaAnnotationDrivenConfiguration {
    private final KafkaProperties properties;

    KafkaAnnotationDrivenConfiguration(KafkaProperties properties) {
        this.properties = properties;
    }

    @ConditionalOnMissingBean
    @Bean
    public ConcurrentKafkaListenerContainerFactoryConfigurer kafkaListenerContainerFactoryConfigurer() {
        ConcurrentKafkaListenerContainerFactoryConfigurer configurer = new ConcurrentKafkaListenerContainerFactoryConfigurer();
        configurer.setKafkaProperties(this.properties);
        return configurer;
    }

    @ConditionalOnMissingBean(name = {"kafkaListenerContainerFactory"})
    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer, ConsumerFactory<Object, Object> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);
        return factory;
    }

    @EnableKafka
    @ConditionalOnMissingBean(name = {"org.springframework.kafka.config.internalKafkaListenerAnnotationProcessor"})
    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/kafka/KafkaAnnotationDrivenConfiguration$EnableKafkaConfiguration.class */
    protected static class EnableKafkaConfiguration {
        protected EnableKafkaConfiguration() {
        }
    }
}
