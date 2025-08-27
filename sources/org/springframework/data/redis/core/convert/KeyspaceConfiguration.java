package org.springframework.data.redis.core.convert;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/KeyspaceConfiguration.class */
public class KeyspaceConfiguration {
    private Map<Class<?>, KeyspaceSettings> settingsMap = new ConcurrentHashMap();

    public KeyspaceConfiguration() {
        for (KeyspaceSettings initial : initialConfiguration()) {
            this.settingsMap.put(initial.type, initial);
        }
    }

    public boolean hasSettingsFor(Class<?> type) {
        Assert.notNull(type, "Type to lookup must not be null!");
        if (this.settingsMap.containsKey(type)) {
            if (this.settingsMap.get(type) instanceof DefaultKeyspaceSetting) {
                return false;
            }
            return true;
        }
        for (KeyspaceSettings assignment : this.settingsMap.values()) {
            if (assignment.inherit && ClassUtils.isAssignable(assignment.type, type)) {
                this.settingsMap.put(type, assignment.cloneFor(type));
                return true;
            }
        }
        this.settingsMap.put(type, new DefaultKeyspaceSetting(type));
        return false;
    }

    public KeyspaceSettings getKeyspaceSettings(Class<?> type) {
        KeyspaceSettings settings;
        if (!hasSettingsFor(type) || (settings = this.settingsMap.get(type)) == null || (settings instanceof DefaultKeyspaceSetting)) {
            return null;
        }
        return settings;
    }

    protected Iterable<KeyspaceSettings> initialConfiguration() {
        return Collections.emptySet();
    }

    public void addKeyspaceSettings(KeyspaceSettings keyspaceSettings) {
        Assert.notNull(keyspaceSettings, "KeyspaceSettings must not be null!");
        this.settingsMap.put(keyspaceSettings.getType(), keyspaceSettings);
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/KeyspaceConfiguration$KeyspaceSettings.class */
    public static class KeyspaceSettings {
        private final String keyspace;
        private final Class<?> type;
        private final boolean inherit;
        private Long timeToLive;
        private String timeToLivePropertyName;

        public KeyspaceSettings(Class<?> type, String keyspace) {
            this(type, keyspace, true);
        }

        public KeyspaceSettings(Class<?> type, String keyspace, boolean inherit) {
            this.type = type;
            this.keyspace = keyspace;
            this.inherit = inherit;
        }

        KeyspaceSettings cloneFor(Class<?> type) {
            return new KeyspaceSettings(type, this.keyspace, false);
        }

        public String getKeyspace() {
            return this.keyspace;
        }

        public Class<?> getType() {
            return this.type;
        }

        public void setTimeToLive(Long timeToLive) {
            this.timeToLive = timeToLive;
        }

        public Long getTimeToLive() {
            return this.timeToLive;
        }

        public void setTimeToLivePropertyName(String propertyName) {
            this.timeToLivePropertyName = propertyName;
        }

        public String getTimeToLivePropertyName() {
            return this.timeToLivePropertyName;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/KeyspaceConfiguration$DefaultKeyspaceSetting.class */
    private static class DefaultKeyspaceSetting extends KeyspaceSettings {
        public DefaultKeyspaceSetting(Class<?> type) {
            super(type, "#default#", false);
        }
    }
}
