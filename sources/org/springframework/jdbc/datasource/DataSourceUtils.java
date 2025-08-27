package org.springframework.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/DataSourceUtils.class */
public abstract class DataSourceUtils {
    public static final int CONNECTION_SYNCHRONIZATION_ORDER = 1000;
    private static final Log logger = LogFactory.getLog(DataSourceUtils.class);

    public static Connection getConnection(DataSource dataSource) throws CannotGetJdbcConnectionException {
        try {
            return doGetConnection(dataSource);
        } catch (SQLException ex) {
            throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", ex);
        }
    }

    public static Connection doGetConnection(DataSource dataSource) throws SQLException {
        Assert.notNull(dataSource, "No DataSource specified");
        ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        if (conHolder != null && (conHolder.hasConnection() || conHolder.isSynchronizedWithTransaction())) {
            conHolder.requested();
            if (!conHolder.hasConnection()) {
                logger.debug("Fetching resumed JDBC Connection from DataSource");
                conHolder.setConnection(dataSource.getConnection());
            }
            return conHolder.getConnection();
        }
        logger.debug("Fetching JDBC Connection from DataSource");
        Connection con = dataSource.getConnection();
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            ConnectionHolder holderToUse = conHolder;
            try {
                if (holderToUse == null) {
                    holderToUse = new ConnectionHolder(con);
                } else {
                    holderToUse.setConnection(con);
                }
                holderToUse.requested();
                TransactionSynchronizationManager.registerSynchronization(new ConnectionSynchronization(holderToUse, dataSource));
                holderToUse.setSynchronizedWithTransaction(true);
                if (holderToUse != conHolder) {
                    TransactionSynchronizationManager.bindResource(dataSource, holderToUse);
                }
            } catch (RuntimeException ex) {
                releaseConnection(con, dataSource);
                throw ex;
            }
        }
        return con;
    }

    public static Integer prepareConnectionForTransaction(Connection con, TransactionDefinition definition) throws SQLException {
        Assert.notNull(con, "No Connection specified");
        if (definition != null && definition.isReadOnly()) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Setting JDBC Connection [" + con + "] read-only");
                }
                con.setReadOnly(true);
            } catch (RuntimeException ex) {
                Throwable cause = ex;
                while (true) {
                    Throwable exToCheck = cause;
                    if (exToCheck != null) {
                        if (exToCheck.getClass().getSimpleName().contains("Timeout")) {
                            throw ex;
                        }
                        cause = exToCheck.getCause();
                    } else {
                        logger.debug("Could not set JDBC Connection read-only", ex);
                        break;
                    }
                }
            } catch (SQLException ex2) {
                Throwable cause2 = ex2;
                while (true) {
                    Throwable exToCheck2 = cause2;
                    if (exToCheck2 != null) {
                        if (exToCheck2.getClass().getSimpleName().contains("Timeout")) {
                            throw ex2;
                        }
                        cause2 = exToCheck2.getCause();
                    } else {
                        logger.debug("Could not set JDBC Connection read-only", ex2);
                        break;
                    }
                }
            }
        }
        Integer previousIsolationLevel = null;
        if (definition != null && definition.getIsolationLevel() != -1) {
            if (logger.isDebugEnabled()) {
                logger.debug("Changing isolation level of JDBC Connection [" + con + "] to " + definition.getIsolationLevel());
            }
            int currentIsolation = con.getTransactionIsolation();
            if (currentIsolation != definition.getIsolationLevel()) {
                previousIsolationLevel = Integer.valueOf(currentIsolation);
                con.setTransactionIsolation(definition.getIsolationLevel());
            }
        }
        return previousIsolationLevel;
    }

    public static void resetConnectionAfterTransaction(Connection con, Integer previousIsolationLevel) {
        Assert.notNull(con, "No Connection specified");
        if (previousIsolationLevel != null) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Resetting isolation level of JDBC Connection [" + con + "] to " + previousIsolationLevel);
                }
                con.setTransactionIsolation(previousIsolationLevel.intValue());
            } catch (Throwable ex) {
                logger.debug("Could not reset JDBC Connection after transaction", ex);
                return;
            }
        }
        if (con.isReadOnly()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Resetting read-only flag of JDBC Connection [" + con + "]");
            }
            con.setReadOnly(false);
        }
    }

    public static boolean isConnectionTransactional(Connection con, DataSource dataSource) {
        ConnectionHolder conHolder;
        return (dataSource == null || (conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource)) == null || !connectionEquals(conHolder, con)) ? false : true;
    }

    public static void applyTransactionTimeout(Statement stmt, DataSource dataSource) throws SQLException {
        applyTimeout(stmt, dataSource, -1);
    }

    public static void applyTimeout(Statement stmt, DataSource dataSource, int timeout) throws SQLException {
        Assert.notNull(stmt, "No Statement specified");
        ConnectionHolder holder = null;
        if (dataSource != null) {
            holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        }
        if (holder != null && holder.hasTimeout()) {
            stmt.setQueryTimeout(holder.getTimeToLiveInSeconds());
        } else if (timeout >= 0) {
            stmt.setQueryTimeout(timeout);
        }
    }

    public static void releaseConnection(Connection con, DataSource dataSource) {
        try {
            doReleaseConnection(con, dataSource);
        } catch (SQLException ex) {
            logger.debug("Could not close JDBC Connection", ex);
        } catch (Throwable ex2) {
            logger.debug("Unexpected exception on closing JDBC Connection", ex2);
        }
    }

    public static void doReleaseConnection(Connection con, DataSource dataSource) throws SQLException {
        ConnectionHolder conHolder;
        if (con == null) {
            return;
        }
        if (dataSource != null && (conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource)) != null && connectionEquals(conHolder, con)) {
            conHolder.released();
        } else {
            doCloseConnection(con, dataSource);
        }
    }

    public static void doCloseConnection(Connection con, DataSource dataSource) throws SQLException {
        if (!(dataSource instanceof SmartDataSource) || ((SmartDataSource) dataSource).shouldClose(con)) {
            con.close();
        }
    }

    private static boolean connectionEquals(ConnectionHolder conHolder, Connection passedInCon) {
        if (!conHolder.hasConnection()) {
            return false;
        }
        Connection heldCon = conHolder.getConnection();
        return heldCon == passedInCon || heldCon.equals(passedInCon) || getTargetConnection(heldCon).equals(passedInCon);
    }

    public static Connection getTargetConnection(Connection con) {
        Connection targetConnection = con;
        while (true) {
            Connection conToUse = targetConnection;
            if (conToUse instanceof ConnectionProxy) {
                targetConnection = ((ConnectionProxy) conToUse).getTargetConnection();
            } else {
                return conToUse;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getConnectionSynchronizationOrder(DataSource dataSource) {
        int order = 1000;
        DataSource targetDataSource = dataSource;
        while (true) {
            DataSource currDs = targetDataSource;
            if (currDs instanceof DelegatingDataSource) {
                order--;
                targetDataSource = ((DelegatingDataSource) currDs).getTargetDataSource();
            } else {
                return order;
            }
        }
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/DataSourceUtils$ConnectionSynchronization.class */
    private static class ConnectionSynchronization extends TransactionSynchronizationAdapter {
        private final ConnectionHolder connectionHolder;
        private final DataSource dataSource;
        private int order;
        private boolean holderActive = true;

        public ConnectionSynchronization(ConnectionHolder connectionHolder, DataSource dataSource) {
            this.connectionHolder = connectionHolder;
            this.dataSource = dataSource;
            this.order = DataSourceUtils.getConnectionSynchronizationOrder(dataSource);
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.core.Ordered
        public int getOrder() {
            return this.order;
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void suspend() throws IllegalStateException {
            if (this.holderActive) {
                TransactionSynchronizationManager.unbindResource(this.dataSource);
                if (this.connectionHolder.hasConnection() && !this.connectionHolder.isOpen()) {
                    DataSourceUtils.releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
                    this.connectionHolder.setConnection(null);
                }
            }
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void resume() throws IllegalStateException {
            if (this.holderActive) {
                TransactionSynchronizationManager.bindResource(this.dataSource, this.connectionHolder);
            }
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void beforeCompletion() throws IllegalStateException {
            if (!this.connectionHolder.isOpen()) {
                TransactionSynchronizationManager.unbindResource(this.dataSource);
                this.holderActive = false;
                if (this.connectionHolder.hasConnection()) {
                    DataSourceUtils.releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
                }
            }
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void afterCompletion(int status) {
            if (this.holderActive) {
                TransactionSynchronizationManager.unbindResourceIfPossible(this.dataSource);
                this.holderActive = false;
                if (this.connectionHolder.hasConnection()) {
                    DataSourceUtils.releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
                    this.connectionHolder.setConnection(null);
                }
            }
            this.connectionHolder.reset();
        }
    }
}
