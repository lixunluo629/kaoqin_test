package org.hibernate.validator.internal.metadata.raw;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/raw/ConfigurationSource.class */
public enum ConfigurationSource {
    ANNOTATION(0),
    XML(1),
    API(2);

    private int priority;

    ConfigurationSource(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

    public static ConfigurationSource max(ConfigurationSource a, ConfigurationSource b) {
        return a.getPriority() >= b.getPriority() ? a : b;
    }
}
