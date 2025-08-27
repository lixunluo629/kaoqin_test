package com.mysql.fabric;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/Server.class */
public class Server implements Comparable<Server> {
    private String groupName;
    private String uuid;
    private String hostname;
    private int port;
    private ServerMode mode;
    private ServerRole role;
    private double weight;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Server.class.desiredAssertionStatus();
    }

    public Server(String groupName, String uuid, String hostname, int port, ServerMode mode, ServerRole role, double weight) {
        this.groupName = groupName;
        this.uuid = uuid;
        this.hostname = hostname;
        this.port = port;
        this.mode = mode;
        this.role = role;
        this.weight = weight;
        if (!$assertionsDisabled && (uuid == null || "".equals(uuid))) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (hostname == null || "".equals(hostname))) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && port <= 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && mode == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && role == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && weight <= 0.0d) {
            throw new AssertionError();
        }
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getHostname() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public ServerMode getMode() {
        return this.mode;
    }

    public ServerRole getRole() {
        return this.role;
    }

    public double getWeight() {
        return this.weight;
    }

    public String getHostPortString() {
        return this.hostname + ":" + this.port;
    }

    public boolean isMaster() {
        return this.role == ServerRole.PRIMARY;
    }

    public boolean isSlave() {
        return this.role == ServerRole.SECONDARY || this.role == ServerRole.SPARE;
    }

    public String toString() {
        return String.format("Server[%s, %s:%d, %s, %s, weight=%s]", this.uuid, this.hostname, Integer.valueOf(this.port), this.mode, this.role, Double.valueOf(this.weight));
    }

    public boolean equals(Object o) {
        if (!(o instanceof Server)) {
            return false;
        }
        Server s = (Server) o;
        return s.getUuid().equals(getUuid());
    }

    public int hashCode() {
        return getUuid().hashCode();
    }

    @Override // java.lang.Comparable
    public int compareTo(Server other) {
        return getUuid().compareTo(other.getUuid());
    }
}
