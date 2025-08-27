package com.mysql.jdbc.jmx;

import com.mysql.jdbc.ConnectionGroupManager;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.SQLError;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jmx/LoadBalanceConnectionGroupManager.class */
public class LoadBalanceConnectionGroupManager implements LoadBalanceConnectionGroupManagerMBean {
    private boolean isJmxRegistered = false;

    public synchronized void registerJmx() throws SQLException {
        if (this.isJmxRegistered) {
            return;
        }
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName name = new ObjectName("com.mysql.jdbc.jmx:type=LoadBalanceConnectionGroupManager");
            mbs.registerMBean(this, name);
            this.isJmxRegistered = true;
        } catch (Exception e) {
            throw SQLError.createSQLException("Unable to register load-balance management bean with JMX", (String) null, e, (ExceptionInterceptor) null);
        }
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public void addHost(String group, String host, boolean forExisting) {
        try {
            ConnectionGroupManager.addHost(group, host, forExisting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public int getActiveHostCount(String group) {
        return ConnectionGroupManager.getActiveHostCount(group);
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public long getActiveLogicalConnectionCount(String group) {
        return ConnectionGroupManager.getActiveLogicalConnectionCount(group);
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public long getActivePhysicalConnectionCount(String group) {
        return ConnectionGroupManager.getActivePhysicalConnectionCount(group);
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public int getTotalHostCount(String group) {
        return ConnectionGroupManager.getTotalHostCount(group);
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public long getTotalLogicalConnectionCount(String group) {
        return ConnectionGroupManager.getTotalLogicalConnectionCount(group);
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public long getTotalPhysicalConnectionCount(String group) {
        return ConnectionGroupManager.getTotalPhysicalConnectionCount(group);
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public long getTotalTransactionCount(String group) {
        return ConnectionGroupManager.getTotalTransactionCount(group);
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public void removeHost(String group, String host) throws SQLException {
        ConnectionGroupManager.removeHost(group, host);
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public String getActiveHostsList(String group) {
        return ConnectionGroupManager.getActiveHostLists(group);
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public String getRegisteredConnectionGroups() {
        return ConnectionGroupManager.getRegisteredConnectionGroups();
    }

    @Override // com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManagerMBean
    public void stopNewConnectionsToHost(String group, String host) throws SQLException {
        ConnectionGroupManager.removeHost(group, host);
    }
}
