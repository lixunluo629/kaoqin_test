package com.mysql.fabric.jdbc;

import com.mysql.fabric.FabricConnection;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.JDBC4ClientInfoProvider;
import com.mysql.jdbc.JDBC4MySQLConnection;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/jdbc/JDBC4FabricMySQLConnectionProxy.class */
public class JDBC4FabricMySQLConnectionProxy extends FabricMySQLConnectionProxy implements JDBC4FabricMySQLConnection, FabricMySQLConnectionProperties {
    private static final long serialVersionUID = 5845485979107347258L;
    private FabricConnection fabricConnection;

    public JDBC4FabricMySQLConnectionProxy(Properties props) throws SQLException {
        super(props);
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public Blob createBlob() {
        try {
            transactionBegun();
            return getActiveConnection().createBlob();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public Clob createClob() {
        try {
            transactionBegun();
            return getActiveConnection().createClob();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public NClob createNClob() {
        try {
            transactionBegun();
            return getActiveConnection().createNClob();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public SQLXML createSQLXML() throws SQLException {
        transactionBegun();
        return getActiveConnection().createSQLXML();
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        for (Connection c : this.serverConnections.values()) {
            c.setClientInfo(properties);
        }
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        for (Connection c : this.serverConnections.values()) {
            c.setClientInfo(name, value);
        }
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return getActiveConnection().createArrayOf(typeName, elements);
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        transactionBegun();
        return getActiveConnection().createStruct(typeName, attributes);
    }

    @Override // com.mysql.jdbc.JDBC4MySQLConnection
    public JDBC4ClientInfoProvider getClientInfoProviderImpl() throws SQLException {
        return ((JDBC4MySQLConnection) getActiveConnection()).getClientInfoProviderImpl();
    }
}
