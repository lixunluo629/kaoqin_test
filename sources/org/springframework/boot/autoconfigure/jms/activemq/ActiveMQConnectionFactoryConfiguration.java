package org.springframework.boot.autoconfigure.jms.activemq;

import java.util.List;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.catalina.Lifecycle;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnMissingBean({ConnectionFactory.class})
@Configuration
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/activemq/ActiveMQConnectionFactoryConfiguration.class */
class ActiveMQConnectionFactoryConfiguration {
    ActiveMQConnectionFactoryConfiguration() {
    }

    @ConditionalOnProperty(prefix = "spring.activemq.pool", name = {"enabled"}, havingValue = "false", matchIfMissing = true)
    @Bean
    public ActiveMQConnectionFactory jmsConnectionFactory(ActiveMQProperties properties, ObjectProvider<List<ActiveMQConnectionFactoryCustomizer>> factoryCustomizers) {
        return new ActiveMQConnectionFactoryFactory(properties, factoryCustomizers.getIfAvailable()).createConnectionFactory(ActiveMQConnectionFactory.class);
    }

    @Configuration
    @ConditionalOnClass({PooledConnectionFactory.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/activemq/ActiveMQConnectionFactoryConfiguration$PooledConnectionFactoryConfiguration.class */
    static class PooledConnectionFactoryConfiguration {
        PooledConnectionFactoryConfiguration() {
        }

        @ConfigurationProperties(prefix = "spring.activemq.pool.configuration")
        @ConditionalOnProperty(prefix = "spring.activemq.pool", name = {"enabled"}, havingValue = "true", matchIfMissing = false)
        @Bean(destroyMethod = Lifecycle.STOP_EVENT)
        public PooledConnectionFactory pooledJmsConnectionFactory(ActiveMQProperties properties, ObjectProvider<List<ActiveMQConnectionFactoryCustomizer>> factoryCustomizers) {
            PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(new ActiveMQConnectionFactoryFactory(properties, factoryCustomizers.getIfAvailable()).createConnectionFactory(ActiveMQConnectionFactory.class));
            ActiveMQProperties.Pool pool = properties.getPool();
            pooledConnectionFactory.setBlockIfSessionPoolIsFull(pool.isBlockIfFull());
            pooledConnectionFactory.setBlockIfSessionPoolIsFullTimeout(pool.getBlockIfFullTimeout());
            pooledConnectionFactory.setCreateConnectionOnStartup(pool.isCreateConnectionOnStartup());
            pooledConnectionFactory.setExpiryTimeout(pool.getExpiryTimeout());
            pooledConnectionFactory.setIdleTimeout(pool.getIdleTimeout());
            pooledConnectionFactory.setMaxConnections(pool.getMaxConnections());
            pooledConnectionFactory.setMaximumActiveSessionPerConnection(pool.getMaximumActiveSessionPerConnection());
            pooledConnectionFactory.setReconnectOnException(pool.isReconnectOnException());
            pooledConnectionFactory.setTimeBetweenExpirationCheckMillis(pool.getTimeBetweenExpirationCheck());
            pooledConnectionFactory.setUseAnonymousProducers(pool.isUseAnonymousProducers());
            return pooledConnectionFactory;
        }
    }
}
