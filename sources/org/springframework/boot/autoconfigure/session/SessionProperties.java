package org.springframework.boot.autoconfigure.session;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.hazelcast.HazelcastFlushMode;

@ConfigurationProperties(prefix = "spring.session")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionProperties.class */
public class SessionProperties {
    private StoreType storeType;
    private final Integer timeout;
    private final Hazelcast hazelcast = new Hazelcast();
    private final Jdbc jdbc = new Jdbc();
    private final Mongo mongo = new Mongo();

    /* renamed from: redis, reason: collision with root package name */
    private final Redis f12redis = new Redis();

    public SessionProperties(ObjectProvider<ServerProperties> serverProperties) throws BeansException {
        ServerProperties properties = serverProperties.getIfUnique();
        this.timeout = properties != null ? properties.getSession().getTimeout() : null;
    }

    public StoreType getStoreType() {
        return this.storeType;
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    public Integer getTimeout() {
        return this.timeout;
    }

    public Hazelcast getHazelcast() {
        return this.hazelcast;
    }

    public Jdbc getJdbc() {
        return this.jdbc;
    }

    public Mongo getMongo() {
        return this.mongo;
    }

    public Redis getRedis() {
        return this.f12redis;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionProperties$Hazelcast.class */
    public static class Hazelcast {
        private String mapName = "spring:session:sessions";
        private HazelcastFlushMode flushMode = HazelcastFlushMode.ON_SAVE;

        public String getMapName() {
            return this.mapName;
        }

        public void setMapName(String mapName) {
            this.mapName = mapName;
        }

        public HazelcastFlushMode getFlushMode() {
            return this.flushMode;
        }

        public void setFlushMode(HazelcastFlushMode flushMode) {
            this.flushMode = flushMode;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionProperties$Jdbc.class */
    public static class Jdbc {
        private static final String DEFAULT_SCHEMA_LOCATION = "classpath:org/springframework/session/jdbc/schema-@@platform@@.sql";
        private static final String DEFAULT_TABLE_NAME = "SPRING_SESSION";
        private String schema = DEFAULT_SCHEMA_LOCATION;
        private String tableName = DEFAULT_TABLE_NAME;
        private final Initializer initializer = new Initializer();

        public String getSchema() {
            return this.schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        public String getTableName() {
            return this.tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public Initializer getInitializer() {
            return this.initializer;
        }

        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionProperties$Jdbc$Initializer.class */
        public class Initializer {
            private Boolean enabled;

            public Initializer() {
            }

            public boolean isEnabled() {
                if (this.enabled != null) {
                    return this.enabled.booleanValue();
                }
                boolean defaultTableName = Jdbc.DEFAULT_TABLE_NAME.equals(Jdbc.this.getTableName());
                boolean customSchema = !Jdbc.DEFAULT_SCHEMA_LOCATION.equals(Jdbc.this.getSchema());
                return defaultTableName || customSchema;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = Boolean.valueOf(enabled);
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionProperties$Mongo.class */
    public static class Mongo {
        private String collectionName = "sessions";

        public String getCollectionName() {
            return this.collectionName;
        }

        public void setCollectionName(String collectionName) {
            this.collectionName = collectionName;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionProperties$Redis.class */
    public static class Redis {
        private String namespace = "";
        private RedisFlushMode flushMode = RedisFlushMode.ON_SAVE;

        public String getNamespace() {
            return this.namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public RedisFlushMode getFlushMode() {
            return this.flushMode;
        }

        public void setFlushMode(RedisFlushMode flushMode) {
            this.flushMode = flushMode;
        }
    }
}
