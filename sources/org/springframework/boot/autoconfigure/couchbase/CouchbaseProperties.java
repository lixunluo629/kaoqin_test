package org.springframework.boot.autoconfigure.couchbase;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "spring.couchbase")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseProperties.class */
public class CouchbaseProperties {
    private List<String> bootstrapHosts;
    private final Bucket bucket = new Bucket();
    private final Env env = new Env();

    public List<String> getBootstrapHosts() {
        return this.bootstrapHosts;
    }

    public void setBootstrapHosts(List<String> bootstrapHosts) {
        this.bootstrapHosts = bootstrapHosts;
    }

    public Bucket getBucket() {
        return this.bucket;
    }

    public Env getEnv() {
        return this.env;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseProperties$Bucket.class */
    public static class Bucket {
        private String name = "default";
        private String password = "";

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseProperties$Env.class */
    public static class Env {

        @NestedConfigurationProperty
        private final Endpoints endpoints = new Endpoints();

        @NestedConfigurationProperty
        private final Ssl ssl = new Ssl();

        @NestedConfigurationProperty
        private final Timeouts timeouts = new Timeouts();

        public Endpoints getEndpoints() {
            return this.endpoints;
        }

        public Ssl getSsl() {
            return this.ssl;
        }

        public Timeouts getTimeouts() {
            return this.timeouts;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseProperties$Endpoints.class */
    public static class Endpoints {
        private int keyValue = 1;
        private int query = 1;
        private int view = 1;

        public int getKeyValue() {
            return this.keyValue;
        }

        public void setKeyValue(int keyValue) {
            this.keyValue = keyValue;
        }

        public int getQuery() {
            return this.query;
        }

        public void setQuery(int query) {
            this.query = query;
        }

        public int getView() {
            return this.view;
        }

        public void setView(int view) {
            this.view = view;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseProperties$Ssl.class */
    public static class Ssl {
        private Boolean enabled;
        private String keyStore;
        private String keyStorePassword;

        public Boolean getEnabled() {
            return Boolean.valueOf(this.enabled != null ? this.enabled.booleanValue() : StringUtils.hasText(this.keyStore));
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getKeyStore() {
            return this.keyStore;
        }

        public void setKeyStore(String keyStore) {
            this.keyStore = keyStore;
        }

        public String getKeyStorePassword() {
            return this.keyStorePassword;
        }

        public void setKeyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseProperties$Timeouts.class */
    public static class Timeouts {
        private long connect = 5000;
        private long keyValue = 2500;
        private long query = 7500;
        private int socketConnect = 1000;
        private long view = 7500;

        public long getConnect() {
            return this.connect;
        }

        public void setConnect(long connect) {
            this.connect = connect;
        }

        public long getKeyValue() {
            return this.keyValue;
        }

        public void setKeyValue(long keyValue) {
            this.keyValue = keyValue;
        }

        public long getQuery() {
            return this.query;
        }

        public void setQuery(long query) {
            this.query = query;
        }

        public int getSocketConnect() {
            return this.socketConnect;
        }

        public void setSocketConnect(int socketConnect) {
            this.socketConnect = socketConnect;
        }

        public long getView() {
            return this.view;
        }

        public void setView(long view) {
            this.view = view;
        }
    }
}
