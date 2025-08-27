package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.ConnectionPropertiesImpl;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.NonRegisteringDriver;
import com.mysql.jdbc.SQLError;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.DataSource;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jdbc2/optional/MysqlDataSource.class */
public class MysqlDataSource extends ConnectionPropertiesImpl implements DataSource, Referenceable, Serializable {
    static final long serialVersionUID = -5515846944416881264L;
    protected static final NonRegisteringDriver mysqlDriver;
    protected transient PrintWriter logWriter = null;
    protected String databaseName = null;
    protected String encoding = null;
    protected String hostName = null;
    protected String password = null;
    protected String profileSql = "false";
    protected String url = null;
    protected String user = null;
    protected boolean explicitUrl = false;
    protected int port = 3306;

    static {
        try {
            mysqlDriver = new NonRegisteringDriver();
        } catch (Exception e) {
            throw new RuntimeException("Can not load Driver class com.mysql.jdbc.Driver");
        }
    }

    @Override // javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        return getConnection(this.user, this.password);
    }

    @Override // javax.sql.DataSource
    public Connection getConnection(String userID, String pass) throws SQLException {
        Properties props = new Properties();
        if (userID != null) {
            props.setProperty("user", userID);
        }
        if (pass != null) {
            props.setProperty(NonRegisteringDriver.PASSWORD_PROPERTY_KEY, pass);
        }
        exposeAsProperties(props);
        return getConnection(props);
    }

    public void setDatabaseName(String dbName) {
        this.databaseName = dbName;
    }

    public String getDatabaseName() {
        return this.databaseName != null ? this.databaseName : "";
    }

    @Override // javax.sql.CommonDataSource
    public void setLogWriter(PrintWriter output) throws SQLException {
        this.logWriter = output;
    }

    @Override // javax.sql.CommonDataSource
    public PrintWriter getLogWriter() {
        return this.logWriter;
    }

    @Override // javax.sql.CommonDataSource
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override // javax.sql.CommonDataSource
    public int getLoginTimeout() {
        return 0;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public void setPort(int p) {
        this.port = p;
    }

    public int getPort() {
        return this.port;
    }

    public void setPortNumber(int p) {
        setPort(p);
    }

    public int getPortNumber() {
        return getPort();
    }

    public void setPropertiesViaRef(Reference ref) throws SQLException, ClassNotFoundException {
        super.initializeFromRef(ref);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.naming.NamingException */
    public Reference getReference() throws NamingException {
        Reference ref = new Reference(getClass().getName(), "com.mysql.jdbc.jdbc2.optional.MysqlDataSourceFactory", (String) null);
        ref.add(new StringRefAddr("user", getUser()));
        ref.add(new StringRefAddr(NonRegisteringDriver.PASSWORD_PROPERTY_KEY, this.password));
        ref.add(new StringRefAddr("serverName", getServerName()));
        ref.add(new StringRefAddr("port", "" + getPort()));
        ref.add(new StringRefAddr("databaseName", getDatabaseName()));
        ref.add(new StringRefAddr(RtspHeaders.Values.URL, getUrl()));
        ref.add(new StringRefAddr("explicitUrl", String.valueOf(this.explicitUrl)));
        try {
            storeToRef(ref);
            return ref;
        } catch (SQLException sqlEx) {
            throw new NamingException(sqlEx.getMessage());
        }
    }

    public void setServerName(String serverName) {
        this.hostName = serverName;
    }

    public String getServerName() {
        return this.hostName != null ? this.hostName : "";
    }

    public void setURL(String url) {
        setUrl(url);
    }

    public String getURL() {
        return getUrl();
    }

    public void setUrl(String url) {
        this.url = url;
        this.explicitUrl = true;
    }

    public String getUrl() {
        if (!this.explicitUrl) {
            String builtUrl = "jdbc:mysql://" + getServerName() + ":" + getPort() + "/" + getDatabaseName();
            return builtUrl;
        }
        return this.url;
    }

    public void setUser(String userID) {
        this.user = userID;
    }

    public String getUser() {
        return this.user;
    }

    protected Connection getConnection(Properties props) throws SQLException, IOException {
        String jdbcUrlToUse;
        if (!this.explicitUrl) {
            StringBuilder jdbcUrl = new StringBuilder("jdbc:mysql://");
            if (this.hostName != null) {
                jdbcUrl.append(this.hostName);
            }
            jdbcUrl.append(":");
            jdbcUrl.append(this.port);
            jdbcUrl.append("/");
            if (this.databaseName != null) {
                jdbcUrl.append(this.databaseName);
            }
            jdbcUrlToUse = jdbcUrl.toString();
        } else {
            jdbcUrlToUse = this.url;
        }
        Properties urlProps = mysqlDriver.parseURL(jdbcUrlToUse, null);
        if (urlProps == null) {
            throw SQLError.createSQLException(Messages.getString("MysqlDataSource.BadUrl", new Object[]{jdbcUrlToUse}), SQLError.SQL_STATE_CONNECTION_FAILURE, (ExceptionInterceptor) null);
        }
        urlProps.remove(NonRegisteringDriver.DBNAME_PROPERTY_KEY);
        urlProps.remove(NonRegisteringDriver.HOST_PROPERTY_KEY);
        urlProps.remove(NonRegisteringDriver.PORT_PROPERTY_KEY);
        for (String key : urlProps.keySet()) {
            props.setProperty(key, urlProps.getProperty(key));
        }
        return mysqlDriver.connect(jdbcUrlToUse, props);
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl
    public Properties exposeAsProperties(Properties props) throws SQLException {
        return exposeAsProperties(props, true);
    }
}
