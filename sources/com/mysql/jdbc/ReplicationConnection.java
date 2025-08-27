package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ReplicationConnection.class */
public interface ReplicationConnection extends MySQLConnection {
    long getConnectionGroupId();

    Connection getCurrentConnection();

    Connection getMasterConnection();

    void promoteSlaveToMaster(String str) throws SQLException;

    void removeMasterHost(String str) throws SQLException;

    void removeMasterHost(String str, boolean z) throws SQLException;

    boolean isHostMaster(String str);

    Connection getSlavesConnection();

    void addSlaveHost(String str) throws SQLException;

    void removeSlave(String str) throws SQLException;

    void removeSlave(String str, boolean z) throws SQLException;

    boolean isHostSlave(String str);
}
