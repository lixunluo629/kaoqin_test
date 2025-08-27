package org.springframework.boot.jta.narayana;

import javax.transaction.TransactionManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/narayana/NarayanaBeanFactoryPostProcessor.class */
public class NarayanaBeanFactoryPostProcessor implements BeanFactoryPostProcessor, Ordered {
    private static final String[] NO_BEANS = new String[0];
    private static final int ORDER = Integer.MAX_VALUE;

    @Override // org.springframework.beans.factory.config.BeanFactoryPostProcessor
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] transactionManagers = beanFactory.getBeanNamesForType(TransactionManager.class, true, false);
        String[] recoveryManagers = beanFactory.getBeanNamesForType(NarayanaRecoveryManagerBean.class, true, false);
        addBeanDependencies(beanFactory, transactionManagers, "javax.sql.DataSource");
        addBeanDependencies(beanFactory, recoveryManagers, "javax.sql.DataSource");
        addBeanDependencies(beanFactory, transactionManagers, "javax.jms.ConnectionFactory");
        addBeanDependencies(beanFactory, recoveryManagers, "javax.jms.ConnectionFactory");
    }

    private void addBeanDependencies(ConfigurableListableBeanFactory beanFactory, String[] beanNames, String dependencyType) {
        for (String beanName : beanNames) {
            addBeanDependencies(beanFactory, beanName, dependencyType);
        }
    }

    private void addBeanDependencies(ConfigurableListableBeanFactory beanFactory, String beanName, String dependencyType) {
        for (String dependentBeanName : getBeanNamesForType(beanFactory, dependencyType)) {
            beanFactory.registerDependentBean(beanName, dependentBeanName);
        }
    }

    private String[] getBeanNamesForType(ConfigurableListableBeanFactory beanFactory, String type) {
        try {
            return beanFactory.getBeanNamesForType(Class.forName(type), true, false);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return NO_BEANS;
        }
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
