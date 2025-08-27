package org.springframework.jdbc.support.nativejdbc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.util.ReflectionUtils;

@Deprecated
/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/nativejdbc/CommonsDbcpNativeJdbcExtractor.class */
public class CommonsDbcpNativeJdbcExtractor extends NativeJdbcExtractorAdapter {
    private static final String GET_INNERMOST_DELEGATE_METHOD_NAME = "getInnermostDelegate";

    private static Object getInnermostDelegate(Object obj) throws SQLException, NoSuchMethodException, SecurityException {
        if (obj == null) {
            return null;
        }
        try {
            Class<?> classToAnalyze = obj.getClass();
            while (!Modifier.isPublic(classToAnalyze.getModifiers())) {
                classToAnalyze = classToAnalyze.getSuperclass();
                if (classToAnalyze == null) {
                    return obj;
                }
            }
            Method getInnermostDelegate = classToAnalyze.getMethod(GET_INNERMOST_DELEGATE_METHOD_NAME, (Class[]) null);
            Object delegate = ReflectionUtils.invokeJdbcMethod(getInnermostDelegate, obj);
            return delegate != null ? delegate : obj;
        } catch (NoSuchMethodException e) {
            return obj;
        } catch (SecurityException ex) {
            throw new IllegalStateException("Commons DBCP getInnermostDelegate method is not accessible: " + ex);
        }
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter
    protected Connection doGetNativeConnection(Connection con) throws SQLException {
        return (Connection) getInnermostDelegate(con);
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public Statement getNativeStatement(Statement stmt) throws SQLException {
        return (Statement) getInnermostDelegate(stmt);
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public PreparedStatement getNativePreparedStatement(PreparedStatement ps) throws SQLException {
        return (PreparedStatement) getNativeStatement(ps);
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public CallableStatement getNativeCallableStatement(CallableStatement cs) throws SQLException {
        return (CallableStatement) getNativeStatement(cs);
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public ResultSet getNativeResultSet(ResultSet rs) throws SQLException {
        return (ResultSet) getInnermostDelegate(rs);
    }
}
