package com.mysql.jdbc.jmx;

import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.ReplicationConnectionGroup;
import com.mysql.jdbc.ReplicationConnectionGroupManager;
import com.mysql.jdbc.SQLError;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jmx/ReplicationGroupManager.class */
public class ReplicationGroupManager implements ReplicationGroupManagerMBean {
    private boolean isJmxRegistered = false;

    public synchronized void registerJmx() throws SQLException {
        if (this.isJmxRegistered) {
            return;
        }
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName name = new ObjectName("com.mysql.jdbc.jmx:type=ReplicationGroupManager");
            mbs.registerMBean(this, name);
            this.isJmxRegistered = true;
        } catch (Exception e) {
            throw SQLError.createSQLException("Unable to register replication host management bean with JMX", (String) null, e, (ExceptionInterceptor) null);
        }
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public void addSlaveHost(String groupFilter, String host) throws SQLException {
        ReplicationConnectionGroupManager.addSlaveHost(groupFilter, host);
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public void removeSlaveHost(String groupFilter, String host) throws SQLException {
        ReplicationConnectionGroupManager.removeSlaveHost(groupFilter, host);
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public void promoteSlaveToMaster(String groupFilter, String host) throws SQLException {
        ReplicationConnectionGroupManager.promoteSlaveToMaster(groupFilter, host);
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public void removeMasterHost(String groupFilter, String host) throws SQLException {
        ReplicationConnectionGroupManager.removeMasterHost(groupFilter, host);
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public String getMasterHostsList(String group) {
        StringBuilder sb = new StringBuilder("");
        boolean found = false;
        for (String host : ReplicationConnectionGroupManager.getMasterHosts(group)) {
            if (found) {
                sb.append(",");
            }
            found = true;
            sb.append(host);
        }
        return sb.toString();
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public String getSlaveHostsList(String group) {
        StringBuilder sb = new StringBuilder("");
        boolean found = false;
        for (String host : ReplicationConnectionGroupManager.getSlaveHosts(group)) {
            if (found) {
                sb.append(",");
            }
            found = true;
            sb.append(host);
        }
        return sb.toString();
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public String getRegisteredConnectionGroups() {
        StringBuilder sb = new StringBuilder("");
        boolean found = false;
        for (ReplicationConnectionGroup group : ReplicationConnectionGroupManager.getGroupsMatching(null)) {
            if (found) {
                sb.append(",");
            }
            found = true;
            sb.append(group.getGroupName());
        }
        return sb.toString();
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public int getActiveMasterHostCount(String group) {
        return ReplicationConnectionGroupManager.getMasterHosts(group).size();
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public int getActiveSlaveHostCount(String group) {
        return ReplicationConnectionGroupManager.getSlaveHosts(group).size();
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public int getSlavePromotionCount(String group) {
        return ReplicationConnectionGroupManager.getNumberOfMasterPromotion(group);
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public long getTotalLogicalConnectionCount(String group) {
        return ReplicationConnectionGroupManager.getTotalConnectionCount(group);
    }

    @Override // com.mysql.jdbc.jmx.ReplicationGroupManagerMBean
    public long getActiveLogicalConnectionCount(String group) {
        return ReplicationConnectionGroupManager.getActiveConnectionCount(group);
    }
}
