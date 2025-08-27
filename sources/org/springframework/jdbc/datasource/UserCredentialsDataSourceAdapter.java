package org.springframework.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.UsesJava7;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/UserCredentialsDataSourceAdapter.class */
public class UserCredentialsDataSourceAdapter extends DelegatingDataSource {
    private String username;
    private String password;
    private String catalog;
    private String schema;
    private final ThreadLocal<JdbcUserCredentials> threadBoundCredentials = new NamedThreadLocal("Current JDBC user credentials");

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setCredentialsForCurrentThread(String username, String password) {
        this.threadBoundCredentials.set(new JdbcUserCredentials(username, password));
    }

    public void removeCredentialsFromCurrentThread() {
        this.threadBoundCredentials.remove();
    }

    @Override // org.springframework.jdbc.datasource.DelegatingDataSource, javax.sql.DataSource
    @UsesJava7
    public Connection getConnection() throws SQLException {
        Connection connectionDoGetConnection;
        JdbcUserCredentials threadCredentials = this.threadBoundCredentials.get();
        if (threadCredentials != null) {
            connectionDoGetConnection = doGetConnection(threadCredentials.username, threadCredentials.password);
        } else {
            connectionDoGetConnection = doGetConnection(this.username, this.password);
        }
        Connection con = connectionDoGetConnection;
        if (this.catalog != null) {
            con.setCatalog(this.catalog);
        }
        if (this.schema != null) {
            con.setSchema(this.schema);
        }
        return con;
    }

    @Override // org.springframework.jdbc.datasource.DelegatingDataSource, javax.sql.DataSource
    public Connection getConnection(String username, String password) throws SQLException {
        return doGetConnection(username, password);
    }

    protected Connection doGetConnection(String username, String password) throws SQLException {
        Assert.state(getTargetDataSource() != null, "'targetDataSource' is required");
        if (StringUtils.hasLength(username)) {
            return getTargetDataSource().getConnection(username, password);
        }
        return getTargetDataSource().getConnection();
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/UserCredentialsDataSourceAdapter$JdbcUserCredentials.class */
    private static class JdbcUserCredentials {
        public final String username;
        public final String password;

        private JdbcUserCredentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String toString() {
            return "JdbcUserCredentials[username='" + this.username + "',password='" + this.password + "']";
        }
    }
}
