package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/MiniAdmin.class */
public class MiniAdmin {
    private Connection conn;

    public MiniAdmin(java.sql.Connection conn) throws SQLException {
        if (conn == null) {
            throw SQLError.createSQLException(Messages.getString("MiniAdmin.0"), SQLError.SQL_STATE_GENERAL_ERROR, (ExceptionInterceptor) null);
        }
        if (!(conn instanceof Connection)) {
            throw SQLError.createSQLException(Messages.getString("MiniAdmin.1"), SQLError.SQL_STATE_GENERAL_ERROR, ((ConnectionImpl) conn).getExceptionInterceptor());
        }
        this.conn = (Connection) conn;
    }

    public MiniAdmin(String jdbcUrl) throws SQLException {
        this(jdbcUrl, new Properties());
    }

    public MiniAdmin(String jdbcUrl, Properties props) throws SQLException {
        this.conn = (Connection) new Driver().connect(jdbcUrl, props);
    }

    public void shutdown() throws SQLException {
        this.conn.shutdownServer();
    }
}
