package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ReplicationConnectionGroup.class */
public class ReplicationConnectionGroup {
    private String groupName;
    private long connections = 0;
    private long slavesAdded = 0;
    private long slavesRemoved = 0;
    private long slavesPromoted = 0;
    private long activeConnections = 0;
    private HashMap<Long, ReplicationConnection> replicationConnections = new HashMap<>();
    private Set<String> slaveHostList = new CopyOnWriteArraySet();
    private boolean isInitialized = false;
    private Set<String> masterHostList = new CopyOnWriteArraySet();

    /*  JADX ERROR: Failed to decode insn: 0x0036: MOVE_MULTI
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
    public long registerReplicationConnection(com.mysql.jdbc.ReplicationConnection r7, java.util.List<java.lang.String> r8, java.util.List<java.lang.String> r9) {
        /*
            r6 = this;
            r0 = r6
            r1 = r0
            r12 = r1
            monitor-enter(r0)
            r0 = r6
            boolean r0 = r0.isInitialized
            if (r0 != 0) goto L2f
            r0 = r8
            if (r0 == 0) goto L1b
            r0 = r6
            java.util.Set<java.lang.String> r0 = r0.masterHostList
            r1 = r8
            boolean r0 = r0.addAll(r1)
            r0 = r9
            if (r0 == 0) goto L2a
            r0 = r6
            java.util.Set<java.lang.String> r0 = r0.slaveHostList
            r1 = r9
            boolean r0 = r0.addAll(r1)
            r0 = r6
            r1 = 1
            r0.isInitialized = r1
            r0 = r6
            r1 = r0
            long r1 = r1.connections
            r2 = 1
            long r1 = r1 + r2
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.connections = r1
            r10 = r-1
            r-1 = r6
            java.util.HashMap<java.lang.Long, com.mysql.jdbc.ReplicationConnection> r-1 = r-1.replicationConnections
            r0 = r10
            java.lang.Long r0 = java.lang.Long.valueOf(r0)
            r1 = r7
            r-1.put(r0, r1)
            r-1 = r12
            monitor-exit(r-1)
            goto L58
            r13 = move-exception
            r0 = r12
            monitor-exit(r0)
            r0 = r13
            throw r0
            r-1 = r6
            r0 = r-1
            long r0 = r0.activeConnections
            r1 = 1
            long r0 = r0 + r1
            r-1.activeConnections = r0
            r-1 = r10
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ReplicationConnectionGroup.registerReplicationConnection(com.mysql.jdbc.ReplicationConnection, java.util.List, java.util.List):long");
    }

    ReplicationConnectionGroup(String groupName) {
        this.groupName = groupName;
    }

    public long getConnectionCount() {
        return this.connections;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Collection<String> getMasterHosts() {
        return this.masterHostList;
    }

    public Collection<String> getSlaveHosts() {
        return this.slaveHostList;
    }

    public void addSlaveHost(String hostPortPair) throws SQLException {
        if (this.slaveHostList.add(hostPortPair)) {
            this.slavesAdded++;
            for (ReplicationConnection c : this.replicationConnections.values()) {
                c.addSlaveHost(hostPortPair);
            }
        }
    }

    public void handleCloseConnection(ReplicationConnection conn) {
        this.replicationConnections.remove(Long.valueOf(conn.getConnectionGroupId()));
        this.activeConnections--;
    }

    public void removeSlaveHost(String hostPortPair, boolean closeGently) throws SQLException {
        if (this.slaveHostList.remove(hostPortPair)) {
            this.slavesRemoved++;
            for (ReplicationConnection c : this.replicationConnections.values()) {
                c.removeSlave(hostPortPair, closeGently);
            }
        }
    }

    public void promoteSlaveToMaster(String hostPortPair) throws SQLException {
        if (this.slaveHostList.remove(hostPortPair) | this.masterHostList.add(hostPortPair)) {
            this.slavesPromoted++;
            for (ReplicationConnection c : this.replicationConnections.values()) {
                c.promoteSlaveToMaster(hostPortPair);
            }
        }
    }

    public void removeMasterHost(String hostPortPair) throws SQLException {
        removeMasterHost(hostPortPair, true);
    }

    public void removeMasterHost(String hostPortPair, boolean closeGently) throws SQLException {
        if (this.masterHostList.remove(hostPortPair)) {
            for (ReplicationConnection c : this.replicationConnections.values()) {
                c.removeMasterHost(hostPortPair, closeGently);
            }
        }
    }

    public int getConnectionCountWithHostAsSlave(String hostPortPair) {
        int matched = 0;
        for (ReplicationConnection c : this.replicationConnections.values()) {
            if (c.isHostSlave(hostPortPair)) {
                matched++;
            }
        }
        return matched;
    }

    public int getConnectionCountWithHostAsMaster(String hostPortPair) {
        int matched = 0;
        for (ReplicationConnection c : this.replicationConnections.values()) {
            if (c.isHostMaster(hostPortPair)) {
                matched++;
            }
        }
        return matched;
    }

    public long getNumberOfSlavesAdded() {
        return this.slavesAdded;
    }

    public long getNumberOfSlavesRemoved() {
        return this.slavesRemoved;
    }

    public long getNumberOfSlavePromotions() {
        return this.slavesPromoted;
    }

    public long getTotalConnectionCount() {
        return this.connections;
    }

    public long getActiveConnectionCount() {
        return this.activeConnections;
    }

    public String toString() {
        return "ReplicationConnectionGroup[groupName=" + this.groupName + ",masterHostList=" + this.masterHostList + ",slaveHostList=" + this.slaveHostList + "]";
    }
}
