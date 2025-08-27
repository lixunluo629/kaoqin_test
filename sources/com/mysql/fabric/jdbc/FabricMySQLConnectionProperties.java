package com.mysql.fabric.jdbc;

import com.mysql.jdbc.ConnectionProperties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/jdbc/FabricMySQLConnectionProperties.class */
public interface FabricMySQLConnectionProperties extends ConnectionProperties {
    void setFabricShardKey(String str);

    String getFabricShardKey();

    void setFabricShardTable(String str);

    String getFabricShardTable();

    void setFabricServerGroup(String str);

    String getFabricServerGroup();

    void setFabricProtocol(String str);

    String getFabricProtocol();

    void setFabricUsername(String str);

    String getFabricUsername();

    void setFabricPassword(String str);

    String getFabricPassword();

    void setFabricReportErrors(boolean z);

    boolean getFabricReportErrors();
}
