package org.springframework.boot.autoconfigure.jdbc;

import java.sql.SQLException;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jdbc.pool.DataSourceProxy;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@EnableConfigurationProperties({DataSourceProperties.class})
@Configuration
@ConditionalOnClass({DataSource.class, EmbeddedDatabaseType.class})
@Import({DataSourceInitializerPostProcessor.Registrar.class, DataSourcePoolMetadataProvidersConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration.class */
public class DataSourceAutoConfiguration {
    private static final Log logger = LogFactory.getLog(DataSourceAutoConfiguration.class);

    @ConditionalOnMissingBean
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSourceProperties properties, ApplicationContext applicationContext) {
        return new DataSourceInitializer(properties, applicationContext);
    }

    public static boolean containsAutoConfiguredDataSource(ConfigurableListableBeanFactory beanFactory) {
        try {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition("dataSource");
            return EmbeddedDataSourceConfiguration.class.getName().equals(beanDefinition.getFactoryBeanName());
        } catch (NoSuchBeanDefinitionException e) {
            return false;
        }
    }

    @Configuration
    @ConditionalOnMissingBean({DataSource.class, XADataSource.class})
    @Conditional({EmbeddedDatabaseCondition.class})
    @Import({EmbeddedDataSourceConfiguration.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration$EmbeddedDatabaseConfiguration.class */
    protected static class EmbeddedDatabaseConfiguration {
        protected EmbeddedDatabaseConfiguration() {
        }
    }

    @Configuration
    @ConditionalOnMissingBean({DataSource.class, XADataSource.class})
    @Conditional({PooledDataSourceCondition.class})
    @Import({DataSourceConfiguration.Tomcat.class, DataSourceConfiguration.Hikari.class, DataSourceConfiguration.Dbcp.class, DataSourceConfiguration.Dbcp2.class, DataSourceConfiguration.Generic.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration$PooledDataSourceConfiguration.class */
    protected static class PooledDataSourceConfiguration {
        protected PooledDataSourceConfiguration() {
        }
    }

    @Configuration
    @ConditionalOnClass(name = {"org.apache.tomcat.jdbc.pool.DataSourceProxy"})
    @ConditionalOnMissingBean(name = {"dataSourceMBean"})
    @ConditionalOnProperty(prefix = "spring.datasource", name = {"jmx-enabled"})
    @Conditional({DataSourceAvailableCondition.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration$TomcatDataSourceJmxConfiguration.class */
    protected static class TomcatDataSourceJmxConfiguration {
        protected TomcatDataSourceJmxConfiguration() {
        }

        @Bean
        public Object dataSourceMBean(DataSource dataSource) {
            if (dataSource instanceof DataSourceProxy) {
                try {
                    return ((DataSourceProxy) dataSource).createPool().getJmxPool();
                } catch (SQLException e) {
                    DataSourceAutoConfiguration.logger.warn("Cannot expose DataSource to JMX (could not connect)");
                    return null;
                }
            }
            return null;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration$PooledDataSourceCondition.class */
    static class PooledDataSourceCondition extends AnyNestedCondition {
        PooledDataSourceCondition() {
            super(ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnProperty(prefix = "spring.datasource", name = {"type"})
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration$PooledDataSourceCondition$ExplicitType.class */
        static class ExplicitType {
            ExplicitType() {
            }
        }

        @Conditional({PooledDataSourceAvailableCondition.class})
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration$PooledDataSourceCondition$PooledDataSourceAvailable.class */
        static class PooledDataSourceAvailable {
            PooledDataSourceAvailable() {
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration$PooledDataSourceAvailableCondition.class */
    static class PooledDataSourceAvailableCondition extends SpringBootCondition {
        PooledDataSourceAvailableCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConditionMessage.Builder message = ConditionMessage.forCondition("PooledDataSource", new Object[0]);
            if (getDataSourceClassLoader(context) != null) {
                return ConditionOutcome.match(message.foundExactly("supported DataSource"));
            }
            return ConditionOutcome.noMatch(message.didNotFind("supported DataSource").atAll());
        }

        private ClassLoader getDataSourceClassLoader(ConditionContext context) {
            Class<?> dataSourceClass = new DataSourceBuilder(context.getClassLoader()).findType();
            if (dataSourceClass != null) {
                return dataSourceClass.getClassLoader();
            }
            return null;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration$EmbeddedDatabaseCondition.class */
    static class EmbeddedDatabaseCondition extends SpringBootCondition {
        private final SpringBootCondition pooledCondition = new PooledDataSourceCondition();

        EmbeddedDatabaseCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConditionMessage.Builder message = ConditionMessage.forCondition("EmbeddedDataSource", new Object[0]);
            if (anyMatches(context, metadata, this.pooledCondition)) {
                return ConditionOutcome.noMatch(message.foundExactly("supported pooled data source"));
            }
            EmbeddedDatabaseType type = EmbeddedDatabaseConnection.get(context.getClassLoader()).getType();
            return type == null ? ConditionOutcome.noMatch(message.didNotFind("embedded database").atAll()) : ConditionOutcome.match(message.found("embedded database").items(type));
        }
    }

    @Order(2147483637)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration$DataSourceAvailableCondition.class */
    static class DataSourceAvailableCondition extends SpringBootCondition {
        private final SpringBootCondition pooledCondition = new PooledDataSourceCondition();
        private final SpringBootCondition embeddedCondition = new EmbeddedDatabaseCondition();

        DataSourceAvailableCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConditionMessage.Builder message = ConditionMessage.forCondition("DataSourceAvailable", new Object[0]);
            if (hasBean(context, DataSource.class) || hasBean(context, XADataSource.class)) {
                return ConditionOutcome.match(message.foundExactly("existing data source bean"));
            }
            if (anyMatches(context, metadata, this.pooledCondition, this.embeddedCondition)) {
                return ConditionOutcome.match(message.foundExactly("existing auto-configured data source bean"));
            }
            return ConditionOutcome.noMatch(message.didNotFind("any existing data source bean").atAll());
        }

        private boolean hasBean(ConditionContext context, Class<?> type) {
            return BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getBeanFactory(), type, true, false).length > 0;
        }
    }
}
