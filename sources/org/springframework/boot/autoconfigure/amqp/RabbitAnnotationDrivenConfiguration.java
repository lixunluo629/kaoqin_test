package org.springframework.boot.autoconfigure.amqp;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({EnableRabbit.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitAnnotationDrivenConfiguration.class */
class RabbitAnnotationDrivenConfiguration {
    private final ObjectProvider<MessageConverter> messageConverter;
    private final ObjectProvider<MessageRecoverer> messageRecoverer;
    private final RabbitProperties properties;

    RabbitAnnotationDrivenConfiguration(ObjectProvider<MessageConverter> messageConverter, ObjectProvider<MessageRecoverer> messageRecoverer, RabbitProperties properties) {
        this.messageConverter = messageConverter;
        this.messageRecoverer = messageRecoverer;
        this.properties = properties;
    }

    @ConditionalOnMissingBean
    @Bean
    public SimpleRabbitListenerContainerFactoryConfigurer rabbitListenerContainerFactoryConfigurer() {
        SimpleRabbitListenerContainerFactoryConfigurer configurer = new SimpleRabbitListenerContainerFactoryConfigurer();
        configurer.setMessageConverter(this.messageConverter.getIfUnique());
        configurer.setMessageRecoverer(this.messageRecoverer.getIfUnique());
        configurer.setRabbitProperties(this.properties);
        return configurer;
    }

    @ConditionalOnMissingBean(name = {"rabbitListenerContainerFactory"})
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @ConditionalOnMissingBean(name = {"org.springframework.amqp.rabbit.config.internalRabbitListenerAnnotationProcessor"})
    @EnableRabbit
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitAnnotationDrivenConfiguration$EnableRabbitConfiguration.class */
    protected static class EnableRabbitConfiguration {
        protected EnableRabbitConfiguration() {
        }
    }
}
