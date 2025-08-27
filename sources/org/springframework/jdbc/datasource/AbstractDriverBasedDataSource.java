package org.springframework.jdbc.datasource;

import com.mysql.jdbc.NonRegisteringDriver;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.springframework.lang.UsesJava7;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/AbstractDriverBasedDataSource.class */
public abstract class AbstractDriverBasedDataSource extends AbstractDataSource {
    private String url;
    private String username;
    private String password;
    private String catalog;
    private String schema;
    private Properties connectionProperties;

    protected abstract Connection getConnectionFromDriver(Properties properties) throws SQLException;

    public void setUrl(String url) {
        Assert.hasText(url, "Property 'url' must not be empty");
        this.url = url.trim();
    }

    public String getUrl() {
        return this.url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getCatalog() {
        return this.catalog;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public Properties getConnectionProperties() {
        return this.connectionProperties;
    }

    @Override // javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        return getConnectionFromDriver(getUsername(), getPassword());
    }

    @Override // javax.sql.DataSource
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnectionFromDriver(username, password);
    }

    @UsesJava7
    protected Connection getConnectionFromDriver(String username, String password) throws SQLException {
        Properties mergedProps = new Properties();
        Properties connProps = getConnectionProperties();
        if (connProps != null) {
            mergedProps.putAll(connProps);
        }
        if (username != null) {
            mergedProps.setProperty("user", username);
        }
        if (password != null) {
            mergedProps.setProperty(NonRegisteringDriver.PASSWORD_PROPERTY_KEY, password);
        }
        Connection con = getConnectionFromDriver(mergedProps);
        if (this.catalog != null) {
            con.setCatalog(this.catalog);
        }
        if (this.schema != null) {
            con.setSchema(this.schema);
        }
        return con;
    }
}
