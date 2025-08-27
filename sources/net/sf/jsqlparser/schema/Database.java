package net.sf.jsqlparser.schema;

/* loaded from: jsqlparser-0.9.1.jar:net/sf/jsqlparser/schema/Database.class */
public final class Database implements MultiPartName {
    private Server server;
    private String databaseName;

    public Database(String databaseName) {
        setDatabaseName(databaseName);
    }

    public Database(Server server, String databaseName) {
        setServer(server);
        setDatabaseName(databaseName);
    }

    public Server getServer() {
        return this.server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override // net.sf.jsqlparser.schema.MultiPartName
    public String getFullyQualifiedName() {
        String fqn = "";
        if (this.server != null) {
            fqn = fqn + this.server.getFullyQualifiedName();
        }
        if (!fqn.isEmpty()) {
            fqn = fqn + ".";
        }
        if (this.databaseName != null) {
            fqn = fqn + this.databaseName;
        }
        return fqn;
    }

    public String toString() {
        return getFullyQualifiedName();
    }
}
