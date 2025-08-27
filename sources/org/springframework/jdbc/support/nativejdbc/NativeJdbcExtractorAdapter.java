package org.springframework.jdbc.support.nativejdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.jdbc.datasource.DataSourceUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/nativejdbc/NativeJdbcExtractorAdapter.class */
public abstract class NativeJdbcExtractorAdapter implements NativeJdbcExtractor {
    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public boolean isNativeConnectionNecessaryForNativeStatements() {
        return false;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public boolean isNativeConnectionNecessaryForNativePreparedStatements() {
        return false;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public boolean isNativeConnectionNecessaryForNativeCallableStatements() {
        return false;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public Connection getNativeConnection(Connection con) throws SQLException {
        DatabaseMetaData metaData;
        Connection metaCon;
        if (con == null) {
            return null;
        }
        Connection targetCon = DataSourceUtils.getTargetConnection(con);
        Connection nativeCon = doGetNativeConnection(targetCon);
        if (nativeCon == targetCon && (metaData = targetCon.getMetaData()) != null && (metaCon = metaData.getConnection()) != null && metaCon != targetCon) {
            nativeCon = doGetNativeConnection(metaCon);
        }
        return nativeCon;
    }

    protected Connection doGetNativeConnection(Connection con) throws SQLException {
        return con;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public Connection getNativeConnectionFromStatement(Statement stmt) throws SQLException {
        if (stmt == null) {
            return null;
        }
        return getNativeConnection(stmt.getConnection());
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public Statement getNativeStatement(Statement stmt) throws SQLException {
        return stmt;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public PreparedStatement getNativePreparedStatement(PreparedStatement ps) throws SQLException {
        return ps;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public CallableStatement getNativeCallableStatement(CallableStatement cs) throws SQLException {
        return cs;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public ResultSet getNativeResultSet(ResultSet rs) throws SQLException {
        return rs;
    }
}
