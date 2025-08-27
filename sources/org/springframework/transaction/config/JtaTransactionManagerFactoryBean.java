package org.springframework.transaction.config;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.ClassUtils;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/transaction/config/JtaTransactionManagerFactoryBean.class */
public class JtaTransactionManagerFactoryBean implements FactoryBean<JtaTransactionManager> {
    private static final String WEBLOGIC_JTA_TRANSACTION_MANAGER_CLASS_NAME = "org.springframework.transaction.jta.WebLogicJtaTransactionManager";
    private static final String WEBSPHERE_TRANSACTION_MANAGER_CLASS_NAME = "org.springframework.transaction.jta.WebSphereUowTransactionManager";
    private static final String JTA_TRANSACTION_MANAGER_CLASS_NAME = "org.springframework.transaction.jta.JtaTransactionManager";
    private static final boolean weblogicPresent = ClassUtils.isPresent("weblogic.transaction.UserTransaction", JtaTransactionManagerFactoryBean.class.getClassLoader());
    private static final boolean webspherePresent = ClassUtils.isPresent("com.ibm.wsspi.uow.UOWManager", JtaTransactionManagerFactoryBean.class.getClassLoader());
    private final JtaTransactionManager transactionManager;

    public JtaTransactionManagerFactoryBean() {
        String className = resolveJtaTransactionManagerClassName();
        try {
            this.transactionManager = (JtaTransactionManager) BeanUtils.instantiate(ClassUtils.forName(className, JtaTransactionManagerFactoryBean.class.getClassLoader()));
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to load JtaTransactionManager class: " + className, ex);
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public JtaTransactionManager getObject() {
        return this.transactionManager;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return this.transactionManager != null ? this.transactionManager.getClass() : JtaTransactionManager.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }

    static String resolveJtaTransactionManagerClassName() {
        if (weblogicPresent) {
            return WEBLOGIC_JTA_TRANSACTION_MANAGER_CLASS_NAME;
        }
        if (webspherePresent) {
            return WEBSPHERE_TRANSACTION_MANAGER_CLASS_NAME;
        }
        return JTA_TRANSACTION_MANAGER_CLASS_NAME;
    }
}
