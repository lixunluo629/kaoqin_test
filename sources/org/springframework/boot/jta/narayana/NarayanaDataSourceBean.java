package org.springframework.boot.jta.narayana;

import com.arjuna.ats.internal.jdbc.ConnectionManager;
import com.arjuna.ats.jdbc.TransactionalDriver;
import com.mysql.jdbc.NonRegisteringDriver;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jta/narayana/NarayanaDataSourceBean.class */
public class NarayanaDataSourceBean implements DataSource {
    private final XADataSource xaDataSource;

    public NarayanaDataSourceBean(XADataSource xaDataSource) {
        Assert.notNull(xaDataSource, "XADataSource must not be null");
        this.xaDataSource = xaDataSource;
    }

    @Override // javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        properties.put(TransactionalDriver.XADataSource, this.xaDataSource);
        return ConnectionManager.create((String) null, properties);
    }

    @Override // javax.sql.DataSource
    public Connection getConnection(String username, String password) throws SQLException {
        Properties properties = new Properties();
        properties.put(TransactionalDriver.XADataSource, this.xaDataSource);
        properties.put("user", username);
        properties.put(NonRegisteringDriver.PASSWORD_PROPERTY_KEY, password);
        return ConnectionManager.create((String) null, properties);
    }

    @Override // javax.sql.CommonDataSource
    public PrintWriter getLogWriter() throws SQLException {
        return this.xaDataSource.getLogWriter();
    }

    @Override // javax.sql.CommonDataSource
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.xaDataSource.setLogWriter(out);
    }

    @Override // javax.sql.CommonDataSource
    public void setLoginTimeout(int seconds) throws SQLException {
        this.xaDataSource.setLoginTimeout(seconds);
    }

    @Override // javax.sql.CommonDataSource
    public int getLoginTimeout() throws SQLException {
        return this.xaDataSource.getLoginTimeout();
    }

    @Override // javax.sql.CommonDataSource
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> cls) throws SQLException {
        if (isWrapperFor(cls)) {
            return this;
        }
        if (ClassUtils.isAssignableValue(cls, this.xaDataSource)) {
            return (T) this.xaDataSource;
        }
        throw new SQLException(getClass() + " is not a wrapper for " + cls);
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isAssignableFrom(getClass());
    }
}
