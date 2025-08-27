package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/V1toV2StatementInterceptorAdapter.class */
public class V1toV2StatementInterceptorAdapter implements StatementInterceptorV2 {
    private final StatementInterceptor toProxy;

    public V1toV2StatementInterceptorAdapter(StatementInterceptor toProxy) {
        this.toProxy = toProxy;
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2
    public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection, int warningCount, boolean noIndexUsed, boolean noGoodIndexUsed, SQLException statementException) throws SQLException {
        return this.toProxy.postProcess(sql, interceptedStatement, originalResultSet, connection);
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2, com.mysql.jdbc.Extension
    public void destroy() {
        this.toProxy.destroy();
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2
    public boolean executeTopLevelOnly() {
        return this.toProxy.executeTopLevelOnly();
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2, com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
        this.toProxy.init(conn, props);
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2
    public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection) throws SQLException {
        return this.toProxy.preProcess(sql, interceptedStatement, connection);
    }
}
