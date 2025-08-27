package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/LoadBalancedAutoCommitInterceptor.class */
public class LoadBalancedAutoCommitInterceptor implements StatementInterceptorV2 {
    private String matchingAfterStatementRegex;
    private ConnectionImpl conn;
    private int matchingAfterStatementCount = 0;
    private int matchingAfterStatementThreshold = 0;
    private LoadBalancedConnectionProxy proxy = null;
    private boolean countStatements = false;

    @Override // com.mysql.jdbc.StatementInterceptorV2, com.mysql.jdbc.Extension
    public void destroy() {
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2
    public boolean executeTopLevelOnly() {
        return false;
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2, com.mysql.jdbc.Extension
    public void init(Connection connection, Properties props) throws SQLException {
        this.conn = (ConnectionImpl) connection;
        String autoCommitSwapThresholdAsString = props.getProperty("loadBalanceAutoCommitStatementThreshold", "0");
        try {
            this.matchingAfterStatementThreshold = Integer.parseInt(autoCommitSwapThresholdAsString);
        } catch (NumberFormatException e) {
        }
        String autoCommitSwapRegex = props.getProperty("loadBalanceAutoCommitStatementRegex", "");
        if ("".equals(autoCommitSwapRegex)) {
            return;
        }
        this.matchingAfterStatementRegex = autoCommitSwapRegex;
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2
    public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection, int warningCount, boolean noIndexUsed, boolean noGoodIndexUsed, SQLException statementException) throws SQLException {
        MySQLConnection lcl_proxy;
        if (!this.countStatements || StringUtils.startsWithIgnoreCase(sql, "SET") || StringUtils.startsWithIgnoreCase(sql, "SHOW")) {
            return originalResultSet;
        }
        if (!this.conn.getAutoCommit()) {
            this.matchingAfterStatementCount = 0;
            return originalResultSet;
        }
        if (this.proxy == null && this.conn.isProxySet()) {
            MySQLConnection multiHostSafeProxy = this.conn.getMultiHostSafeProxy();
            while (true) {
                lcl_proxy = multiHostSafeProxy;
                if (lcl_proxy == null || (lcl_proxy instanceof LoadBalancedMySQLConnection)) {
                    break;
                }
                multiHostSafeProxy = lcl_proxy.getMultiHostSafeProxy();
            }
            if (lcl_proxy != null) {
                this.proxy = ((LoadBalancedMySQLConnection) lcl_proxy).getThisAsProxy();
            }
        }
        if (this.proxy == null) {
            return originalResultSet;
        }
        if (this.matchingAfterStatementRegex == null || sql.matches(this.matchingAfterStatementRegex)) {
            this.matchingAfterStatementCount++;
        }
        if (this.matchingAfterStatementCount >= this.matchingAfterStatementThreshold) {
            this.matchingAfterStatementCount = 0;
            try {
                this.proxy.pickNewConnection();
            } catch (SQLException e) {
            }
        }
        return originalResultSet;
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2
    public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection) throws SQLException {
        return null;
    }

    void pauseCounters() {
        this.countStatements = false;
    }

    void resumeCounters() {
        this.countStatements = true;
    }
}
