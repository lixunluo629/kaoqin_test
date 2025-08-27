package com.mysql.fabric.jdbc;

import com.mysql.fabric.ServerGroup;
import com.mysql.jdbc.MySQLConnection;
import java.sql.SQLException;
import java.util.Set;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/jdbc/FabricMySQLConnection.class */
public interface FabricMySQLConnection extends MySQLConnection {
    void clearServerSelectionCriteria() throws SQLException;

    void setShardKey(String str) throws SQLException;

    String getShardKey();

    void setShardTable(String str) throws SQLException;

    String getShardTable();

    void setServerGroupName(String str) throws SQLException;

    String getServerGroupName();

    ServerGroup getCurrentServerGroup();

    void clearQueryTables() throws SQLException;

    void addQueryTable(String str) throws SQLException;

    Set<String> getQueryTables();
}
