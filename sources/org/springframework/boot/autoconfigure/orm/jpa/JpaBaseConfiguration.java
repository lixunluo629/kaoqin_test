package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.boot.autoconfigure.orm.jpa.DataSourceInitializedPublisher;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableConfigurationProperties({JpaProperties.class})
@Import({DataSourceInitializedPublisher.Registrar.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/JpaBaseConfiguration.class */
public abstract class JpaBaseConfiguration implements BeanFactoryAware {
    private final DataSource dataSource;
    private final JpaProperties properties;
    private final JtaTransactionManager jtaTransactionManager;
    private final TransactionManagerCustomizers transactionManagerCustomizers;
    private ConfigurableListableBeanFactory beanFactory;

    protected abstract AbstractJpaVendorAdapter createJpaVendorAdapter();

    protected abstract Map<String, Object> getVendorProperties();

    protected JpaBaseConfiguration(DataSource dataSource, JpaProperties properties, ObjectProvider<JtaTransactionManager> jtaTransactionManager, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        this.dataSource = dataSource;
        this.properties = properties;
        this.jtaTransactionManager = jtaTransactionManager.getIfAvailable();
        this.transactionManagerCustomizers = transactionManagerCustomizers.getIfAvailable();
    }

    @ConditionalOnMissingBean({PlatformTransactionManager.class})
    @Bean
    public PlatformTransactionManager transactionManager() {
        PlatformTransactionManager jpaTransactionManager = new JpaTransactionManager();
        if (this.transactionManagerCustomizers != null) {
            this.transactionManagerCustomizers.customize(jpaTransactionManager);
        }
        return jpaTransactionManager;
    }

    @ConditionalOnMissingBean
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        AbstractJpaVendorAdapter adapter = createJpaVendorAdapter();
        adapter.setShowSql(this.properties.isShowSql());
        adapter.setDatabase(this.properties.determineDatabase(this.dataSource));
        adapter.setDatabasePlatform(this.properties.getDatabasePlatform());
        adapter.setGenerateDdl(this.properties.isGenerateDdl());
        return adapter;
    }

    @ConditionalOnMissingBean
    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter, ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter, this.properties.getProperties(), persistenceUnitManager.getIfAvailable());
        builder.setCallback(getVendorCallback());
        return builder;
    }

    @ConditionalOnMissingBean({LocalContainerEntityManagerFactoryBean.class, EntityManagerFactory.class})
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factoryBuilder) {
        Map<String, Object> vendorProperties = getVendorProperties();
        customizeVendorProperties(vendorProperties);
        return factoryBuilder.dataSource(this.dataSource).packages(getPackagesToScan()).properties(vendorProperties).jta(isJta()).build();
    }

    protected void customizeVendorProperties(Map<String, Object> vendorProperties) {
    }

    protected EntityManagerFactoryBuilder.EntityManagerFactoryBeanCallback getVendorCallback() {
        return null;
    }

    protected String[] getPackagesToScan() {
        List<String> packages = EntityScanPackages.get(this.beanFactory).getPackageNames();
        if (packages.isEmpty() && AutoConfigurationPackages.has(this.beanFactory)) {
            packages = AutoConfigurationPackages.get(this.beanFactory);
        }
        return (String[]) packages.toArray(new String[packages.size()]);
    }

    protected JtaTransactionManager getJtaTransactionManager() {
        return this.jtaTransactionManager;
    }

    protected final boolean isJta() {
        return this.jtaTransactionManager != null;
    }

    protected final JpaProperties getProperties() {
        return this.properties;
    }

    protected final DataSource getDataSource() {
        return this.dataSource;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Configuration
    @ConditionalOnClass({WebMvcConfigurerAdapter.class})
    @ConditionalOnMissingBean({OpenEntityManagerInViewInterceptor.class, OpenEntityManagerInViewFilter.class})
    @ConditionalOnProperty(prefix = "spring.jpa", name = {"open-in-view"}, havingValue = "true", matchIfMissing = true)
    @ConditionalOnWebApplication
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/JpaBaseConfiguration$JpaWebConfiguration.class */
    protected static class JpaWebConfiguration {
        protected JpaWebConfiguration() {
        }

        @Configuration
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/JpaBaseConfiguration$JpaWebConfiguration$JpaWebMvcConfiguration.class */
        protected static class JpaWebMvcConfiguration extends WebMvcConfigurerAdapter {
            protected JpaWebMvcConfiguration() {
            }

            @Bean
            public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
                return new OpenEntityManagerInViewInterceptor();
            }

            @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());
            }
        }
    }
}
