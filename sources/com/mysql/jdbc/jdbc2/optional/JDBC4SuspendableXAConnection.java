package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.StatementEvent;
import javax.sql.StatementEventListener;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jdbc2/optional/JDBC4SuspendableXAConnection.class */
public class JDBC4SuspendableXAConnection extends SuspendableXAConnection {
    private final Map<StatementEventListener, StatementEventListener> statementEventListeners;

    public JDBC4SuspendableXAConnection(Connection connection) throws SQLException {
        super(connection);
        this.statementEventListeners = new HashMap();
    }

    @Override // com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection, com.mysql.jdbc.jdbc2.optional.MysqlPooledConnection, javax.sql.PooledConnection
    public synchronized void close() throws SQLException {
        super.close();
        this.statementEventListeners.clear();
    }

    @Override // javax.sql.PooledConnection
    public void addStatementEventListener(StatementEventListener listener) {
        synchronized (this.statementEventListeners) {
            this.statementEventListeners.put(listener, listener);
        }
    }

    @Override // javax.sql.PooledConnection
    public void removeStatementEventListener(StatementEventListener listener) {
        synchronized (this.statementEventListeners) {
            this.statementEventListeners.remove(listener);
        }
    }

    void fireStatementEvent(StatementEvent event) throws SQLException {
        synchronized (this.statementEventListeners) {
            for (StatementEventListener listener : this.statementEventListeners.keySet()) {
                listener.statementClosed(event);
            }
        }
    }
}
