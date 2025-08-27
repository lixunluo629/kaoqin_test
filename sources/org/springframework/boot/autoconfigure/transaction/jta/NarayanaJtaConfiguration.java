package org.springframework.boot.autoconfigure.transaction.jta;

import com.arjuna.ats.arjuna.recovery.RecoveryManager;
import com.arjuna.ats.jbossatx.jta.RecoveryManagerService;
import com.arjuna.ats.jta.UserTransaction;
import java.io.File;
import javax.jms.Message;
import javax.transaction.TransactionManager;
import org.jboss.narayana.jta.jms.TransactionHelper;
import org.jboss.tm.XAResourceRecoveryRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationHome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.XAConnectionFactoryWrapper;
import org.springframework.boot.jta.XADataSourceWrapper;
import org.springframework.boot.jta.narayana.NarayanaBeanFactoryPostProcessor;
import org.springframework.boot.jta.narayana.NarayanaConfigurationBean;
import org.springframework.boot.jta.narayana.NarayanaProperties;
import org.springframework.boot.jta.narayana.NarayanaRecoveryManagerBean;
import org.springframework.boot.jta.narayana.NarayanaXAConnectionFactoryWrapper;
import org.springframework.boot.jta.narayana.NarayanaXADataSourceWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;

@EnableConfigurationProperties({JtaProperties.class})
@Configuration
@ConditionalOnClass({JtaTransactionManager.class, UserTransaction.class, XAResourceRecoveryRegistry.class})
@ConditionalOnMissingBean({PlatformTransactionManager.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/jta/NarayanaJtaConfiguration.class */
public class NarayanaJtaConfiguration {
    private final JtaProperties jtaProperties;
    private final TransactionManagerCustomizers transactionManagerCustomizers;

    public NarayanaJtaConfiguration(JtaProperties jtaProperties, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        this.jtaProperties = jtaProperties;
        this.transactionManagerCustomizers = transactionManagerCustomizers.getIfAvailable();
    }

    @ConditionalOnMissingBean
    @Bean
    public NarayanaProperties narayanaProperties() {
        return new NarayanaProperties();
    }

    @ConditionalOnMissingBean
    @Bean
    public NarayanaConfigurationBean narayanaConfiguration(NarayanaProperties properties) {
        properties.setLogDir(getLogDir().getAbsolutePath());
        if (this.jtaProperties.getTransactionManagerId() != null) {
            properties.setTransactionManagerId(this.jtaProperties.getTransactionManagerId());
        }
        return new NarayanaConfigurationBean(properties);
    }

    private File getLogDir() {
        if (StringUtils.hasLength(this.jtaProperties.getLogDir())) {
            return new File(this.jtaProperties.getLogDir());
        }
        File home = new ApplicationHome().getDir();
        return new File(home, "transaction-logs");
    }

    @DependsOn({"narayanaConfiguration"})
    @ConditionalOnMissingBean
    @Bean
    public javax.transaction.UserTransaction narayanaUserTransaction() {
        return UserTransaction.userTransaction();
    }

    @DependsOn({"narayanaConfiguration"})
    @ConditionalOnMissingBean
    @Bean
    public TransactionManager narayanaTransactionManager() {
        return com.arjuna.ats.jta.TransactionManager.transactionManager();
    }

    @DependsOn({"narayanaConfiguration"})
    @Bean
    public RecoveryManagerService narayanaRecoveryManagerService() {
        RecoveryManager.delayRecoveryManagerThread();
        return new RecoveryManagerService();
    }

    @ConditionalOnMissingBean
    @Bean
    public NarayanaRecoveryManagerBean narayanaRecoveryManager(RecoveryManagerService recoveryManagerService) {
        return new NarayanaRecoveryManagerBean(recoveryManagerService);
    }

    @Bean
    public JtaTransactionManager transactionManager(javax.transaction.UserTransaction userTransaction, TransactionManager transactionManager) {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager(userTransaction, transactionManager);
        if (this.transactionManagerCustomizers != null) {
            this.transactionManagerCustomizers.customize(jtaTransactionManager);
        }
        return jtaTransactionManager;
    }

    @ConditionalOnMissingBean({XADataSourceWrapper.class})
    @Bean
    public XADataSourceWrapper xaDataSourceWrapper(NarayanaRecoveryManagerBean narayanaRecoveryManagerBean, NarayanaProperties narayanaProperties) {
        return new NarayanaXADataSourceWrapper(narayanaRecoveryManagerBean, narayanaProperties);
    }

    @ConditionalOnMissingBean
    @Bean
    public static NarayanaBeanFactoryPostProcessor narayanaBeanFactoryPostProcessor() {
        return new NarayanaBeanFactoryPostProcessor();
    }

    @Configuration
    @ConditionalOnClass({Message.class, TransactionHelper.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/jta/NarayanaJtaConfiguration$NarayanaJtaJmsConfiguration.class */
    static class NarayanaJtaJmsConfiguration {
        NarayanaJtaJmsConfiguration() {
        }

        @ConditionalOnMissingBean({XAConnectionFactoryWrapper.class})
        @Bean
        public NarayanaXAConnectionFactoryWrapper xaConnectionFactoryWrapper(TransactionManager transactionManager, NarayanaRecoveryManagerBean narayanaRecoveryManagerBean, NarayanaProperties narayanaProperties) {
            return new NarayanaXAConnectionFactoryWrapper(transactionManager, narayanaRecoveryManagerBean, narayanaProperties);
        }
    }
}
