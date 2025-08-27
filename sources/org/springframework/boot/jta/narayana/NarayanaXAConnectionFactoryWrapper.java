package org.springframework.boot.jta.narayana;

import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;
import javax.jms.ConnectionFactory;
import javax.jms.XAConnectionFactory;
import javax.transaction.TransactionManager;
import org.jboss.narayana.jta.jms.ConnectionFactoryProxy;
import org.jboss.narayana.jta.jms.JmsXAResourceRecoveryHelper;
import org.jboss.narayana.jta.jms.TransactionHelperImpl;
import org.springframework.boot.jta.XAConnectionFactoryWrapper;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/narayana/NarayanaXAConnectionFactoryWrapper.class */
public class NarayanaXAConnectionFactoryWrapper implements XAConnectionFactoryWrapper {
    private final TransactionManager transactionManager;
    private final NarayanaRecoveryManagerBean recoveryManager;
    private final NarayanaProperties properties;

    public NarayanaXAConnectionFactoryWrapper(TransactionManager transactionManager, NarayanaRecoveryManagerBean recoveryManager, NarayanaProperties properties) {
        Assert.notNull(transactionManager, "TransactionManager must not be null");
        Assert.notNull(recoveryManager, "RecoveryManager must not be null");
        Assert.notNull(properties, "Properties must not be null");
        this.transactionManager = transactionManager;
        this.recoveryManager = recoveryManager;
        this.properties = properties;
    }

    @Override // org.springframework.boot.jta.XAConnectionFactoryWrapper
    public ConnectionFactory wrapConnectionFactory(XAConnectionFactory xaConnectionFactory) {
        XAResourceRecoveryHelper recoveryHelper = getRecoveryHelper(xaConnectionFactory);
        this.recoveryManager.registerXAResourceRecoveryHelper(recoveryHelper);
        return new ConnectionFactoryProxy(xaConnectionFactory, new TransactionHelperImpl(this.transactionManager));
    }

    private XAResourceRecoveryHelper getRecoveryHelper(XAConnectionFactory xaConnectionFactory) {
        if (this.properties.getRecoveryJmsUser() == null && this.properties.getRecoveryJmsPass() == null) {
            return new JmsXAResourceRecoveryHelper(xaConnectionFactory);
        }
        return new JmsXAResourceRecoveryHelper(xaConnectionFactory, this.properties.getRecoveryJmsUser(), this.properties.getRecoveryJmsPass());
    }
}
