package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ReplicationMySQLConnection.class */
public class ReplicationMySQLConnection extends MultiHostMySQLConnection implements ReplicationConnection {
    public ReplicationMySQLConnection(MultiHostConnectionProxy proxy) {
        super(proxy);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mysql.jdbc.MultiHostMySQLConnection
    public ReplicationConnectionProxy getThisAsProxy() {
        return (ReplicationConnectionProxy) super.getThisAsProxy();
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.MySQLConnection
    public MySQLConnection getActiveMySQLConnection() {
        return (MySQLConnection) getCurrentConnection();
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public synchronized Connection getCurrentConnection() {
        return getThisAsProxy().getCurrentConnection();
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public long getConnectionGroupId() {
        return getThisAsProxy().getConnectionGroupId();
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public synchronized Connection getMasterConnection() {
        return getThisAsProxy().getMasterConnection();
    }

    private Connection getValidatedMasterConnection() {
        Connection conn = getThisAsProxy().masterConnection;
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    return conn;
                }
            } catch (SQLException e) {
                return null;
            }
        }
        return null;
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public void promoteSlaveToMaster(String host) throws SQLException {
        getThisAsProxy().promoteSlaveToMaster(host);
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public void removeMasterHost(String host) throws SQLException {
        getThisAsProxy().removeMasterHost(host);
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public void removeMasterHost(String host, boolean waitUntilNotInUse) throws SQLException {
        getThisAsProxy().removeMasterHost(host, waitUntilNotInUse);
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public boolean isHostMaster(String host) {
        return getThisAsProxy().isHostMaster(host);
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public synchronized Connection getSlavesConnection() {
        return getThisAsProxy().getSlavesConnection();
    }

    private Connection getValidatedSlavesConnection() {
        Connection conn = getThisAsProxy().slavesConnection;
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    return conn;
                }
            } catch (SQLException e) {
                return null;
            }
        }
        return null;
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public void addSlaveHost(String host) throws SQLException {
        getThisAsProxy().addSlaveHost(host);
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public void removeSlave(String host) throws SQLException {
        getThisAsProxy().removeSlave(host);
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public void removeSlave(String host, boolean closeGently) throws SQLException {
        getThisAsProxy().removeSlave(host, closeGently);
    }

    @Override // com.mysql.jdbc.ReplicationConnection
    public boolean isHostSlave(String host) {
        return getThisAsProxy().isHostSlave(host);
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, java.sql.Connection
    public void setReadOnly(boolean readOnlyFlag) throws SQLException {
        getThisAsProxy().setReadOnly(readOnlyFlag);
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.MySQLConnection, java.sql.Connection
    public boolean isReadOnly() throws SQLException {
        return getThisAsProxy().isReadOnly();
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.Connection
    public synchronized void ping() throws SQLException {
        try {
            Connection conn = getValidatedMasterConnection();
            if (conn != null) {
                conn.ping();
            }
        } catch (SQLException e) {
            if (isMasterConnection()) {
                throw e;
            }
        }
        try {
            Connection conn2 = getValidatedSlavesConnection();
            if (conn2 != null) {
                conn2.ping();
            }
        } catch (SQLException e2) {
            if (!isMasterConnection()) {
                throw e2;
            }
        }
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.Connection
    public synchronized void changeUser(String userName, String newPassword) throws SQLException {
        Connection conn = getValidatedMasterConnection();
        if (conn != null) {
            conn.changeUser(userName, newPassword);
        }
        Connection conn2 = getValidatedSlavesConnection();
        if (conn2 != null) {
            conn2.changeUser(userName, newPassword);
        }
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.Connection
    public synchronized void setStatementComment(String comment) {
        Connection conn = getValidatedMasterConnection();
        if (conn != null) {
            conn.setStatementComment(comment);
        }
        Connection conn2 = getValidatedSlavesConnection();
        if (conn2 != null) {
            conn2.setStatementComment(comment);
        }
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.Connection
    public boolean hasSameProperties(Connection c) {
        Connection connM = getValidatedMasterConnection();
        Connection connS = getValidatedSlavesConnection();
        if (connM == null && connS == null) {
            return false;
        }
        return (connM == null || connM.hasSameProperties(c)) && (connS == null || connS.hasSameProperties(c));
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public Properties getProperties() {
        Properties props = new Properties();
        Connection conn = getValidatedMasterConnection();
        if (conn != null) {
            props.putAll(conn.getProperties());
        }
        Connection conn2 = getValidatedSlavesConnection();
        if (conn2 != null) {
            props.putAll(conn2.getProperties());
        }
        return props;
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.Connection
    public void abort(Executor executor) throws SQLException {
        getThisAsProxy().doAbort(executor);
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.Connection
    public void abortInternal() throws SQLException {
        getThisAsProxy().doAbortInternal();
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.ConnectionProperties
    public boolean getAllowMasterDownConnections() {
        return getThisAsProxy().allowMasterDownConnections;
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.ConnectionProperties
    public void setAllowMasterDownConnections(boolean connectIfMasterDown) {
        getThisAsProxy().allowMasterDownConnections = connectIfMasterDown;
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.ConnectionProperties
    public boolean getReplicationEnableJMX() {
        return getThisAsProxy().enableJMX;
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.ConnectionProperties
    public void setReplicationEnableJMX(boolean replicationEnableJMX) {
        getThisAsProxy().enableJMX = replicationEnableJMX;
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public void setProxy(MySQLConnection proxy) {
        getThisAsProxy().setProxy(proxy);
    }
}
