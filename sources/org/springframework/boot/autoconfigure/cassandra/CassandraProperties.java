package org.springframework.boot.autoconfigure.cassandra;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.cassandra")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CassandraProperties.class */
public class CassandraProperties {
    private String keyspaceName;
    private String clusterName;
    private String username;
    private String password;
    private Class<? extends LoadBalancingPolicy> loadBalancingPolicy;
    private ConsistencyLevel consistencyLevel;
    private ConsistencyLevel serialConsistencyLevel;
    private Class<? extends ReconnectionPolicy> reconnectionPolicy;
    private Class<? extends RetryPolicy> retryPolicy;
    private String contactPoints = "localhost";
    private int port = 9042;
    private ProtocolOptions.Compression compression = ProtocolOptions.Compression.NONE;
    private int fetchSize = 5000;
    private int connectTimeoutMillis = 5000;
    private int readTimeoutMillis = 12000;
    private String schemaAction = "none";
    private boolean ssl = false;

    public String getKeyspaceName() {
        return this.keyspaceName;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    public String getClusterName() {
        return this.clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getContactPoints() {
        return this.contactPoints;
    }

    public void setContactPoints(String contactPoints) {
        this.contactPoints = contactPoints;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProtocolOptions.Compression getCompression() {
        return this.compression;
    }

    public void setCompression(ProtocolOptions.Compression compression) {
        this.compression = compression;
    }

    public Class<? extends LoadBalancingPolicy> getLoadBalancingPolicy() {
        return this.loadBalancingPolicy;
    }

    public void setLoadBalancingPolicy(Class<? extends LoadBalancingPolicy> loadBalancingPolicy) {
        this.loadBalancingPolicy = loadBalancingPolicy;
    }

    public ConsistencyLevel getConsistencyLevel() {
        return this.consistencyLevel;
    }

    public void setConsistencyLevel(ConsistencyLevel consistency) {
        this.consistencyLevel = consistency;
    }

    public ConsistencyLevel getSerialConsistencyLevel() {
        return this.serialConsistencyLevel;
    }

    public void setSerialConsistencyLevel(ConsistencyLevel serialConsistency) {
        this.serialConsistencyLevel = serialConsistency;
    }

    public int getFetchSize() {
        return this.fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public Class<? extends ReconnectionPolicy> getReconnectionPolicy() {
        return this.reconnectionPolicy;
    }

    public void setReconnectionPolicy(Class<? extends ReconnectionPolicy> reconnectionPolicy) {
        this.reconnectionPolicy = reconnectionPolicy;
    }

    public Class<? extends RetryPolicy> getRetryPolicy() {
        return this.retryPolicy;
    }

    public void setRetryPolicy(Class<? extends RetryPolicy> retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public int getConnectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return this.readTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public boolean isSsl() {
        return this.ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getSchemaAction() {
        return this.schemaAction;
    }

    public void setSchemaAction(String schemaAction) {
        this.schemaAction = schemaAction;
    }
}
