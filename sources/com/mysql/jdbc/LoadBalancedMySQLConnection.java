package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/LoadBalancedMySQLConnection.class */
public class LoadBalancedMySQLConnection extends MultiHostMySQLConnection implements LoadBalancedConnection {
    public LoadBalancedMySQLConnection(LoadBalancedConnectionProxy proxy) {
        super(proxy);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mysql.jdbc.MultiHostMySQLConnection
    public LoadBalancedConnectionProxy getThisAsProxy() {
        return (LoadBalancedConnectionProxy) super.getThisAsProxy();
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, java.sql.Connection, java.lang.AutoCloseable
    public void close() throws SQLException {
        getThisAsProxy().doClose();
    }

    @Override // com.mysql.jdbc.MultiHostMySQLConnection, com.mysql.jdbc.Connection
    public void ping() throws SQLException {
        ping(true);
    }

    @Override // com.mysql.jdbc.LoadBalancedConnection
    public void ping(boolean allConnections) throws SQLException {
        if (allConnections) {
            getThisAsProxy().doPing();
        } else {
            getActiveMySQLConnection().ping();
        }
    }

    @Override // com.mysql.jdbc.LoadBalancedConnection
    public boolean addHost(String host) throws SQLException {
        return getThisAsProxy().addHost(host);
    }

    @Override // com.mysql.jdbc.LoadBalancedConnection
    public void removeHost(String host) throws SQLException {
        getThisAsProxy().removeHost(host);
    }

    @Override // com.mysql.jdbc.LoadBalancedConnection
    public void removeHostWhenNotInUse(String host) throws SQLException {
        getThisAsProxy().removeHostWhenNotInUse(host);
    }
}
