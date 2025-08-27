package org.ehcache.jsr107.internal;

import org.ehcache.jsr107.config.ConfigurationElementState;
import org.ehcache.jsr107.config.Jsr107Configuration;
import org.ehcache.jsr107.config.Jsr107Service;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceProvider;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/internal/DefaultJsr107Service.class */
public class DefaultJsr107Service implements Jsr107Service {
    private final Jsr107Configuration configuration;

    public DefaultJsr107Service(Jsr107Configuration configuration) {
        this.configuration = configuration;
    }

    @Override // org.ehcache.spi.service.Service
    public void start(ServiceProvider<Service> serviceProvider) {
    }

    @Override // org.ehcache.jsr107.config.Jsr107Service
    public String getTemplateNameForCache(String cacheAlias) {
        Jsr107Configuration cfg = this.configuration;
        if (cfg == null) {
            return null;
        }
        String template = cfg.getTemplates().get(cacheAlias);
        if (template != null) {
            return template;
        }
        return cfg.getDefaultTemplate();
    }

    @Override // org.ehcache.spi.service.Service
    public void stop() {
    }

    @Override // org.ehcache.jsr107.config.Jsr107Service
    public boolean jsr107CompliantAtomics() {
        Jsr107Configuration cfg = this.configuration;
        if (cfg == null) {
            return true;
        }
        return cfg.isJsr107CompliantAtomics();
    }

    @Override // org.ehcache.jsr107.config.Jsr107Service
    public ConfigurationElementState isManagementEnabledOnAllCaches() {
        if (this.configuration == null) {
            return null;
        }
        return this.configuration.isEnableManagementAll();
    }

    @Override // org.ehcache.jsr107.config.Jsr107Service
    public ConfigurationElementState isStatisticsEnabledOnAllCaches() {
        if (this.configuration == null) {
            return null;
        }
        return this.configuration.isEnableStatisticsAll();
    }
}
