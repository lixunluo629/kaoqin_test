package org.springframework.boot.autoconfigure.amqp;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.util.Assert;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/SimpleRabbitListenerContainerFactoryConfigurer.class */
public final class SimpleRabbitListenerContainerFactoryConfigurer {
    private MessageConverter messageConverter;
    private MessageRecoverer messageRecoverer;
    private RabbitProperties rabbitProperties;

    void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    void setMessageRecoverer(MessageRecoverer messageRecoverer) {
        this.messageRecoverer = messageRecoverer;
    }

    void setRabbitProperties(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    public void configure(SimpleRabbitListenerContainerFactory factory, ConnectionFactory connectionFactory) {
        Assert.notNull(factory, "Factory must not be null");
        Assert.notNull(connectionFactory, "ConnectionFactory must not be null");
        factory.setConnectionFactory(connectionFactory);
        if (this.messageConverter != null) {
            factory.setMessageConverter(this.messageConverter);
        }
        RabbitProperties.AmqpContainer config = this.rabbitProperties.getListener().getSimple();
        factory.setAutoStartup(Boolean.valueOf(config.isAutoStartup()));
        if (config.getAcknowledgeMode() != null) {
            factory.setAcknowledgeMode(config.getAcknowledgeMode());
        }
        if (config.getConcurrency() != null) {
            factory.setConcurrentConsumers(config.getConcurrency());
        }
        if (config.getMaxConcurrency() != null) {
            factory.setMaxConcurrentConsumers(config.getMaxConcurrency());
        }
        if (config.getPrefetch() != null) {
            factory.setPrefetchCount(config.getPrefetch());
        }
        if (config.getTransactionSize() != null) {
            factory.setTxSize(config.getTransactionSize());
        }
        if (config.getDefaultRequeueRejected() != null) {
            factory.setDefaultRequeueRejected(config.getDefaultRequeueRejected());
        }
        if (config.getIdleEventInterval() != null) {
            factory.setIdleEventInterval(config.getIdleEventInterval());
        }
        RabbitProperties.ListenerRetry retryConfig = config.getRetry();
        if (retryConfig.isEnabled()) {
            RetryInterceptorBuilder.StatelessRetryInterceptorBuilder statelessRetryInterceptorBuilderStateless = retryConfig.isStateless() ? RetryInterceptorBuilder.stateless() : RetryInterceptorBuilder.stateful();
            statelessRetryInterceptorBuilderStateless.maxAttempts(retryConfig.getMaxAttempts());
            statelessRetryInterceptorBuilderStateless.backOffOptions(retryConfig.getInitialInterval(), retryConfig.getMultiplier(), retryConfig.getMaxInterval());
            MessageRecoverer recoverer = this.messageRecoverer != null ? this.messageRecoverer : new RejectAndDontRequeueRecoverer();
            statelessRetryInterceptorBuilderStateless.recoverer(recoverer);
            factory.setAdviceChain(new Advice[]{statelessRetryInterceptorBuilderStateless.build()});
        }
    }
}
