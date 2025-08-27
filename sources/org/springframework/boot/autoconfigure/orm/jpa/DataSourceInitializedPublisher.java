package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/DataSourceInitializedPublisher.class */
class DataSourceInitializedPublisher implements BeanPostProcessor {

    @Autowired
    private ApplicationContext applicationContext;
    private DataSource dataSource;
    private JpaProperties properties;

    DataSourceInitializedPublisher() {
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            this.dataSource = (DataSource) bean;
        }
        if (bean instanceof JpaProperties) {
            this.properties = (JpaProperties) bean;
        }
        if (bean instanceof EntityManagerFactory) {
            publishEventIfRequired((EntityManagerFactory) bean);
        }
        return bean;
    }

    private void publishEventIfRequired(EntityManagerFactory entityManagerFactory) {
        DataSource dataSource = findDataSource(entityManagerFactory);
        if (dataSource != null && isInitializingDatabase(dataSource)) {
            this.applicationContext.publishEvent((ApplicationEvent) new DataSourceInitializedEvent(dataSource));
        }
    }

    private DataSource findDataSource(EntityManagerFactory entityManagerFactory) {
        Object dataSource = entityManagerFactory.getProperties().get("javax.persistence.nonJtaDataSource");
        return (dataSource == null || !(dataSource instanceof DataSource)) ? this.dataSource : (DataSource) dataSource;
    }

    private boolean isInitializingDatabase(DataSource dataSource) {
        if (this.properties == null) {
            return true;
        }
        Map<String, String> hibernate = this.properties.getHibernateProperties(dataSource);
        if (hibernate.containsKey("hibernate.hbm2ddl.auto")) {
            return true;
        }
        return false;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/DataSourceInitializedPublisher$Registrar.class */
    static class Registrar implements ImportBeanDefinitionRegistrar {
        private static final String BEAN_NAME = "dataSourceInitializedPublisher";

        Registrar() {
        }

        @Override // org.springframework.context.annotation.ImportBeanDefinitionRegistrar
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
            if (!registry.containsBeanDefinition(BEAN_NAME)) {
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(DataSourceInitializedPublisher.class);
                beanDefinition.setRole(2);
                beanDefinition.setSynthetic(true);
                registry.registerBeanDefinition(BEAN_NAME, beanDefinition);
            }
        }
    }
}
