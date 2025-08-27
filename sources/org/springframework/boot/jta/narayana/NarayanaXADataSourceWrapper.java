package org.springframework.boot.jta.narayana;

import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import org.springframework.boot.jta.XADataSourceWrapper;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/narayana/NarayanaXADataSourceWrapper.class */
public class NarayanaXADataSourceWrapper implements XADataSourceWrapper {
    private final NarayanaRecoveryManagerBean recoveryManager;
    private final NarayanaProperties properties;

    public NarayanaXADataSourceWrapper(NarayanaRecoveryManagerBean recoveryManager, NarayanaProperties properties) {
        Assert.notNull(recoveryManager, "RecoveryManager must not be null");
        Assert.notNull(properties, "Properties must not be null");
        this.recoveryManager = recoveryManager;
        this.properties = properties;
    }

    @Override // org.springframework.boot.jta.XADataSourceWrapper
    /* renamed from: wrapDataSource */
    public DataSource mo7464wrapDataSource(XADataSource dataSource) {
        XAResourceRecoveryHelper recoveryHelper = getRecoveryHelper(dataSource);
        this.recoveryManager.registerXAResourceRecoveryHelper(recoveryHelper);
        return new NarayanaDataSourceBean(dataSource);
    }

    private XAResourceRecoveryHelper getRecoveryHelper(XADataSource dataSource) {
        if (this.properties.getRecoveryDbUser() == null && this.properties.getRecoveryDbPass() == null) {
            return new DataSourceXAResourceRecoveryHelper(dataSource);
        }
        return new DataSourceXAResourceRecoveryHelper(dataSource, this.properties.getRecoveryDbUser(), this.properties.getRecoveryDbPass());
    }
}
