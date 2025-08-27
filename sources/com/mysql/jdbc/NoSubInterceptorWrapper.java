package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/NoSubInterceptorWrapper.class */
public class NoSubInterceptorWrapper implements StatementInterceptorV2 {
    private final StatementInterceptorV2 underlyingInterceptor;

    public NoSubInterceptorWrapper(StatementInterceptorV2 underlyingInterceptor) {
        if (underlyingInterceptor == null) {
            throw new RuntimeException("Interceptor to be wrapped can not be NULL");
        }
        this.underlyingInterceptor = underlyingInterceptor;
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2, com.mysql.jdbc.Extension
    public void destroy() {
        this.underlyingInterceptor.destroy();
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2
    public boolean executeTopLevelOnly() {
        return this.underlyingInterceptor.executeTopLevelOnly();
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2, com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
        this.underlyingInterceptor.init(conn, props);
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2
    public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection, int warningCount, boolean noIndexUsed, boolean noGoodIndexUsed, SQLException statementException) throws SQLException {
        this.underlyingInterceptor.postProcess(sql, interceptedStatement, originalResultSet, connection, warningCount, noIndexUsed, noGoodIndexUsed, statementException);
        return null;
    }

    @Override // com.mysql.jdbc.StatementInterceptorV2
    public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection) throws SQLException {
        this.underlyingInterceptor.preProcess(sql, interceptedStatement, connection);
        return null;
    }

    public StatementInterceptorV2 getUnderlyingInterceptor() {
        return this.underlyingInterceptor;
    }
}
