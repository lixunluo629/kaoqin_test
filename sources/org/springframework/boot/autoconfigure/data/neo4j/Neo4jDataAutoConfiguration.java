package org.springframework.boot.autoconfigure.data.neo4j;

import java.util.List;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.event.EventListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.web.support.OpenSessionInViewInterceptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableConfigurationProperties({Neo4jProperties.class})
@Configuration
@ConditionalOnClass({SessionFactory.class, Neo4jTransactionManager.class, PlatformTransactionManager.class})
@ConditionalOnMissingBean({SessionFactory.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/neo4j/Neo4jDataAutoConfiguration.class */
public class Neo4jDataAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public org.neo4j.ogm.config.Configuration configuration(Neo4jProperties properties) {
        return properties.createConfiguration();
    }

    @Bean
    public SessionFactory sessionFactory(org.neo4j.ogm.config.Configuration configuration, ApplicationContext applicationContext, ObjectProvider<List<EventListener>> eventListeners) throws BeansException {
        SessionFactory sessionFactory = new SessionFactory(configuration, getPackagesToScan(applicationContext));
        List<EventListener> providedEventListeners = eventListeners.getIfAvailable();
        if (providedEventListeners != null) {
            for (EventListener eventListener : providedEventListeners) {
                sessionFactory.register(eventListener);
            }
        }
        return sessionFactory;
    }

    @ConditionalOnMissingBean({Neo4jOperations.class})
    @Bean
    public Neo4jTemplate neo4jTemplate(SessionFactory sessionFactory) {
        return new Neo4jTemplate(sessionFactory);
    }

    @ConditionalOnMissingBean({PlatformTransactionManager.class})
    @Bean
    public Neo4jTransactionManager transactionManager(SessionFactory sessionFactory, Neo4jProperties properties, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        return customize(new Neo4jTransactionManager(sessionFactory), transactionManagerCustomizers.getIfAvailable());
    }

    private Neo4jTransactionManager customize(Neo4jTransactionManager transactionManager, TransactionManagerCustomizers customizers) {
        if (customizers != null) {
            customizers.customize(transactionManager);
        }
        return transactionManager;
    }

    private String[] getPackagesToScan(ApplicationContext applicationContext) {
        List<String> packages = EntityScanPackages.get(applicationContext).getPackageNames();
        if (packages.isEmpty() && AutoConfigurationPackages.has(applicationContext)) {
            packages = AutoConfigurationPackages.get(applicationContext);
        }
        return (String[]) packages.toArray(new String[packages.size()]);
    }

    @Configuration
    @ConditionalOnClass({WebMvcConfigurerAdapter.class, OpenSessionInViewInterceptor.class})
    @ConditionalOnMissingBean({OpenSessionInViewInterceptor.class})
    @ConditionalOnProperty(prefix = "spring.data.neo4j", name = {"open-in-view"}, havingValue = "true", matchIfMissing = true)
    @ConditionalOnWebApplication
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/neo4j/Neo4jDataAutoConfiguration$Neo4jWebConfiguration.class */
    protected static class Neo4jWebConfiguration {
        protected Neo4jWebConfiguration() {
        }

        @Configuration
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/data/neo4j/Neo4jDataAutoConfiguration$Neo4jWebConfiguration$Neo4jWebMvcConfiguration.class */
        protected static class Neo4jWebMvcConfiguration extends WebMvcConfigurerAdapter {
            protected Neo4jWebMvcConfiguration() {
            }

            @Bean
            public OpenSessionInViewInterceptor neo4jOpenSessionInViewInterceptor() {
                return new OpenSessionInViewInterceptor();
            }

            @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addWebRequestInterceptor(neo4jOpenSessionInViewInterceptor());
            }
        }
    }
}
