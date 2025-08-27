package org.springframework.boot.autoconfigure.jms.activemq;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.activemq")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/activemq/ActiveMQProperties.class */
public class ActiveMQProperties {
    private String brokerUrl;
    private String user;
    private String password;
    private boolean inMemory = true;
    private int closeTimeout = 15000;
    private boolean nonBlockingRedelivery = false;
    private int sendTimeout = 0;
    private Pool pool = new Pool();
    private Packages packages = new Packages();

    public String getBrokerUrl() {
        return this.brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public boolean isInMemory() {
        return this.inMemory;
    }

    public void setInMemory(boolean inMemory) {
        this.inMemory = inMemory;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCloseTimeout() {
        return this.closeTimeout;
    }

    public void setCloseTimeout(int closeTimeout) {
        this.closeTimeout = closeTimeout;
    }

    public boolean isNonBlockingRedelivery() {
        return this.nonBlockingRedelivery;
    }

    public void setNonBlockingRedelivery(boolean nonBlockingRedelivery) {
        this.nonBlockingRedelivery = nonBlockingRedelivery;
    }

    public int getSendTimeout() {
        return this.sendTimeout;
    }

    public void setSendTimeout(int sendTimeout) {
        this.sendTimeout = sendTimeout;
    }

    public Pool getPool() {
        return this.pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Packages getPackages() {
        return this.packages;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/activemq/ActiveMQProperties$Pool.class */
    public static class Pool {
        private boolean enabled;
        private boolean blockIfFull = true;
        private long blockIfFullTimeout = -1;
        private boolean createConnectionOnStartup = true;
        private long expiryTimeout = 0;
        private int idleTimeout = 30000;
        private int maxConnections = 1;
        private int maximumActiveSessionPerConnection = 500;
        private boolean reconnectOnException = true;
        private long timeBetweenExpirationCheck = -1;
        private boolean useAnonymousProducers = true;

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isBlockIfFull() {
            return this.blockIfFull;
        }

        public void setBlockIfFull(boolean blockIfFull) {
            this.blockIfFull = blockIfFull;
        }

        public long getBlockIfFullTimeout() {
            return this.blockIfFullTimeout;
        }

        public void setBlockIfFullTimeout(long blockIfFullTimeout) {
            this.blockIfFullTimeout = blockIfFullTimeout;
        }

        public boolean isCreateConnectionOnStartup() {
            return this.createConnectionOnStartup;
        }

        public void setCreateConnectionOnStartup(boolean createConnectionOnStartup) {
            this.createConnectionOnStartup = createConnectionOnStartup;
        }

        public long getExpiryTimeout() {
            return this.expiryTimeout;
        }

        public void setExpiryTimeout(long expiryTimeout) {
            this.expiryTimeout = expiryTimeout;
        }

        public int getIdleTimeout() {
            return this.idleTimeout;
        }

        public void setIdleTimeout(int idleTimeout) {
            this.idleTimeout = idleTimeout;
        }

        public int getMaxConnections() {
            return this.maxConnections;
        }

        public void setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
        }

        public int getMaximumActiveSessionPerConnection() {
            return this.maximumActiveSessionPerConnection;
        }

        public void setMaximumActiveSessionPerConnection(int maximumActiveSessionPerConnection) {
            this.maximumActiveSessionPerConnection = maximumActiveSessionPerConnection;
        }

        public boolean isReconnectOnException() {
            return this.reconnectOnException;
        }

        public void setReconnectOnException(boolean reconnectOnException) {
            this.reconnectOnException = reconnectOnException;
        }

        public long getTimeBetweenExpirationCheck() {
            return this.timeBetweenExpirationCheck;
        }

        public void setTimeBetweenExpirationCheck(long timeBetweenExpirationCheck) {
            this.timeBetweenExpirationCheck = timeBetweenExpirationCheck;
        }

        public boolean isUseAnonymousProducers() {
            return this.useAnonymousProducers;
        }

        public void setUseAnonymousProducers(boolean useAnonymousProducers) {
            this.useAnonymousProducers = useAnonymousProducers;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/jms/activemq/ActiveMQProperties$Packages.class */
    public static class Packages {
        private Boolean trustAll;
        private List<String> trusted = new ArrayList();

        public Boolean getTrustAll() {
            return this.trustAll;
        }

        public void setTrustAll(Boolean trustAll) {
            this.trustAll = trustAll;
        }

        public List<String> getTrusted() {
            return this.trusted;
        }

        public void setTrusted(List<String> trusted) {
            this.trusted = trusted;
        }
    }
}
