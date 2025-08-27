package org.springframework.jdbc.datasource;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/WebSphereDataSourceAdapter.class */
public class WebSphereDataSourceAdapter extends IsolationLevelDataSourceAdapter {
    protected final Log logger = LogFactory.getLog(getClass());
    private Class<?> wsDataSourceClass;
    private Method newJdbcConnSpecMethod;
    private Method wsDataSourceGetConnectionMethod;
    private Method setTransactionIsolationMethod;
    private Method setReadOnlyMethod;
    private Method setUserNameMethod;
    private Method setPasswordMethod;

    public WebSphereDataSourceAdapter() throws ClassNotFoundException {
        try {
            this.wsDataSourceClass = getClass().getClassLoader().loadClass("com.ibm.websphere.rsadapter.WSDataSource");
            Class<?> jdbcConnSpecClass = getClass().getClassLoader().loadClass("com.ibm.websphere.rsadapter.JDBCConnectionSpec");
            Class<?> wsrraFactoryClass = getClass().getClassLoader().loadClass("com.ibm.websphere.rsadapter.WSRRAFactory");
            this.newJdbcConnSpecMethod = wsrraFactoryClass.getMethod("createJDBCConnectionSpec", new Class[0]);
            this.wsDataSourceGetConnectionMethod = this.wsDataSourceClass.getMethod("getConnection", jdbcConnSpecClass);
            this.setTransactionIsolationMethod = jdbcConnSpecClass.getMethod("setTransactionIsolation", Integer.TYPE);
            this.setReadOnlyMethod = jdbcConnSpecClass.getMethod("setReadOnly", Boolean.class);
            this.setUserNameMethod = jdbcConnSpecClass.getMethod("setUserName", String.class);
            this.setPasswordMethod = jdbcConnSpecClass.getMethod("setPassword", String.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize WebSphereDataSourceAdapter because WebSphere API classes are not available: " + ex);
        }
    }

    @Override // org.springframework.jdbc.datasource.DelegatingDataSource, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        if (!this.wsDataSourceClass.isInstance(getTargetDataSource())) {
            throw new IllegalStateException("Specified 'targetDataSource' is not a WebSphere WSDataSource: " + getTargetDataSource());
        }
    }

    @Override // org.springframework.jdbc.datasource.IsolationLevelDataSourceAdapter, org.springframework.jdbc.datasource.UserCredentialsDataSourceAdapter
    protected Connection doGetConnection(String username, String password) throws SQLException {
        Object connSpec = createConnectionSpec(getCurrentIsolationLevel(), getCurrentReadOnlyFlag(), username, password);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Obtaining JDBC Connection from WebSphere DataSource [" + getTargetDataSource() + "], using ConnectionSpec [" + connSpec + "]");
        }
        return (Connection) ReflectionUtils.invokeJdbcMethod(this.wsDataSourceGetConnectionMethod, getTargetDataSource(), connSpec);
    }

    protected Object createConnectionSpec(Integer isolationLevel, Boolean readOnlyFlag, String username, String password) throws SQLException {
        Object connSpec = ReflectionUtils.invokeJdbcMethod(this.newJdbcConnSpecMethod, null);
        if (isolationLevel != null) {
            ReflectionUtils.invokeJdbcMethod(this.setTransactionIsolationMethod, connSpec, isolationLevel);
        }
        if (readOnlyFlag != null) {
            ReflectionUtils.invokeJdbcMethod(this.setReadOnlyMethod, connSpec, readOnlyFlag);
        }
        if (StringUtils.hasLength(username)) {
            ReflectionUtils.invokeJdbcMethod(this.setUserNameMethod, connSpec, username);
            ReflectionUtils.invokeJdbcMethod(this.setPasswordMethod, connSpec, password);
        }
        return connSpec;
    }
}
