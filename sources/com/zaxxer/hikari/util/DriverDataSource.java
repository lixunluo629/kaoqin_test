package com.zaxxer.hikari.util;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/DriverDataSource.class */
public final class DriverDataSource implements DataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) DriverDataSource.class);
    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private final String jdbcUrl;
    private final Properties driverProperties = new Properties();
    private Driver driver;

    public DriverDataSource(String jdbcUrl, String driverClassName, Properties properties, String username, String password) throws ClassNotFoundException {
        this.jdbcUrl = jdbcUrl;
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            this.driverProperties.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
        if (username != null) {
            this.driverProperties.put("user", this.driverProperties.getProperty("user", username));
        }
        if (password != null) {
            this.driverProperties.put("password", this.driverProperties.getProperty("password", password));
        }
        if (driverClassName != null) {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (true) {
                if (!drivers.hasMoreElements()) {
                    break;
                }
                Driver d = drivers.nextElement();
                if (d.getClass().getName().equals(driverClassName)) {
                    this.driver = d;
                    break;
                }
            }
            if (this.driver == null) {
                LOGGER.warn("Registered driver with driverClassName={} was not found, trying direct instantiation.", driverClassName);
                Class<?> driverClass = null;
                ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
                try {
                    if (threadContextClassLoader != null) {
                        try {
                            driverClass = threadContextClassLoader.loadClass(driverClassName);
                            LOGGER.debug("Driver class {} found in Thread context class loader {}", driverClassName, threadContextClassLoader);
                        } catch (ClassNotFoundException e) {
                            LOGGER.debug("Driver class {} not found in Thread context class loader {}, trying classloader {}", driverClassName, threadContextClassLoader, getClass().getClassLoader());
                        }
                    }
                    if (driverClass == null) {
                        driverClass = getClass().getClassLoader().loadClass(driverClassName);
                        LOGGER.debug("Driver class {} found in the HikariConfig class classloader {}", driverClassName, getClass().getClassLoader());
                    }
                } catch (ClassNotFoundException e2) {
                    LOGGER.debug("Failed to load driver class {} from HikariConfig class classloader {}", driverClassName, getClass().getClassLoader());
                }
                if (driverClass != null) {
                    try {
                        this.driver = (Driver) driverClass.newInstance();
                    } catch (Exception e3) {
                        LOGGER.warn("Failed to create instance of driver class {}, trying jdbcUrl resolution", driverClassName, e3);
                    }
                }
            }
        }
        String sanitizedUrl = jdbcUrl.replaceAll("([?&;]password=)[^&#;]*(.*)", "$1<masked>$2");
        try {
            if (this.driver == null) {
                this.driver = DriverManager.getDriver(jdbcUrl);
                LOGGER.debug("Loaded driver with class name {} for jdbcUrl={}", this.driver.getClass().getName(), sanitizedUrl);
            } else if (!this.driver.acceptsURL(jdbcUrl)) {
                throw new RuntimeException("Driver " + driverClassName + " claims to not accept jdbcUrl, " + sanitizedUrl);
            }
        } catch (SQLException e4) {
            throw new RuntimeException("Failed to get driver instance for jdbcUrl=" + sanitizedUrl, e4);
        }
    }

    @Override // javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        return this.driver.connect(this.jdbcUrl, this.driverProperties);
    }

    @Override // javax.sql.DataSource
    public Connection getConnection(String username, String password) throws SQLException {
        Properties cloned = (Properties) this.driverProperties.clone();
        if (username != null) {
            cloned.put("user", username);
            if (cloned.containsKey("username")) {
                cloned.put("username", username);
            }
        }
        if (password != null) {
            cloned.put("password", password);
        }
        return this.driver.connect(this.jdbcUrl, cloned);
    }

    @Override // javax.sql.CommonDataSource
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // javax.sql.CommonDataSource
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // javax.sql.CommonDataSource
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override // javax.sql.CommonDataSource
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override // javax.sql.CommonDataSource
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.driver.getParentLogger();
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
