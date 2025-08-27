package org.ehcache.jsr107.config;

import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/config/Jsr107CacheConfiguration.class */
public class Jsr107CacheConfiguration implements ServiceConfiguration<Jsr107Service> {
    private final ConfigurationElementState statisticsEnabled;
    private final ConfigurationElementState managementEnabled;

    public Jsr107CacheConfiguration(ConfigurationElementState statisticsEnabled, ConfigurationElementState managementEnabled) {
        this.statisticsEnabled = statisticsEnabled;
        this.managementEnabled = managementEnabled;
    }

    @Override // org.ehcache.spi.service.ServiceConfiguration
    public Class<Jsr107Service> getServiceType() {
        return Jsr107Service.class;
    }

    public ConfigurationElementState isManagementEnabled() {
        return this.managementEnabled;
    }

    public ConfigurationElementState isStatisticsEnabled() {
        return this.statisticsEnabled;
    }
}
