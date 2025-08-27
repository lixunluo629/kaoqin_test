package com.mysql.jdbc;

import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4ClientInfoProvider.class */
public interface JDBC4ClientInfoProvider {
    void initialize(java.sql.Connection connection, Properties properties) throws SQLException;

    void destroy() throws SQLException;

    Properties getClientInfo(java.sql.Connection connection) throws SQLException;

    String getClientInfo(java.sql.Connection connection, String str) throws SQLException;

    void setClientInfo(java.sql.Connection connection, Properties properties) throws SQLClientInfoException;

    void setClientInfo(java.sql.Connection connection, String str, String str2) throws SQLClientInfoException;
}
