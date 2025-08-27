package com.mysql.jdbc.jdbc2.optional;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jdbc2/optional/MysqlXADataSource.class */
public class MysqlXADataSource extends MysqlDataSource implements XADataSource {
    static final long serialVersionUID = 7911390333152247455L;

    public XAConnection getXAConnection() throws SQLException {
        Connection conn = getConnection();
        return wrapConnection(conn);
    }

    public XAConnection getXAConnection(String u, String p) throws SQLException {
        Connection conn = getConnection(u, p);
        return wrapConnection(conn);
    }

    private XAConnection wrapConnection(Connection conn) throws SQLException {
        if (getPinGlobalTxToPhysicalConnection() || ((com.mysql.jdbc.Connection) conn).getPinGlobalTxToPhysicalConnection()) {
            return SuspendableXAConnection.getInstance((com.mysql.jdbc.Connection) conn);
        }
        return MysqlXAConnection.getInstance((com.mysql.jdbc.Connection) conn, getLogXaCommands());
    }
}
