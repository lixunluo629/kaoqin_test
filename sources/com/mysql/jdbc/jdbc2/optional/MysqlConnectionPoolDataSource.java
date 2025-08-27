package com.mysql.jdbc.jdbc2.optional;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jdbc2/optional/MysqlConnectionPoolDataSource.class */
public class MysqlConnectionPoolDataSource extends MysqlDataSource implements ConnectionPoolDataSource {
    static final long serialVersionUID = -7767325445592304961L;

    @Override // javax.sql.ConnectionPoolDataSource
    public synchronized PooledConnection getPooledConnection() throws SQLException {
        Connection connection = getConnection();
        MysqlPooledConnection mysqlPooledConnection = MysqlPooledConnection.getInstance((com.mysql.jdbc.Connection) connection);
        return mysqlPooledConnection;
    }

    @Override // javax.sql.ConnectionPoolDataSource
    public synchronized PooledConnection getPooledConnection(String s, String s1) throws SQLException {
        Connection connection = getConnection(s, s1);
        MysqlPooledConnection mysqlPooledConnection = MysqlPooledConnection.getInstance((com.mysql.jdbc.Connection) connection);
        return mysqlPooledConnection;
    }
}
