package com.mysql.fabric.jdbc;

import com.mysql.jdbc.NonRegisteringDriver;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/jdbc/FabricMySQLDataSource.class */
public class FabricMySQLDataSource extends MysqlDataSource implements FabricMySQLConnectionProperties {
    private static final long serialVersionUID = 1;
    private static final Driver driver;
    private String fabricShardKey;
    private String fabricShardTable;
    private String fabricServerGroup;
    private String fabricUsername;
    private String fabricPassword;
    private String fabricProtocol = "http";
    private boolean fabricReportErrors = false;

    static {
        try {
            driver = new FabricMySQLDriver();
        } catch (Exception ex) {
            throw new RuntimeException("Can create driver", ex);
        }
    }

    @Override // com.mysql.jdbc.jdbc2.optional.MysqlDataSource
    protected Connection getConnection(Properties props) throws SQLException {
        String jdbcUrlToUse;
        if (!this.explicitUrl) {
            StringBuilder jdbcUrl = new StringBuilder(FabricMySQLDriver.FABRIC_URL_PREFIX);
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
        Properties urlProps = ((FabricMySQLDriver) driver).parseFabricURL(jdbcUrlToUse, null);
        urlProps.remove(NonRegisteringDriver.DBNAME_PROPERTY_KEY);
        urlProps.remove(NonRegisteringDriver.HOST_PROPERTY_KEY);
        urlProps.remove(NonRegisteringDriver.PORT_PROPERTY_KEY);
        for (String key : urlProps.keySet()) {
            props.setProperty(key, urlProps.getProperty(key));
        }
        if (this.fabricShardKey != null) {
            props.setProperty(FabricMySQLDriver.FABRIC_SHARD_KEY_PROPERTY_KEY, this.fabricShardKey);
        }
        if (this.fabricShardTable != null) {
            props.setProperty(FabricMySQLDriver.FABRIC_SHARD_TABLE_PROPERTY_KEY, this.fabricShardTable);
        }
        if (this.fabricServerGroup != null) {
            props.setProperty(FabricMySQLDriver.FABRIC_SERVER_GROUP_PROPERTY_KEY, this.fabricServerGroup);
        }
        props.setProperty(FabricMySQLDriver.FABRIC_PROTOCOL_PROPERTY_KEY, this.fabricProtocol);
        if (this.fabricUsername != null) {
            props.setProperty(FabricMySQLDriver.FABRIC_USERNAME_PROPERTY_KEY, this.fabricUsername);
        }
        if (this.fabricPassword != null) {
            props.setProperty(FabricMySQLDriver.FABRIC_PASSWORD_PROPERTY_KEY, this.fabricPassword);
        }
        props.setProperty(FabricMySQLDriver.FABRIC_REPORT_ERRORS_PROPERTY_KEY, Boolean.toString(this.fabricReportErrors));
        return driver.connect(jdbcUrlToUse, props);
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricShardKey(String value) {
        this.fabricShardKey = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricShardKey() {
        return this.fabricShardKey;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricShardTable(String value) {
        this.fabricShardTable = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricShardTable() {
        return this.fabricShardTable;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricServerGroup(String value) {
        this.fabricServerGroup = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricServerGroup() {
        return this.fabricServerGroup;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricProtocol(String value) {
        this.fabricProtocol = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricProtocol() {
        return this.fabricProtocol;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricUsername(String value) {
        this.fabricUsername = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricUsername() {
        return this.fabricUsername;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricPassword(String value) {
        this.fabricPassword = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricPassword() {
        return this.fabricPassword;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricReportErrors(boolean value) {
        this.fabricReportErrors = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public boolean getFabricReportErrors() {
        return this.fabricReportErrors;
    }
}
