package org.springframework.boot.autoconfigure.jmx;

import javax.management.MBeanServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.MBeanExportConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jmx.MBeanServerNotFoundException;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.export.naming.ObjectNamingStrategy;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnClass({MBeanExporter.class})
@ConditionalOnProperty(prefix = "spring.jmx", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jmx/JmxAutoConfiguration.class */
public class JmxAutoConfiguration implements EnvironmentAware, BeanFactoryAware {
    private RelaxedPropertyResolver propertyResolver;
    private BeanFactory beanFactory;

    @Override // org.springframework.context.EnvironmentAware
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.jmx.");
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @ConditionalOnMissingBean(value = {MBeanExporter.class}, search = SearchStrategy.CURRENT)
    @Bean
    @Primary
    public AnnotationMBeanExporter mbeanExporter(ObjectNamingStrategy namingStrategy) {
        AnnotationMBeanExporter exporter = new AnnotationMBeanExporter();
        exporter.setRegistrationPolicy(RegistrationPolicy.FAIL_ON_EXISTING);
        exporter.setNamingStrategy(namingStrategy);
        String server = this.propertyResolver.getProperty("server", "mbeanServer");
        if (StringUtils.hasLength(server)) {
            exporter.setServer((MBeanServer) this.beanFactory.getBean(server, MBeanServer.class));
        }
        return exporter;
    }

    @ConditionalOnMissingBean(value = {ObjectNamingStrategy.class}, search = SearchStrategy.CURRENT)
    @Bean
    public ParentAwareNamingStrategy objectNamingStrategy() {
        ParentAwareNamingStrategy namingStrategy = new ParentAwareNamingStrategy(new AnnotationJmxAttributeSource());
        String defaultDomain = this.propertyResolver.getProperty("default-domain");
        if (StringUtils.hasLength(defaultDomain)) {
            namingStrategy.setDefaultDomain(defaultDomain);
        }
        return namingStrategy;
    }

    @ConditionalOnMissingBean({MBeanServer.class})
    @Bean
    public MBeanServer mbeanServer() throws MBeanServerNotFoundException {
        MBeanExportConfiguration.SpecificPlatform platform = MBeanExportConfiguration.SpecificPlatform.get();
        if (platform != null) {
            return platform.getMBeanServer();
        }
        MBeanServerFactoryBean factory = new MBeanServerFactoryBean();
        factory.setLocateExistingServerIfPossible(true);
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
