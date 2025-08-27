package org.springframework.jdbc.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/DelegatingDataSource.class */
public class DelegatingDataSource implements DataSource, InitializingBean {
    private DataSource targetDataSource;

    public DelegatingDataSource() {
    }

    public DelegatingDataSource(DataSource targetDataSource) {
        setTargetDataSource(targetDataSource);
    }

    public void setTargetDataSource(DataSource targetDataSource) {
        Assert.notNull(targetDataSource, "'targetDataSource' must not be null");
        this.targetDataSource = targetDataSource;
    }

    public DataSource getTargetDataSource() {
        return this.targetDataSource;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (getTargetDataSource() == null) {
            throw new IllegalArgumentException("Property 'targetDataSource' is required");
        }
    }

    @Override // javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        return getTargetDataSource().getConnection();
    }

    @Override // javax.sql.DataSource
    public Connection getConnection(String username, String password) throws SQLException {
        return getTargetDataSource().getConnection(username, password);
    }

    @Override // javax.sql.CommonDataSource
    public PrintWriter getLogWriter() throws SQLException {
        return getTargetDataSource().getLogWriter();
    }

    @Override // javax.sql.CommonDataSource
    public void setLogWriter(PrintWriter out) throws SQLException {
        getTargetDataSource().setLogWriter(out);
    }

    @Override // javax.sql.CommonDataSource
    public int getLoginTimeout() throws SQLException {
        return getTargetDataSource().getLoginTimeout();
    }

    @Override // javax.sql.CommonDataSource
    public void setLoginTimeout(int seconds) throws SQLException {
        getTargetDataSource().setLoginTimeout(seconds);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> cls) throws SQLException {
        if (cls.isInstance(this)) {
            return this;
        }
        return (T) getTargetDataSource().unwrap(cls);
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this) || getTargetDataSource().isWrapperFor(iface);
    }

    @Override // javax.sql.CommonDataSource
    public Logger getParentLogger() {
        return Logger.getLogger("global");
    }
}
