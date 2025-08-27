package org.apache.ibatis.transaction.managed;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/transaction/managed/ManagedTransaction.class */
public class ManagedTransaction implements Transaction {
    private static final Log log = LogFactory.getLog((Class<?>) ManagedTransaction.class);
    private DataSource dataSource;
    private TransactionIsolationLevel level;
    private Connection connection;
    private final boolean closeConnection;

    public ManagedTransaction(Connection connection, boolean closeConnection) {
        this.connection = connection;
        this.closeConnection = closeConnection;
    }

    public ManagedTransaction(DataSource ds, TransactionIsolationLevel level, boolean closeConnection) {
        this.dataSource = ds;
        this.level = level;
        this.closeConnection = closeConnection;
    }

    @Override // org.apache.ibatis.transaction.Transaction
    public Connection getConnection() throws SQLException {
        if (this.connection == null) {
            openConnection();
        }
        return this.connection;
    }

    @Override // org.apache.ibatis.transaction.Transaction
    public void commit() throws SQLException {
    }

    @Override // org.apache.ibatis.transaction.Transaction
    public void rollback() throws SQLException {
    }

    @Override // org.apache.ibatis.transaction.Transaction
    public void close() throws SQLException {
        if (this.closeConnection && this.connection != null) {
            if (log.isDebugEnabled()) {
                log.debug("Closing JDBC Connection [" + this.connection + "]");
            }
            this.connection.close();
        }
    }

    protected void openConnection() throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("Opening JDBC Connection");
        }
        this.connection = this.dataSource.getConnection();
        if (this.level != null) {
            this.connection.setTransactionIsolation(this.level.getLevel());
        }
    }

    @Override // org.apache.ibatis.transaction.Transaction
    public Integer getTimeout() throws SQLException {
        return null;
    }
}
