package org.springframework.boot.autoconfigure.integration;

import javax.management.MBeanServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.integration.jmx.config.EnableIntegrationMBeanExport;
import org.springframework.integration.monitor.IntegrationMBeanExporter;
import org.springframework.integration.support.management.IntegrationManagementConfigurer;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnClass({EnableIntegration.class})
@AutoConfigureAfter({JmxAutoConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration.class */
public class IntegrationAutoConfiguration {

    @Configuration
    @EnableIntegration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationConfiguration.class */
    protected static class IntegrationConfiguration {
        protected IntegrationConfiguration() {
        }
    }

    @Configuration
    @ConditionalOnClass({EnableIntegrationMBeanExport.class})
    @ConditionalOnMissingBean(value = {IntegrationMBeanExporter.class}, search = SearchStrategy.CURRENT)
    @ConditionalOnBean({MBeanServer.class})
    @ConditionalOnProperty(prefix = "spring.jmx", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationJmxConfiguration.class */
    protected static class IntegrationJmxConfiguration implements EnvironmentAware, BeanFactoryAware {
        private BeanFactory beanFactory;
        private RelaxedPropertyResolver propertyResolver;

        protected IntegrationJmxConfiguration() {
        }

        @Override // org.springframework.beans.factory.BeanFactoryAware
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override // org.springframework.context.EnvironmentAware
        public void setEnvironment(Environment environment) {
            this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.jmx.");
        }

        @Bean
        public IntegrationMBeanExporter integrationMbeanExporter() {
            IntegrationMBeanExporter exporter = new IntegrationMBeanExporter();
            String defaultDomain = this.propertyResolver.getProperty("default-domain");
            if (StringUtils.hasLength(defaultDomain)) {
                exporter.setDefaultDomain(defaultDomain);
            }
            String server = this.propertyResolver.getProperty("server", "mbeanServer");
            if (StringUtils.hasLength(server)) {
                exporter.setServer((MBeanServer) this.beanFactory.getBean(server, MBeanServer.class));
            }
            return exporter;
        }
    }

    @Configuration
    @ConditionalOnClass({EnableIntegrationManagement.class, EnableIntegrationMBeanExport.class})
    @ConditionalOnMissingBean(value = {IntegrationManagementConfigurer.class}, name = {"integrationManagementConfigurer"}, search = SearchStrategy.CURRENT)
    @ConditionalOnProperty(prefix = "spring.jmx", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationManagementConfiguration.class */
    protected static class IntegrationManagementConfiguration {
        protected IntegrationManagementConfiguration() {
        }

        @EnableIntegrationManagement(defaultCountsEnabled = "true", defaultStatsEnabled = "true")
        @Configuration
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationManagementConfiguration$EnableIntegrationManagementConfiguration.class */
        protected static class EnableIntegrationManagementConfiguration {
            protected EnableIntegrationManagementConfiguration() {
            }
        }
    }

    @ConditionalOnMissingBean({GatewayProxyFactoryBean.class})
    @Import({IntegrationAutoConfigurationScanRegistrar.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfiguration$IntegrationComponentScanAutoConfiguration.class */
    protected static class IntegrationComponentScanAutoConfiguration {
        protected IntegrationComponentScanAutoConfiguration() {
        }
    }
}
