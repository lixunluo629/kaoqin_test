package org.springframework.jdbc.support.nativejdbc;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/nativejdbc/JBossNativeJdbcExtractor.class */
public class JBossNativeJdbcExtractor extends NativeJdbcExtractorAdapter {
    private static final String JBOSS_JCA_PREFIX = "org.jboss.jca.adapters.jdbc.";
    private static final String JBOSS_RESOURCE_PREFIX = "org.jboss.resource.adapter.jdbc.";
    private Class<?> wrappedConnectionClass;
    private Class<?> wrappedStatementClass;
    private Class<?> wrappedResultSetClass;
    private Method getUnderlyingConnectionMethod;
    private Method getUnderlyingStatementMethod;
    private Method getUnderlyingResultSetMethod;

    public JBossNativeJdbcExtractor() {
        String prefix = JBOSS_JCA_PREFIX;
        try {
            this.wrappedConnectionClass = getClass().getClassLoader().loadClass(prefix + "WrappedConnection");
        } catch (ClassNotFoundException e) {
            prefix = JBOSS_RESOURCE_PREFIX;
            try {
                this.wrappedConnectionClass = getClass().getClassLoader().loadClass(prefix + "WrappedConnection");
            } catch (ClassNotFoundException e2) {
                throw new IllegalStateException("Could not initialize JBossNativeJdbcExtractor: neither JBoss 7's [org.jboss.jca.adapters.jdbc..WrappedConnection] nor traditional JBoss [org.jboss.resource.adapter.jdbc..WrappedConnection] found");
            }
        }
        try {
            this.wrappedStatementClass = getClass().getClassLoader().loadClass(prefix + "WrappedStatement");
            this.wrappedResultSetClass = getClass().getClassLoader().loadClass(prefix + "WrappedResultSet");
            this.getUnderlyingConnectionMethod = this.wrappedConnectionClass.getMethod("getUnderlyingConnection", (Class[]) null);
            this.getUnderlyingStatementMethod = this.wrappedStatementClass.getMethod("getUnderlyingStatement", (Class[]) null);
            this.getUnderlyingResultSetMethod = this.wrappedResultSetClass.getMethod("getUnderlyingResultSet", (Class[]) null);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize JBossNativeJdbcExtractor because of missing JBoss API methods/classes: " + ex);
        }
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter
    protected Connection doGetNativeConnection(Connection con) throws SQLException {
        if (this.wrappedConnectionClass.isAssignableFrom(con.getClass())) {
            return (Connection) ReflectionUtils.invokeJdbcMethod(this.getUnderlyingConnectionMethod, con);
        }
        return con;
    }

    @Override // org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractorAdapter, org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor
    public Statement getNativeStatement(Statement stmt) throws SQLException {
        if (this.wrappedStatementClass.isAssignableFrom(stmt.getClass())) {
            return (Statement) ReflectionUtils.invokeJdbcMethod(this.getUnderlyingStatementMethod, stmt);
        }
        return stmt;
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
        if (this.wrappedResultSetClass.isAssignableFrom(rs.getClass())) {
            return (ResultSet) ReflectionUtils.invokeJdbcMethod(this.getUnderlyingResultSetMethod, rs);
        }
        return rs;
    }
}
