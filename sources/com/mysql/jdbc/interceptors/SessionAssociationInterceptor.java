package com.mysql.jdbc.interceptors;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/interceptors/SessionAssociationInterceptor.class */
public class SessionAssociationInterceptor implements StatementInterceptor {
    protected String currentSessionKey;
    protected static final ThreadLocal<String> sessionLocal = new ThreadLocal<>();

    public static final void setSessionKey(String key) {
        sessionLocal.set(key);
    }

    public static final void resetSessionKey() {
        sessionLocal.set(null);
    }

    public static final String getSessionKey() {
        return sessionLocal.get();
    }

    @Override // com.mysql.jdbc.StatementInterceptor
    public boolean executeTopLevelOnly() {
        return true;
    }

    @Override // com.mysql.jdbc.StatementInterceptor, com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
    }

    @Override // com.mysql.jdbc.StatementInterceptor
    public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection) throws SQLException {
        return null;
    }

    @Override // com.mysql.jdbc.StatementInterceptor
    public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection) throws SQLException {
        String key = getSessionKey();
        if (key != null && !key.equals(this.currentSessionKey)) {
            PreparedStatement pstmt = connection.clientPrepareStatement("SET @mysql_proxy_session=?");
            try {
                pstmt.setString(1, key);
                pstmt.execute();
                this.currentSessionKey = key;
                return null;
            } finally {
                pstmt.close();
            }
        }
        return null;
    }

    @Override // com.mysql.jdbc.StatementInterceptor, com.mysql.jdbc.Extension
    public void destroy() {
    }
}
