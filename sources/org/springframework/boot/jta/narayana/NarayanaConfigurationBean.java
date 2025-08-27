package org.springframework.boot.jta.narayana;

import com.arjuna.ats.arjuna.common.CoordinatorEnvironmentBean;
import com.arjuna.ats.arjuna.common.CoreEnvironmentBean;
import com.arjuna.ats.arjuna.common.CoreEnvironmentBeanException;
import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.arjuna.common.RecoveryEnvironmentBean;
import com.arjuna.ats.jta.common.JTAEnvironmentBean;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/narayana/NarayanaConfigurationBean.class */
public class NarayanaConfigurationBean implements InitializingBean {
    private static final String JBOSSTS_PROPERTIES_FILE_NAME = "jbossts-properties.xml";
    private final NarayanaProperties properties;

    public NarayanaConfigurationBean(NarayanaProperties narayanaProperties) {
        this.properties = narayanaProperties;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        if (isPropertiesFileAvailable()) {
            return;
        }
        setNodeIdentifier(this.properties.getTransactionManagerId());
        setObjectStoreDir(this.properties.getLogDir());
        setCommitOnePhase(this.properties.isOnePhaseCommit());
        setDefaultTimeout(this.properties.getDefaultTimeout());
        setPeriodicRecoveryPeriod(this.properties.getPeriodicRecoveryPeriod());
        setRecoveryBackoffPeriod(this.properties.getRecoveryBackoffPeriod());
        setXaResourceOrphanFilters(this.properties.getXaResourceOrphanFilters());
        setRecoveryModules(this.properties.getRecoveryModules());
        setExpiryScanners(this.properties.getExpiryScanners());
    }

    private boolean isPropertiesFileAvailable() {
        return Thread.currentThread().getContextClassLoader().getResource(JBOSSTS_PROPERTIES_FILE_NAME) != null;
    }

    private void setNodeIdentifier(String nodeIdentifier) throws CoreEnvironmentBeanException {
        ((CoreEnvironmentBean) getPopulator(CoreEnvironmentBean.class)).setNodeIdentifier(nodeIdentifier);
    }

    private void setObjectStoreDir(String objectStoreDir) {
        if (objectStoreDir != null) {
            ((ObjectStoreEnvironmentBean) getPopulator(ObjectStoreEnvironmentBean.class)).setObjectStoreDir(objectStoreDir);
            ((ObjectStoreEnvironmentBean) getPopulator(ObjectStoreEnvironmentBean.class, "communicationStore")).setObjectStoreDir(objectStoreDir);
            ((ObjectStoreEnvironmentBean) getPopulator(ObjectStoreEnvironmentBean.class, "stateStore")).setObjectStoreDir(objectStoreDir);
        }
    }

    private void setCommitOnePhase(boolean isCommitOnePhase) {
        ((CoordinatorEnvironmentBean) getPopulator(CoordinatorEnvironmentBean.class)).setCommitOnePhase(isCommitOnePhase);
    }

    private void setDefaultTimeout(int defaultTimeout) {
        ((CoordinatorEnvironmentBean) getPopulator(CoordinatorEnvironmentBean.class)).setDefaultTimeout(defaultTimeout);
    }

    private void setPeriodicRecoveryPeriod(int periodicRecoveryPeriod) {
        ((RecoveryEnvironmentBean) getPopulator(RecoveryEnvironmentBean.class)).setPeriodicRecoveryPeriod(periodicRecoveryPeriod);
    }

    private void setRecoveryBackoffPeriod(int recoveryBackoffPeriod) {
        ((RecoveryEnvironmentBean) getPopulator(RecoveryEnvironmentBean.class)).setRecoveryBackoffPeriod(recoveryBackoffPeriod);
    }

    private void setXaResourceOrphanFilters(List<String> xaResourceOrphanFilters) {
        ((JTAEnvironmentBean) getPopulator(JTAEnvironmentBean.class)).setXaResourceOrphanFilterClassNames(xaResourceOrphanFilters);
    }

    private void setRecoveryModules(List<String> recoveryModules) {
        ((RecoveryEnvironmentBean) getPopulator(RecoveryEnvironmentBean.class)).setRecoveryModuleClassNames(recoveryModules);
    }

    private void setExpiryScanners(List<String> expiryScanners) {
        ((RecoveryEnvironmentBean) getPopulator(RecoveryEnvironmentBean.class)).setExpiryScannerClassNames(expiryScanners);
    }

    private <T> T getPopulator(Class<T> cls) {
        return (T) BeanPopulator.getDefaultInstance(cls);
    }

    private <T> T getPopulator(Class<T> cls, String str) {
        return (T) BeanPopulator.getNamedInstance(cls, str);
    }
}
