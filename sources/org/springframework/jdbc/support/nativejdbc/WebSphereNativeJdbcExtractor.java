package org.springframework.jdbc.support.nativejdbc;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/nativejdbc/WebSphereNativeJdbcExtractor.class */
public class WebSphereNativeJdbcExtractor extends NativeJdbcExtractorAdapter {
    private static final String JDBC_ADAPTER_CONNECTION_NAME = "com.ibm.ws.rsadapter.jdbc.WSJdbcConnection";
    private static final String JDBC_ADAPTER_UTIL_NAME = "com.ibm.ws.rsadapter.jdbc.WSJdbcUtil";
    private Class<?> webSphereConnectionClass;
    private Method webSphereNativeConnectionMethod;

    public WebSphereNativeJdbcExtractor() throws ClassNotFoundException {
        try {
            this.webSphereConnectionClass = getClass().getClassLoader().loadClass(JDBC_ADAPTER_CONNECTION_NAME);
            Class<?> jdbcAdapterUtilClass = getClass().getClassLoader().loadClass(JDBC_ADAPTER_UTIL_NAME);
            this.webSphereNativeConnectionMethod = jdbcAdapterUtilClass.getMethod("getNativeConnection", this.webSphereConnectionClass);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize WebSphereNativeJdbcExtractor because WebSphere API classes are not available: " + ex);
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
        return this.webSphereConnectionClass.isAssignableFrom(con.getClass()) ? (Connection) ReflectionUtils.invokeJdbcMethod(this.webSphereNativeConnectionMethod, null, con) : con;
    }
}
