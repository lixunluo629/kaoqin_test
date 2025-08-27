package com.mysql.jdbc;

import java.sql.Array;
import java.sql.NClob;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4MySQLConnection.class */
public interface JDBC4MySQLConnection extends MySQLConnection {
    SQLXML createSQLXML() throws SQLException;

    Array createArrayOf(String str, Object[] objArr) throws SQLException;

    Struct createStruct(String str, Object[] objArr) throws SQLException;

    Properties getClientInfo() throws SQLException;

    String getClientInfo(String str) throws SQLException;

    boolean isValid(int i) throws SQLException;

    void setClientInfo(Properties properties) throws SQLClientInfoException;

    void setClientInfo(String str, String str2) throws SQLClientInfoException;

    boolean isWrapperFor(Class<?> cls) throws SQLException;

    <T> T unwrap(Class<T> cls) throws SQLException;

    java.sql.Blob createBlob();

    java.sql.Clob createClob();

    NClob createNClob();

    JDBC4ClientInfoProvider getClientInfoProviderImpl() throws SQLException;
}
