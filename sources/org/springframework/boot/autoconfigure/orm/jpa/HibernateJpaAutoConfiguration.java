package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.ClassUtils;

@Configuration
@ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class, EntityManager.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@Conditional({HibernateEntityManagerCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaAutoConfiguration.class */
public class HibernateJpaAutoConfiguration extends JpaBaseConfiguration {
    private static final String JTA_PLATFORM = "hibernate.transaction.jta.platform";
    private static final Log logger = LogFactory.getLog(HibernateJpaAutoConfiguration.class);
    private static final String[] NO_JTA_PLATFORM_CLASSES = {"org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform", "org.hibernate.service.jta.platform.internal.NoJtaPlatform"};
    private static final String[] WEBSPHERE_JTA_PLATFORM_CLASSES = {"org.hibernate.engine.transaction.jta.platform.internal.WebSphereExtendedJtaPlatform", "org.hibernate.service.jta.platform.internal.WebSphereExtendedJtaPlatform"};

    public HibernateJpaAutoConfiguration(DataSource dataSource, JpaProperties jpaProperties, ObjectProvider<JtaTransactionManager> jtaTransactionManager, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        super(dataSource, jpaProperties, jtaTransactionManager, transactionManagerCustomizers);
    }

    @Override // org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Override // org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration
    protected Map<String, Object> getVendorProperties() {
        Map<String, Object> vendorProperties = new LinkedHashMap<>();
        vendorProperties.putAll(getProperties().getHibernateProperties(getDataSource()));
        return vendorProperties;
    }

    @Override // org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration
    protected void customizeVendorProperties(Map<String, Object> vendorProperties) throws LinkageError {
        super.customizeVendorProperties(vendorProperties);
        if (!vendorProperties.containsKey(JTA_PLATFORM)) {
            configureJtaPlatform(vendorProperties);
        }
    }

    private void configureJtaPlatform(Map<String, Object> vendorProperties) throws LinkageError {
        JtaTransactionManager jtaTransactionManager = getJtaTransactionManager();
        if (jtaTransactionManager != null) {
            if (runningOnWebSphere()) {
                configureWebSphereTransactionPlatform(vendorProperties);
                return;
            } else {
                configureSpringJtaPlatform(vendorProperties, jtaTransactionManager);
                return;
            }
        }
        vendorProperties.put(JTA_PLATFORM, getNoJtaPlatformManager());
    }

    private boolean runningOnWebSphere() {
        return ClassUtils.isPresent("com.ibm.websphere.jtaextensions.ExtendedJTATransaction", getClass().getClassLoader());
    }

    private void configureWebSphereTransactionPlatform(Map<String, Object> vendorProperties) {
        vendorProperties.put(JTA_PLATFORM, getWebSphereJtaPlatformManager());
    }

    private Object getWebSphereJtaPlatformManager() {
        return getJtaPlatformManager(WEBSPHERE_JTA_PLATFORM_CLASSES);
    }

    private void configureSpringJtaPlatform(Map<String, Object> vendorProperties, JtaTransactionManager jtaTransactionManager) {
        try {
            vendorProperties.put(JTA_PLATFORM, new SpringJtaPlatform(jtaTransactionManager));
        } catch (LinkageError ex) {
            if (!isUsingJndi()) {
                throw new IllegalStateException("Unable to set Hibernate JTA platform, are you using the correct version of Hibernate?", ex);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to set Hibernate JTA platform : " + ex.getMessage());
            }
        }
    }

    private boolean isUsingJndi() {
        try {
            return JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable();
        } catch (Error e) {
            return false;
        }
    }

    private Object getNoJtaPlatformManager() {
        return getJtaPlatformManager(NO_JTA_PLATFORM_CLASSES);
    }

    private Object getJtaPlatformManager(String[] candidates) {
        for (String candidate : candidates) {
            try {
                return Class.forName(candidate).newInstance();
            } catch (Exception e) {
            }
        }
        throw new IllegalStateException("Could not configure JTA platform");
    }

    @Order(-2147483628)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaAutoConfiguration$HibernateEntityManagerCondition.class */
    static class HibernateEntityManagerCondition extends SpringBootCondition {
        private static String[] CLASS_NAMES = {"org.hibernate.ejb.HibernateEntityManager", "org.hibernate.jpa.HibernateEntityManager"};

        HibernateEntityManagerCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConditionMessage.Builder message = ConditionMessage.forCondition("HibernateEntityManager", new Object[0]);
            for (String className : CLASS_NAMES) {
                if (ClassUtils.isPresent(className, context.getClassLoader())) {
                    return ConditionOutcome.match(message.found("class").items(ConditionMessage.Style.QUOTE, className));
                }
            }
            return ConditionOutcome.noMatch(message.didNotFind("class", "classes").items(ConditionMessage.Style.QUOTE, Arrays.asList(CLASS_NAMES)));
        }
    }
}
