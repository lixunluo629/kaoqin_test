package org.springframework.boot.jta.narayana;

import com.arjuna.ats.internal.jta.recovery.arjunacore.XARecoveryModule;
import com.arjuna.ats.jbossatx.jta.RecoveryManagerService;
import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/narayana/NarayanaRecoveryManagerBean.class */
public class NarayanaRecoveryManagerBean implements InitializingBean, DisposableBean {
    private final RecoveryManagerService recoveryManagerService;

    public NarayanaRecoveryManagerBean(RecoveryManagerService recoveryManagerService) {
        Assert.notNull(recoveryManagerService, "RecoveryManagerService must not be null");
        this.recoveryManagerService = recoveryManagerService;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        this.recoveryManagerService.create();
        this.recoveryManagerService.start();
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws Exception {
        this.recoveryManagerService.stop();
        this.recoveryManagerService.destroy();
    }

    void registerXAResourceRecoveryHelper(XAResourceRecoveryHelper xaResourceRecoveryHelper) {
        getXARecoveryModule().addXAResourceRecoveryHelper(xaResourceRecoveryHelper);
    }

    private XARecoveryModule getXARecoveryModule() {
        XARecoveryModule xaRecoveryModule = XARecoveryModule.getRegisteredXARecoveryModule();
        if (xaRecoveryModule != null) {
            return xaRecoveryModule;
        }
        throw new IllegalStateException("XARecoveryModule is not registered with recovery manager");
    }
}
