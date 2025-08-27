package org.ehcache.jsr107.config;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/config/ConfigurationElementState.class */
public enum ConfigurationElementState {
    UNSPECIFIED { // from class: org.ehcache.jsr107.config.ConfigurationElementState.1
        @Override // org.ehcache.jsr107.config.ConfigurationElementState
        public boolean asBoolean() {
            throw new IllegalStateException("Cannot be converted to boolean");
        }
    },
    DISABLED { // from class: org.ehcache.jsr107.config.ConfigurationElementState.2
        @Override // org.ehcache.jsr107.config.ConfigurationElementState
        public boolean asBoolean() {
            return false;
        }
    },
    ENABLED { // from class: org.ehcache.jsr107.config.ConfigurationElementState.3
        @Override // org.ehcache.jsr107.config.ConfigurationElementState
        public boolean asBoolean() {
            return true;
        }
    };

    public abstract boolean asBoolean();
}
