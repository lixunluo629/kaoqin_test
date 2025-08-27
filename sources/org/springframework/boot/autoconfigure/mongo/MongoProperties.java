package org.springframework.boot.autoconfigure.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

@ConfigurationProperties(prefix = "spring.data.mongodb")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/MongoProperties.class */
public class MongoProperties {
    public static final int DEFAULT_PORT = 27017;
    public static final String DEFAULT_URI = "mongodb://localhost/test";
    private String host;
    private Integer port = null;
    private String uri;
    private String database;
    private String authenticationDatabase;
    private String gridFsDatabase;
    private String username;
    private char[] password;
    private Class<?> fieldNamingStrategy;

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getAuthenticationDatabase() {
        return this.authenticationDatabase;
    }

    public void setAuthenticationDatabase(String authenticationDatabase) {
        this.authenticationDatabase = authenticationDatabase;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return this.password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public Class<?> getFieldNamingStrategy() {
        return this.fieldNamingStrategy;
    }

    public void setFieldNamingStrategy(Class<?> fieldNamingStrategy) {
        this.fieldNamingStrategy = fieldNamingStrategy;
    }

    public void clearPassword() {
        if (this.password == null) {
            return;
        }
        for (int i = 0; i < this.password.length; i++) {
            this.password[i] = 0;
        }
    }

    public String getUri() {
        return this.uri;
    }

    public String determineUri() {
        return this.uri != null ? this.uri : DEFAULT_URI;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getGridFsDatabase() {
        return this.gridFsDatabase;
    }

    public void setGridFsDatabase(String gridFsDatabase) {
        this.gridFsDatabase = gridFsDatabase;
    }

    public String getMongoClientDatabase() {
        if (this.database != null) {
            return this.database;
        }
        return new MongoClientURI(determineUri()).getDatabase();
    }

    public MongoClient createMongoClient(MongoClientOptions options, Environment environment) throws UnknownHostException {
        try {
            Integer embeddedPort = getEmbeddedPort(environment);
            if (embeddedPort != null) {
                MongoClient mongoClientCreateEmbeddedMongoClient = createEmbeddedMongoClient(options, embeddedPort.intValue());
                clearPassword();
                return mongoClientCreateEmbeddedMongoClient;
            }
            MongoClient mongoClientCreateNetworkMongoClient = createNetworkMongoClient(options);
            clearPassword();
            return mongoClientCreateNetworkMongoClient;
        } catch (Throwable th) {
            clearPassword();
            throw th;
        }
    }

    private Integer getEmbeddedPort(Environment environment) {
        String localPort;
        if (environment != null && (localPort = environment.getProperty("local.mongo.port")) != null) {
            return Integer.valueOf(localPort);
        }
        return null;
    }

    private MongoClient createEmbeddedMongoClient(MongoClientOptions options, int port) {
        if (options == null) {
            options = MongoClientOptions.builder().build();
        }
        String host = this.host != null ? this.host : "localhost";
        return new MongoClient(Collections.singletonList(new ServerAddress(host, port)), Collections.emptyList(), options);
    }

    private MongoClient createNetworkMongoClient(MongoClientOptions options) {
        if (hasCustomAddress() || hasCustomCredentials()) {
            if (this.uri != null) {
                throw new IllegalStateException("Invalid mongo configuration, either uri or host/port/credentials must be specified");
            }
            if (options == null) {
                options = MongoClientOptions.builder().build();
            }
            List<MongoCredential> credentials = new ArrayList<>();
            if (hasCustomCredentials()) {
                String database = this.authenticationDatabase != null ? this.authenticationDatabase : getMongoClientDatabase();
                credentials.add(MongoCredential.createCredential(this.username, database, this.password));
            }
            String host = this.host != null ? this.host : "localhost";
            int port = this.port != null ? this.port.intValue() : DEFAULT_PORT;
            return new MongoClient(Collections.singletonList(new ServerAddress(host, port)), credentials, options);
        }
        return new MongoClient(new MongoClientURI(determineUri(), builder(options)));
    }

    private boolean hasCustomAddress() {
        return (this.host == null && this.port == null) ? false : true;
    }

    private boolean hasCustomCredentials() {
        return (this.username == null || this.password == null) ? false : true;
    }

    private MongoClientOptions.Builder builder(MongoClientOptions options) {
        if (options != null) {
            return MongoClientOptions.builder(options);
        }
        return MongoClientOptions.builder();
    }
}
