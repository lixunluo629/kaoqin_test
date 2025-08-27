package org.springframework.boot.jta.narayana;

import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;
import java.sql.SQLException;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/narayana/DataSourceXAResourceRecoveryHelper.class */
public class DataSourceXAResourceRecoveryHelper implements XAResourceRecoveryHelper, XAResource {
    private static final XAResource[] NO_XA_RESOURCES = new XAResource[0];
    private static final Log logger = LogFactory.getLog(DataSourceXAResourceRecoveryHelper.class);
    private final XADataSource xaDataSource;
    private final String user;
    private final String password;
    private XAConnection xaConnection;
    private XAResource delegate;

    public DataSourceXAResourceRecoveryHelper(XADataSource xaDataSource) {
        this(xaDataSource, null, null);
    }

    public DataSourceXAResourceRecoveryHelper(XADataSource xaDataSource, String user, String password) {
        Assert.notNull(xaDataSource, "XADataSource must not be null");
        this.xaDataSource = xaDataSource;
        this.user = user;
        this.password = password;
    }

    public boolean initialise(String properties) {
        return true;
    }

    public XAResource[] getXAResources() {
        if (connect()) {
            return new XAResource[]{this};
        }
        return NO_XA_RESOURCES;
    }

    private boolean connect() {
        if (this.delegate == null) {
            try {
                this.xaConnection = getXaConnection();
                this.delegate = this.xaConnection.getXAResource();
                return true;
            } catch (SQLException ex) {
                logger.warn("Failed to create connection", ex);
                return false;
            }
        }
        return true;
    }

    private XAConnection getXaConnection() throws SQLException {
        if (this.user == null && this.password == null) {
            return this.xaDataSource.getXAConnection();
        }
        return this.xaDataSource.getXAConnection(this.user, this.password);
    }

    public Xid[] recover(int flag) throws XAException {
        try {
            return getDelegate(true).recover(flag);
        } finally {
            if (flag == 8388608) {
                disconnect();
            }
        }
    }

    private void disconnect() throws XAException {
        try {
            this.xaConnection.close();
        } catch (SQLException ex) {
            logger.warn("Failed to close connection", ex);
        } finally {
            this.xaConnection = null;
            this.delegate = null;
        }
    }

    public void start(Xid xid, int flags) throws XAException {
        getDelegate(true).start(xid, flags);
    }

    public void end(Xid xid, int flags) throws XAException {
        getDelegate(true).end(xid, flags);
    }

    public int prepare(Xid xid) throws XAException {
        return getDelegate(true).prepare(xid);
    }

    public void commit(Xid xid, boolean onePhase) throws XAException {
        getDelegate(true).commit(xid, onePhase);
    }

    public void rollback(Xid xid) throws XAException {
        getDelegate(true).rollback(xid);
    }

    public boolean isSameRM(XAResource xaResource) throws XAException {
        return getDelegate(true).isSameRM(xaResource);
    }

    public void forget(Xid xid) throws XAException {
        getDelegate(true).forget(xid);
    }

    public int getTransactionTimeout() throws XAException {
        return getDelegate(true).getTransactionTimeout();
    }

    public boolean setTransactionTimeout(int seconds) throws XAException {
        return getDelegate(true).setTransactionTimeout(seconds);
    }

    private XAResource getDelegate(boolean required) {
        Assert.state((this.delegate == null && required) ? false : true, "Connection has not been opened");
        return this.delegate;
    }
}
