package org.springframework.jdbc.datasource;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/SimpleDriverDataSource.class */
public class SimpleDriverDataSource extends AbstractDriverBasedDataSource {
    private Driver driver;

    public SimpleDriverDataSource() {
    }

    public SimpleDriverDataSource(Driver driver, String url) {
        setDriver(driver);
        setUrl(url);
    }

    public SimpleDriverDataSource(Driver driver, String url, String username, String password) {
        setDriver(driver);
        setUrl(url);
        setUsername(username);
        setPassword(password);
    }

    public SimpleDriverDataSource(Driver driver, String url, Properties conProps) {
        setDriver(driver);
        setUrl(url);
        setConnectionProperties(conProps);
    }

    public void setDriverClass(Class<? extends Driver> driverClass) {
        this.driver = (Driver) BeanUtils.instantiateClass(driverClass);
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return this.driver;
    }

    @Override // org.springframework.jdbc.datasource.AbstractDriverBasedDataSource
    protected Connection getConnectionFromDriver(Properties props) throws SQLException {
        Driver driver = getDriver();
        String url = getUrl();
        Assert.notNull(driver, "Driver must not be null");
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Creating new JDBC Driver Connection to [" + url + "]");
        }
        return driver.connect(url, props);
    }
}
