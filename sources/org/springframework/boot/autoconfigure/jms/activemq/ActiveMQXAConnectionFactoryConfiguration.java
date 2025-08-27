package org.springframework.boot.autoconfigure.jms.activemq;

import java.util.List;
import javax.jms.ConnectionFactory;
import javax.transaction.TransactionManager;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jta.XAConnectionFactoryWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnClass({TransactionManager.class})
@ConditionalOnMissingBean({ConnectionFactory.class})
@ConditionalOnBean({XAConnectionFactoryWrapper.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/activemq/ActiveMQXAConnectionFactoryConfiguration.class */
class ActiveMQXAConnectionFactoryConfiguration {
    ActiveMQXAConnectionFactoryConfiguration() {
    }

    @Primary
    @Bean(name = {"jmsConnectionFactory", "xaJmsConnectionFactory"})
    public ConnectionFactory jmsConnectionFactory(ActiveMQProperties properties, ObjectProvider<List<ActiveMQConnectionFactoryCustomizer>> factoryCustomizers, XAConnectionFactoryWrapper wrapper) throws Exception {
        ActiveMQXAConnectionFactory connectionFactory = new ActiveMQConnectionFactoryFactory(properties, factoryCustomizers.getIfAvailable()).createConnectionFactory(ActiveMQXAConnectionFactory.class);
        return wrapper.wrapConnectionFactory(connectionFactory);
    }

    @ConditionalOnProperty(prefix = "spring.activemq.pool", name = {"enabled"}, havingValue = "false", matchIfMissing = true)
    @Bean
    public ActiveMQConnectionFactory nonXaJmsConnectionFactory(ActiveMQProperties properties, ObjectProvider<List<ActiveMQConnectionFactoryCustomizer>> factoryCustomizers) {
        return new ActiveMQConnectionFactoryFactory(properties, factoryCustomizers.getIfAvailable()).createConnectionFactory(ActiveMQConnectionFactory.class);
    }
}
