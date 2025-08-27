package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ConnectionGroup.class */
public class ConnectionGroup {
    private String groupName;
    private long connections = 0;
    private long activeConnections = 0;
    private HashMap<Long, LoadBalancedConnectionProxy> connectionProxies = new HashMap<>();
    private Set<String> hostList = new HashSet();
    private boolean isInitialized = false;
    private long closedProxyTotalPhysicalConnections = 0;
    private long closedProxyTotalTransactions = 0;
    private int activeHosts = 0;
    private Set<String> closedHosts = new HashSet();

    /*  JADX ERROR: Failed to decode insn: 0x002D: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    public long registerConnectionProxy(com.mysql.jdbc.LoadBalancedConnectionProxy r7, java.util.List<java.lang.String> r8) {
        /*
            r6 = this;
            r0 = r6
            r1 = r0
            r11 = r1
            monitor-enter(r0)
            r0 = r6
            boolean r0 = r0.isInitialized
            if (r0 != 0) goto L26
            r0 = r6
            java.util.Set<java.lang.String> r0 = r0.hostList
            r1 = r8
            boolean r0 = r0.addAll(r1)
            r0 = r6
            r1 = 1
            r0.isInitialized = r1
            r0 = r6
            r1 = r8
            int r1 = r1.size()
            r0.activeHosts = r1
            r0 = r6
            r1 = r0
            long r1 = r1.connections
            r2 = 1
            long r1 = r1 + r2
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.connections = r1
            r9 = r-1
            r-1 = r6
            java.util.HashMap<java.lang.Long, com.mysql.jdbc.LoadBalancedConnectionProxy> r-1 = r-1.connectionProxies
            r0 = r9
            java.lang.Long r0 = java.lang.Long.valueOf(r0)
            r1 = r7
            r-1.put(r0, r1)
            r-1 = r11
            monitor-exit(r-1)
            goto L4d
            r12 = move-exception
            r0 = r11
            monitor-exit(r0)
            r0 = r12
            throw r0
            r-1 = r6
            r0 = r-1
            long r0 = r0.activeConnections
            r1 = 1
            long r0 = r0 + r1
            r-1.activeConnections = r0
            r-1 = r9
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ConnectionGroup.registerConnectionProxy(com.mysql.jdbc.LoadBalancedConnectionProxy, java.util.List):long");
    }

    ConnectionGroup(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Collection<String> getInitialHosts() {
        return this.hostList;
    }

    public int getActiveHostCount() {
        return this.activeHosts;
    }

    public Collection<String> getClosedHosts() {
        return this.closedHosts;
    }

    public long getTotalLogicalConnectionCount() {
        return this.connections;
    }

    public long getActiveLogicalConnectionCount() {
        return this.activeConnections;
    }

    public long getActivePhysicalConnectionCount() {
        long result = 0;
        Map<Long, LoadBalancedConnectionProxy> proxyMap = new HashMap<>();
        synchronized (this.connectionProxies) {
            proxyMap.putAll(this.connectionProxies);
        }
        for (LoadBalancedConnectionProxy proxy : proxyMap.values()) {
            result += proxy.getActivePhysicalConnectionCount();
        }
        return result;
    }

    public long getTotalPhysicalConnectionCount() {
        long allConnections = this.closedProxyTotalPhysicalConnections;
        Map<Long, LoadBalancedConnectionProxy> proxyMap = new HashMap<>();
        synchronized (this.connectionProxies) {
            proxyMap.putAll(this.connectionProxies);
        }
        for (LoadBalancedConnectionProxy proxy : proxyMap.values()) {
            allConnections += proxy.getTotalPhysicalConnectionCount();
        }
        return allConnections;
    }

    public long getTotalTransactionCount() {
        long transactions = this.closedProxyTotalTransactions;
        Map<Long, LoadBalancedConnectionProxy> proxyMap = new HashMap<>();
        synchronized (this.connectionProxies) {
            proxyMap.putAll(this.connectionProxies);
        }
        for (LoadBalancedConnectionProxy proxy : proxyMap.values()) {
            transactions += proxy.getTransactionCount();
        }
        return transactions;
    }

    public void closeConnectionProxy(LoadBalancedConnectionProxy proxy) {
        this.activeConnections--;
        this.connectionProxies.remove(Long.valueOf(proxy.getConnectionGroupProxyID()));
        this.closedProxyTotalPhysicalConnections += proxy.getTotalPhysicalConnectionCount();
        this.closedProxyTotalTransactions += proxy.getTransactionCount();
    }

    public void removeHost(String hostPortPair) throws SQLException {
        removeHost(hostPortPair, false);
    }

    public void removeHost(String hostPortPair, boolean removeExisting) throws SQLException {
        removeHost(hostPortPair, removeExisting, true);
    }

    public synchronized void removeHost(String hostPortPair, boolean removeExisting, boolean waitForGracefulFailover) throws SQLException {
        if (this.activeHosts == 1) {
            throw SQLError.createSQLException("Cannot remove host, only one configured host active.", null);
        }
        if (this.hostList.remove(hostPortPair)) {
            this.activeHosts--;
            if (removeExisting) {
                Map<Long, LoadBalancedConnectionProxy> proxyMap = new HashMap<>();
                synchronized (this.connectionProxies) {
                    proxyMap.putAll(this.connectionProxies);
                }
                for (LoadBalancedConnectionProxy proxy : proxyMap.values()) {
                    if (waitForGracefulFailover) {
                        proxy.removeHostWhenNotInUse(hostPortPair);
                    } else {
                        proxy.removeHost(hostPortPair);
                    }
                }
            }
            this.closedHosts.add(hostPortPair);
            return;
        }
        throw SQLError.createSQLException("Host is not configured: " + hostPortPair, null);
    }

    public void addHost(String hostPortPair) {
        addHost(hostPortPair, false);
    }

    public void addHost(String hostPortPair, boolean forExisting) {
        synchronized (this) {
            if (this.hostList.add(hostPortPair)) {
                this.activeHosts++;
            }
        }
        if (!forExisting) {
            return;
        }
        Map<Long, LoadBalancedConnectionProxy> proxyMap = new HashMap<>();
        synchronized (this.connectionProxies) {
            proxyMap.putAll(this.connectionProxies);
        }
        for (LoadBalancedConnectionProxy proxy : proxyMap.values()) {
            proxy.addHost(hostPortPair);
        }
    }
}
