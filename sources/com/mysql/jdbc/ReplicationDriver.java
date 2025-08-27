package com.mysql.jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ReplicationDriver.class */
public class ReplicationDriver extends NonRegisteringReplicationDriver implements java.sql.Driver {
    static {
        try {
            DriverManager.registerDriver(new NonRegisteringReplicationDriver());
        } catch (SQLException e) {
            throw new RuntimeException("Can't register driver!");
        }
    }
}
