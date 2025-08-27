package com.mysql.jdbc.jmx;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jmx/ReplicationGroupManagerMBean.class */
public interface ReplicationGroupManagerMBean {
    void addSlaveHost(String str, String str2) throws SQLException;

    void removeSlaveHost(String str, String str2) throws SQLException;

    void promoteSlaveToMaster(String str, String str2) throws SQLException;

    void removeMasterHost(String str, String str2) throws SQLException;

    String getMasterHostsList(String str);

    String getSlaveHostsList(String str);

    String getRegisteredConnectionGroups();

    int getActiveMasterHostCount(String str);

    int getActiveSlaveHostCount(String str);

    int getSlavePromotionCount(String str);

    long getTotalLogicalConnectionCount(String str);

    long getActiveLogicalConnectionCount(String str);
}
