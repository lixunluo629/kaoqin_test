package org.springframework.jdbc.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/DriverManagerDataSource.class */
public class DriverManagerDataSource extends AbstractDriverBasedDataSource {
    public DriverManagerDataSource() {
    }

    public DriverManagerDataSource(String url) {
        setUrl(url);
    }

    public DriverManagerDataSource(String url, String username, String password) {
        setUrl(url);
        setUsername(username);
        setPassword(password);
    }

    public DriverManagerDataSource(String url, Properties conProps) {
        setUrl(url);
        setConnectionProperties(conProps);
    }

    public void setDriverClassName(String driverClassName) throws ClassNotFoundException {
        Assert.hasText(driverClassName, "Property 'driverClassName' must not be empty");
        String driverClassNameToUse = driverClassName.trim();
        try {
            Class.forName(driverClassNameToUse, true, ClassUtils.getDefaultClassLoader());
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Loaded JDBC driver: " + driverClassNameToUse);
            }
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassNameToUse + "]", ex);
        }
    }

    @Override // org.springframework.jdbc.datasource.AbstractDriverBasedDataSource
    protected Connection getConnectionFromDriver(Properties props) throws SQLException {
        String url = getUrl();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Creating new JDBC DriverManager Connection to [" + url + "]");
        }
        return getConnectionFromDriverManager(url, props);
    }

    protected Connection getConnectionFromDriverManager(String url, Properties props) throws SQLException {
        return DriverManager.getConnection(url, props);
    }
}
