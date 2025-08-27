package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/LoadBalancedConnection.class */
public interface LoadBalancedConnection extends MySQLConnection {
    boolean addHost(String str) throws SQLException;

    void removeHost(String str) throws SQLException;

    void removeHostWhenNotInUse(String str) throws SQLException;

    void ping(boolean z) throws SQLException;
}
