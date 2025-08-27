package org.springframework.boot.autoconfigure.session;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionStoreMappings.class */
final class SessionStoreMappings {
    private static final Map<StoreType, Class<?>> MAPPINGS;

    static {
        Map<StoreType, Class<?>> mappings = new HashMap<>();
        mappings.put(StoreType.REDIS, RedisSessionConfiguration.class);
        mappings.put(StoreType.MONGO, MongoSessionConfiguration.class);
        mappings.put(StoreType.JDBC, JdbcSessionConfiguration.class);
        mappings.put(StoreType.HAZELCAST, HazelcastSessionConfiguration.class);
        mappings.put(StoreType.HASH_MAP, HashMapSessionConfiguration.class);
        mappings.put(StoreType.NONE, NoOpSessionConfiguration.class);
        MAPPINGS = Collections.unmodifiableMap(mappings);
    }

    private SessionStoreMappings() {
    }

    public static String getConfigurationClass(StoreType sessionStoreType) {
        Class<?> configurationClass = MAPPINGS.get(sessionStoreType);
        Assert.state(configurationClass != null, "Unknown session store type " + sessionStoreType);
        return configurationClass.getName();
    }

    public static StoreType getType(String configurationClassName) {
        for (Map.Entry<StoreType, Class<?>> entry : MAPPINGS.entrySet()) {
            if (entry.getValue().getName().equals(configurationClassName)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Unknown configuration class " + configurationClassName);
    }
}
