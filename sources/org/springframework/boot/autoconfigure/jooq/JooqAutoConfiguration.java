package org.springframework.boot.autoconfigure.jooq;

import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.ExecuteListenerProvider;
import org.jooq.RecordListenerProvider;
import org.jooq.RecordMapperProvider;
import org.jooq.TransactionProvider;
import org.jooq.VisitListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnClass({DSLContext.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, TransactionAutoConfiguration.class})
@ConditionalOnBean({DataSource.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jooq/JooqAutoConfiguration.class */
public class JooqAutoConfiguration {
    @ConditionalOnMissingBean({DataSourceConnectionProvider.class})
    @Bean
    public DataSourceConnectionProvider dataSourceConnectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    @ConditionalOnBean({PlatformTransactionManager.class})
    @Bean
    public SpringTransactionProvider transactionProvider(PlatformTransactionManager txManager) {
        return new SpringTransactionProvider(txManager);
    }

    @Bean
    public DefaultExecuteListenerProvider jooqExceptionTranslatorExecuteListenerProvider() {
        return new DefaultExecuteListenerProvider(new JooqExceptionTranslator());
    }

    @ConditionalOnMissingBean({DSLContext.class})
    @EnableConfigurationProperties({JooqProperties.class})
    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jooq/JooqAutoConfiguration$DslContextConfiguration.class */
    public static class DslContextConfiguration {
        private final JooqProperties properties;
        private final ConnectionProvider connection;
        private final TransactionProvider transactionProvider;
        private final RecordMapperProvider recordMapperProvider;
        private final Settings settings;
        private final RecordListenerProvider[] recordListenerProviders;
        private final ExecuteListenerProvider[] executeListenerProviders;
        private final VisitListenerProvider[] visitListenerProviders;

        public DslContextConfiguration(JooqProperties properties, ConnectionProvider connectionProvider, ObjectProvider<TransactionProvider> transactionProvider, ObjectProvider<RecordMapperProvider> recordMapperProvider, ObjectProvider<Settings> settings, ObjectProvider<RecordListenerProvider[]> recordListenerProviders, ExecuteListenerProvider[] executeListenerProviders, ObjectProvider<VisitListenerProvider[]> visitListenerProviders) {
            this.properties = properties;
            this.connection = connectionProvider;
            this.transactionProvider = transactionProvider.getIfAvailable();
            this.recordMapperProvider = recordMapperProvider.getIfAvailable();
            this.settings = settings.getIfAvailable();
            this.recordListenerProviders = recordListenerProviders.getIfAvailable();
            this.executeListenerProviders = executeListenerProviders;
            this.visitListenerProviders = visitListenerProviders.getIfAvailable();
        }

        @Bean
        public DefaultDSLContext dslContext(org.jooq.Configuration configuration) {
            return new DefaultDSLContext(configuration);
        }

        @ConditionalOnMissingBean({org.jooq.Configuration.class})
        @Bean
        public DefaultConfiguration jooqConfiguration() {
            DefaultConfiguration configuration = new DefaultConfiguration();
            if (this.properties.getSqlDialect() != null) {
                configuration.set(this.properties.getSqlDialect());
            }
            configuration.set(this.connection);
            if (this.transactionProvider != null) {
                configuration.set(this.transactionProvider);
            }
            if (this.recordMapperProvider != null) {
                configuration.set(this.recordMapperProvider);
            }
            if (this.settings != null) {
                configuration.set(this.settings);
            }
            configuration.set(this.recordListenerProviders);
            configuration.set(this.executeListenerProviders);
            configuration.set(this.visitListenerProviders);
            return configuration;
        }
    }
}
