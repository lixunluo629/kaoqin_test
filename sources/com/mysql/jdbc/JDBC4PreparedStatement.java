package com.mysql.jdbc;

import com.mysql.jdbc.PreparedStatement;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4PreparedStatement.class */
public class JDBC4PreparedStatement extends PreparedStatement {
    public JDBC4PreparedStatement(MySQLConnection conn, String catalog) throws SQLException {
        super(conn, catalog);
    }

    public JDBC4PreparedStatement(MySQLConnection conn, String sql, String catalog) throws SQLException {
        super(conn, sql, catalog);
    }

    public JDBC4PreparedStatement(MySQLConnection conn, String sql, String catalog, PreparedStatement.ParseInfo cachedParseInfo) throws SQLException {
        super(conn, sql, catalog, cachedParseInfo);
    }

    @Override // java.sql.PreparedStatement
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        JDBC4PreparedStatementHelper.setRowId(this, parameterIndex, x);
    }

    @Override // java.sql.PreparedStatement
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        JDBC4PreparedStatementHelper.setNClob(this, parameterIndex, value);
    }

    @Override // java.sql.PreparedStatement
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        JDBC4PreparedStatementHelper.setSQLXML(this, parameterIndex, xmlObject);
    }
}
