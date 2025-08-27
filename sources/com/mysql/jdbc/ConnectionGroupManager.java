package com.mysql.jdbc;

import com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ConnectionGroupManager.class */
public class ConnectionGroupManager {
    private static HashMap<String, ConnectionGroup> GROUP_MAP = new HashMap<>();
    private static LoadBalanceConnectionGroupManager mbean = new LoadBalanceConnectionGroupManager();
    private static boolean hasRegisteredJmx = false;

    public static synchronized ConnectionGroup getConnectionGroupInstance(String groupName) {
        if (GROUP_MAP.containsKey(groupName)) {
            return GROUP_MAP.get(groupName);
        }
        ConnectionGroup group = new ConnectionGroup(groupName);
        GROUP_MAP.put(groupName, group);
        return group;
    }

    public static void registerJmx() throws SQLException {
        if (hasRegisteredJmx) {
            return;
        }
        mbean.registerJmx();
        hasRegisteredJmx = true;
    }

    public static ConnectionGroup getConnectionGroup(String groupName) {
        return GROUP_MAP.get(groupName);
    }

    private static Collection<ConnectionGroup> getGroupsMatching(String group) {
        if (group == null || group.equals("")) {
            Set<ConnectionGroup> s = new HashSet<>();
            s.addAll(GROUP_MAP.values());
            return s;
        }
        Set<ConnectionGroup> s2 = new HashSet<>();
        ConnectionGroup o = GROUP_MAP.get(group);
        if (o != null) {
            s2.add(o);
        }
        return s2;
    }

    public static void addHost(String group, String hostPortPair, boolean forExisting) {
        Collection<ConnectionGroup> s = getGroupsMatching(group);
        for (ConnectionGroup cg : s) {
            cg.addHost(hostPortPair, forExisting);
        }
    }

    public static int getActiveHostCount(String group) {
        Set<String> active = new HashSet<>();
        Collection<ConnectionGroup> s = getGroupsMatching(group);
        for (ConnectionGroup cg : s) {
            active.addAll(cg.getInitialHosts());
        }
        return active.size();
    }

    public static long getActiveLogicalConnectionCount(String group) {
        int count = 0;
        Collection<ConnectionGroup> s = getGroupsMatching(group);
        for (ConnectionGroup cg : s) {
            count = (int) (count + cg.getActiveLogicalConnectionCount());
        }
        return count;
    }

    public static long getActivePhysicalConnectionCount(String group) {
        int count = 0;
        Collection<ConnectionGroup> s = getGroupsMatching(group);
        for (ConnectionGroup cg : s) {
            count = (int) (count + cg.getActivePhysicalConnectionCount());
        }
        return count;
    }

    public static int getTotalHostCount(String group) {
        Collection<ConnectionGroup> s = getGroupsMatching(group);
        Set<String> hosts = new HashSet<>();
        for (ConnectionGroup cg : s) {
            hosts.addAll(cg.getInitialHosts());
            hosts.addAll(cg.getClosedHosts());
        }
        return hosts.size();
    }

    public static long getTotalLogicalConnectionCount(String group) {
        long count = 0;
        Collection<ConnectionGroup> s = getGroupsMatching(group);
        for (ConnectionGroup cg : s) {
            count += cg.getTotalLogicalConnectionCount();
        }
        return count;
    }

    public static long getTotalPhysicalConnectionCount(String group) {
        long count = 0;
        Collection<ConnectionGroup> s = getGroupsMatching(group);
        for (ConnectionGroup cg : s) {
            count += cg.getTotalPhysicalConnectionCount();
        }
        return count;
    }

    public static long getTotalTransactionCount(String group) {
        long count = 0;
        Collection<ConnectionGroup> s = getGroupsMatching(group);
        for (ConnectionGroup cg : s) {
            count += cg.getTotalTransactionCount();
        }
        return count;
    }

    public static void removeHost(String group, String hostPortPair) throws SQLException {
        removeHost(group, hostPortPair, false);
    }

    public static void removeHost(String group, String host, boolean removeExisting) throws SQLException {
        Collection<ConnectionGroup> s = getGroupsMatching(group);
        for (ConnectionGroup cg : s) {
            cg.removeHost(host, removeExisting);
        }
    }

    public static String getActiveHostLists(String group) {
        int o;
        Collection<ConnectionGroup> s = getGroupsMatching(group);
        Map<String, Integer> hosts = new HashMap<>();
        for (ConnectionGroup cg : s) {
            Collection<String> l = cg.getInitialHosts();
            for (String host : l) {
                Integer o2 = hosts.get(host);
                if (o2 == null) {
                    o = 1;
                } else {
                    o = Integer.valueOf(o2.intValue() + 1);
                }
                hosts.put(host, o);
            }
        }
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (String host2 : hosts.keySet()) {
            sb.append(sep);
            sb.append(host2);
            sb.append('(');
            sb.append(hosts.get(host2));
            sb.append(')');
            sep = ",";
        }
        return sb.toString();
    }

    public static String getRegisteredConnectionGroups() {
        Collection<ConnectionGroup> s = getGroupsMatching(null);
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (ConnectionGroup cg : s) {
            String group = cg.getGroupName();
            sb.append(sep);
            sb.append(group);
            sep = ",";
        }
        return sb.toString();
    }
}
