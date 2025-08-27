package org.springframework.jdbc.support.nativejdbc;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/nativejdbc/WebLogicNativeJdbcExtractor.class */
public class WebLogicNativeJdbcExtractor extends NativeJdbcExtractorAdapter {
    private static final String JDBC_EXTENSION_NAME = "weblogic.jdbc.extensions.WLConnection";
    private final Class<?> jdbcExtensionClass;
    private final Method getVendorConnectionMethod;

    public WebLogicNativeJdbcExtractor() {
        try {
            this.jdbcExtensionClass = getClass().getClassLoader().loadClass(JDBC_EXTENSION_NAME);
            this.getVendorConnectionMethod = this.jdbcExtensionClass.getMethod("getVendorConnection", (Class[]) null);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize WebLogicNativeJdbcExtractor because WebLogic API classes are not available: " + ex);
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
        if (this.jdbcExtensionClass.isAssignableFrom(con.getClass())) {
            return (Connection) ReflectionUtils.invokeJdbcMethod(this.getVendorConnectionMethod, con);
        }
        return con;
    }
}
