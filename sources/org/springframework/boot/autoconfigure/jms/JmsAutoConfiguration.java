package org.springframework.boot.autoconfigure.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Message;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.destination.DestinationResolver;

@EnableConfigurationProperties({JmsProperties.class})
@Configuration
@ConditionalOnClass({Message.class, JmsTemplate.class})
@ConditionalOnBean({ConnectionFactory.class})
@Import({JmsAnnotationDrivenConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/JmsAutoConfiguration.class */
public class JmsAutoConfiguration {

    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/JmsAutoConfiguration$JmsTemplateConfiguration.class */
    protected static class JmsTemplateConfiguration {
        private final JmsProperties properties;
        private final ObjectProvider<DestinationResolver> destinationResolver;
        private final ObjectProvider<MessageConverter> messageConverter;

        public JmsTemplateConfiguration(JmsProperties properties, ObjectProvider<DestinationResolver> destinationResolver, ObjectProvider<MessageConverter> messageConverter) {
            this.properties = properties;
            this.destinationResolver = destinationResolver;
            this.messageConverter = messageConverter;
        }

        @ConditionalOnMissingBean
        @Bean
        @ConditionalOnSingleCandidate(ConnectionFactory.class)
        public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) throws BeansException {
            JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
            jmsTemplate.setPubSubDomain(this.properties.isPubSubDomain());
            DestinationResolver destinationResolver = this.destinationResolver.getIfUnique();
            if (destinationResolver != null) {
                jmsTemplate.setDestinationResolver(destinationResolver);
            }
            MessageConverter messageConverter = this.messageConverter.getIfUnique();
            if (messageConverter != null) {
                jmsTemplate.setMessageConverter(messageConverter);
            }
            JmsProperties.Template template = this.properties.getTemplate();
            if (template.getDefaultDestination() != null) {
                jmsTemplate.setDefaultDestinationName(template.getDefaultDestination());
            }
            if (template.getDeliveryDelay() != null) {
                jmsTemplate.setDeliveryDelay(template.getDeliveryDelay().longValue());
            }
            jmsTemplate.setExplicitQosEnabled(template.determineQosEnabled());
            if (template.getDeliveryMode() != null) {
                jmsTemplate.setDeliveryMode(template.getDeliveryMode().getValue());
            }
            if (template.getPriority() != null) {
                jmsTemplate.setPriority(template.getPriority().intValue());
            }
            if (template.getTimeToLive() != null) {
                jmsTemplate.setTimeToLive(template.getTimeToLive().longValue());
            }
            if (template.getReceiveTimeout() != null) {
                jmsTemplate.setReceiveTimeout(template.getReceiveTimeout().longValue());
            }
            return jmsTemplate;
        }
    }

    @Configuration
    @ConditionalOnClass({JmsMessagingTemplate.class})
    @Import({JmsTemplateConfiguration.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/JmsAutoConfiguration$MessagingTemplateConfiguration.class */
    protected static class MessagingTemplateConfiguration {
        protected MessagingTemplateConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        @ConditionalOnSingleCandidate(JmsTemplate.class)
        public JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
            return new JmsMessagingTemplate(jmsTemplate);
        }
    }
}
