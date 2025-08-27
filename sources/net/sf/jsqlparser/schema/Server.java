package net.sf.jsqlparser.schema;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/schema/Server.class */
public final class Server implements MultiPartName {
    public static final Pattern SERVER_PATTERN = Pattern.compile("\\[([^\\]]+?)(?:\\\\([^\\]]+))?\\]");
    private String serverName;
    private String instanceName;

    public Server(String serverAndInstanceName) {
        if (serverAndInstanceName != null) {
            Matcher matcher = SERVER_PATTERN.matcher(serverAndInstanceName);
            if (!matcher.find()) {
                throw new IllegalArgumentException(String.format("%s is not a valid database reference", serverAndInstanceName));
            }
            setServerName(matcher.group(1));
            setInstanceName(matcher.group(2));
        }
    }

    public Server(String serverName, String instanceName) {
        setServerName(serverName);
        setInstanceName(instanceName);
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getInstanceName() {
        return this.instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Override // net.sf.jsqlparser.schema.MultiPartName
    public String getFullyQualifiedName() {
        if (this.serverName != null && !this.serverName.isEmpty() && this.instanceName != null && !this.instanceName.isEmpty()) {
            return String.format("[%s\\%s]", this.serverName, this.instanceName);
        }
        if (this.serverName != null && !this.serverName.isEmpty()) {
            return String.format("[%s]", this.serverName);
        }
        return "";
    }

    public String toString() {
        return getFullyQualifiedName();
    }
}
