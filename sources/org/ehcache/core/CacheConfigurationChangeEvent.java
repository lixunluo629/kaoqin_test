package org.ehcache.core;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/CacheConfigurationChangeEvent.class */
public class CacheConfigurationChangeEvent {
    private final CacheConfigurationProperty property;
    private final Object newValue;
    private final Object oldValue;

    public CacheConfigurationChangeEvent(CacheConfigurationProperty property, Object oldValue, Object newValue) {
        this.property = property;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public CacheConfigurationProperty getProperty() {
        return this.property;
    }

    public Object getNewValue() {
        return this.newValue;
    }

    public Object getOldValue() {
        return this.oldValue;
    }
}
