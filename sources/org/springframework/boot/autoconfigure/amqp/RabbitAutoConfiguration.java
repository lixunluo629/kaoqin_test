package org.springframework.boot.autoconfigure.amqp;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ReflectionUtils;

@EnableConfigurationProperties({RabbitProperties.class})
@Configuration
@ConditionalOnClass({RabbitTemplate.class, Channel.class})
@Import({RabbitAnnotationDrivenConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitAutoConfiguration.class */
public class RabbitAutoConfiguration {

    @ConditionalOnMissingBean({ConnectionFactory.class})
    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitAutoConfiguration$RabbitConnectionFactoryCreator.class */
    protected static class RabbitConnectionFactoryCreator {
        private static final boolean CAN_ENABLE_HOSTNAME_VERIFICATION;

        protected RabbitConnectionFactoryCreator() {
        }

        static {
            CAN_ENABLE_HOSTNAME_VERIFICATION = ReflectionUtils.findMethod(com.rabbitmq.client.ConnectionFactory.class, "enableHostnameVerification") != null;
        }

        @Bean
        public CachingConnectionFactory rabbitConnectionFactory(RabbitProperties config) throws Exception {
            RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
            if (config.determineHost() != null) {
                factory.setHost(config.determineHost());
            }
            factory.setPort(config.determinePort());
            if (config.determineUsername() != null) {
                factory.setUsername(config.determineUsername());
            }
            if (config.determinePassword() != null) {
                factory.setPassword(config.determinePassword());
            }
            if (config.determineVirtualHost() != null) {
                factory.setVirtualHost(config.determineVirtualHost());
            }
            if (config.getRequestedHeartbeat() != null) {
                factory.setRequestedHeartbeat(config.getRequestedHeartbeat().intValue());
            }
            RabbitProperties.Ssl ssl = config.getSsl();
            if (ssl.isEnabled()) {
                factory.setUseSSL(true);
                if (ssl.getAlgorithm() != null) {
                    factory.setSslAlgorithm(ssl.getAlgorithm());
                }
                factory.setKeyStore(ssl.getKeyStore());
                factory.setKeyStorePassphrase(ssl.getKeyStorePassword());
                factory.setTrustStore(ssl.getTrustStore());
                factory.setTrustStorePassphrase(ssl.getTrustStorePassword());
                factory.setSkipServerCertificateValidation(!ssl.isValidateServerCertificate());
                if (ssl.getVerifyHostname() != null) {
                    factory.setEnableHostnameVerification(ssl.getVerifyHostname().booleanValue());
                } else if (CAN_ENABLE_HOSTNAME_VERIFICATION) {
                    factory.setEnableHostnameVerification(true);
                }
            }
            if (config.getConnectionTimeout() != null) {
                factory.setConnectionTimeout(config.getConnectionTimeout().intValue());
            }
            factory.afterPropertiesSet();
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory((com.rabbitmq.client.ConnectionFactory) factory.getObject());
            connectionFactory.setAddresses(config.determineAddresses());
            connectionFactory.setPublisherConfirms(config.isPublisherConfirms());
            connectionFactory.setPublisherReturns(config.isPublisherReturns());
            if (config.getCache().getChannel().getSize() != null) {
                connectionFactory.setChannelCacheSize(config.getCache().getChannel().getSize().intValue());
            }
            if (config.getCache().getConnection().getMode() != null) {
                connectionFactory.setCacheMode(config.getCache().getConnection().getMode());
            }
            if (config.getCache().getConnection().getSize() != null) {
                connectionFactory.setConnectionCacheSize(config.getCache().getConnection().getSize().intValue());
            }
            if (config.getCache().getChannel().getCheckoutTimeout() != null) {
                connectionFactory.setChannelCheckoutTimeout(config.getCache().getChannel().getCheckoutTimeout().longValue());
            }
            return connectionFactory;
        }
    }

    @Configuration
    @Import({RabbitConnectionFactoryCreator.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitAutoConfiguration$RabbitTemplateConfiguration.class */
    protected static class RabbitTemplateConfiguration {
        private final ObjectProvider<MessageConverter> messageConverter;
        private final RabbitProperties properties;

        public RabbitTemplateConfiguration(ObjectProvider<MessageConverter> messageConverter, RabbitProperties properties) {
            this.messageConverter = messageConverter;
            this.properties = properties;
        }

        @ConditionalOnMissingBean({RabbitTemplate.class})
        @Bean
        @ConditionalOnSingleCandidate(ConnectionFactory.class)
        public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) throws BeansException {
            RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
            MessageConverter messageConverter = this.messageConverter.getIfUnique();
            if (messageConverter != null) {
                rabbitTemplate.setMessageConverter(messageConverter);
            }
            rabbitTemplate.setMandatory(determineMandatoryFlag());
            RabbitProperties.Template templateProperties = this.properties.getTemplate();
            RabbitProperties.Retry retryProperties = templateProperties.getRetry();
            if (retryProperties.isEnabled()) {
                rabbitTemplate.setRetryTemplate(createRetryTemplate(retryProperties));
            }
            if (templateProperties.getReceiveTimeout() != null) {
                rabbitTemplate.setReceiveTimeout(templateProperties.getReceiveTimeout().longValue());
            }
            if (templateProperties.getReplyTimeout() != null) {
                rabbitTemplate.setReplyTimeout(templateProperties.getReplyTimeout().longValue());
            }
            return rabbitTemplate;
        }

        private boolean determineMandatoryFlag() {
            Boolean mandatory = this.properties.getTemplate().getMandatory();
            return mandatory != null ? mandatory.booleanValue() : this.properties.isPublisherReturns();
        }

        private RetryTemplate createRetryTemplate(RabbitProperties.Retry properties) {
            RetryTemplate template = new RetryTemplate();
            SimpleRetryPolicy policy = new SimpleRetryPolicy();
            policy.setMaxAttempts(properties.getMaxAttempts());
            template.setRetryPolicy(policy);
            ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
            backOffPolicy.setInitialInterval(properties.getInitialInterval());
            backOffPolicy.setMultiplier(properties.getMultiplier());
            backOffPolicy.setMaxInterval(properties.getMaxInterval());
            template.setBackOffPolicy(backOffPolicy);
            return template;
        }

        @ConditionalOnSingleCandidate(ConnectionFactory.class)
        @ConditionalOnMissingBean({AmqpAdmin.class})
        @ConditionalOnProperty(prefix = "spring.rabbitmq", name = {"dynamic"}, matchIfMissing = true)
        @Bean
        public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
            return new RabbitAdmin(connectionFactory);
        }
    }

    @Configuration
    @ConditionalOnClass({RabbitMessagingTemplate.class})
    @ConditionalOnMissingBean({RabbitMessagingTemplate.class})
    @Import({RabbitTemplateConfiguration.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitAutoConfiguration$MessagingTemplateConfiguration.class */
    protected static class MessagingTemplateConfiguration {
        protected MessagingTemplateConfiguration() {
        }

        @Bean
        @ConditionalOnSingleCandidate(RabbitTemplate.class)
        public RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate) {
            return new RabbitMessagingTemplate(rabbitTemplate);
        }
    }
}
