package com.mysql.fabric.jdbc;

import com.mysql.fabric.FabricCommunicationException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ConnectionImpl;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.NonRegisteringDriver;
import com.mysql.jdbc.SQLError;
import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/jdbc/ErrorReportingExceptionInterceptor.class */
public class ErrorReportingExceptionInterceptor implements ExceptionInterceptor {
    private String hostname;
    private String port;
    private String fabricHaGroup;

    @Override // com.mysql.jdbc.ExceptionInterceptor
    public SQLException interceptException(SQLException sqlEx, Connection conn) {
        MySQLConnection mysqlConn = (MySQLConnection) conn;
        if (ConnectionImpl.class.isAssignableFrom(mysqlConn.getMultiHostSafeProxy().getClass())) {
            return null;
        }
        FabricMySQLConnectionProxy fabricProxy = (FabricMySQLConnectionProxy) mysqlConn.getMultiHostSafeProxy();
        try {
            return fabricProxy.interceptException(sqlEx, conn, this.fabricHaGroup, this.hostname, this.port);
        } catch (FabricCommunicationException ex) {
            return SQLError.createSQLException("Failed to report error to Fabric.", SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE, ex, (ExceptionInterceptor) null);
        }
    }

    @Override // com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
        this.hostname = props.getProperty(NonRegisteringDriver.HOST_PROPERTY_KEY);
        this.port = props.getProperty(NonRegisteringDriver.PORT_PROPERTY_KEY);
        String connectionAttributes = props.getProperty("connectionAttributes");
        this.fabricHaGroup = connectionAttributes.replaceAll("^.*\\bfabricHaGroup:(.+)\\b.*$", "$1");
    }

    @Override // com.mysql.jdbc.Extension
    public void destroy() {
    }
}
