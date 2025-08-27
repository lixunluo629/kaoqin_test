package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/NonRegisteringReplicationDriver.class */
public class NonRegisteringReplicationDriver extends NonRegisteringDriver {
    @Override // com.mysql.jdbc.NonRegisteringDriver, java.sql.Driver
    public java.sql.Connection connect(String url, Properties info) throws SQLException {
        return connectReplicationConnection(url, info);
    }
}
