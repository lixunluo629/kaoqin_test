package org.springframework.jdbc.support.nativejdbc;

import com.mchange.v2.c3p0.C3P0ProxyConnection;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/nativejdbc/C3P0NativeJdbcExtractor.class */
public class C3P0NativeJdbcExtractor extends NativeJdbcExtractorAdapter {
    private final Method getRawConnectionMethod;

    public static Connection getRawConnection(Connection con) {
        return con;
    }

    public C3P0NativeJdbcExtractor() {
        try {
            this.getRawConnectionMethod = getClass().getMethod("getRawConnection", Connection.class);
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException("Internal error in C3P0NativeJdbcExtractor: " + ex.getMessage());
        }
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public boolean isNativeConnectionNecessaryForNativeStatements() {
        return true;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public boolean isNativeConnectionNecessaryForNativePreparedStatements() {
        return true;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public boolean isNativeConnectionNecessaryForNativeCallableStatements() {
        return true;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter
    protected Connection doGetNativeConnection(Connection con) throws SQLException {
        if (con instanceof C3P0ProxyConnection) {
            C3P0ProxyConnection cpCon = (C3P0ProxyConnection) con;
            try {
                return (Connection) cpCon.rawConnectionOperation(this.getRawConnectionMethod, (Object) null, new Object[]{C3P0ProxyConnection.RAW_CONNECTION});
            } catch (SQLException ex) {
                throw ex;
            } catch (Exception ex2) {
                ReflectionUtils.handleReflectionException(ex2);
            }
        }
        return con;
    }
}
