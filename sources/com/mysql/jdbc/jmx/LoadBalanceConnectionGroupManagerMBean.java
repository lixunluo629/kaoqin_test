package com.mysql.jdbc.jmx;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jmx/LoadBalanceConnectionGroupManagerMBean.class */
public interface LoadBalanceConnectionGroupManagerMBean {
    int getActiveHostCount(String str);

    int getTotalHostCount(String str);

    long getTotalLogicalConnectionCount(String str);

    long getActiveLogicalConnectionCount(String str);

    long getActivePhysicalConnectionCount(String str);

    long getTotalPhysicalConnectionCount(String str);

    long getTotalTransactionCount(String str);

    void removeHost(String str, String str2) throws SQLException;

    void stopNewConnectionsToHost(String str, String str2) throws SQLException;

    void addHost(String str, String str2, boolean z);

    String getActiveHostsList(String str);

    String getRegisteredConnectionGroups();
}
