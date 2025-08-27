package org.ehcache.jsr107.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/config/Jsr107Configuration.class */
public class Jsr107Configuration implements ServiceCreationConfiguration<Jsr107Service> {
    private final String defaultTemplate;
    private final boolean jsr107CompliantAtomics;
    private final ConfigurationElementState enableManagementAll;
    private final ConfigurationElementState enableStatisticsAll;
    private final Map<String, String> templates;

    public Jsr107Configuration(String defaultTemplate, Map<String, String> templates, boolean jsr107CompliantAtomics, ConfigurationElementState enableManagementAll, ConfigurationElementState enableStatisticsAll) {
        this.defaultTemplate = defaultTemplate;
        this.jsr107CompliantAtomics = jsr107CompliantAtomics;
        this.enableManagementAll = enableManagementAll;
        this.enableStatisticsAll = enableStatisticsAll;
        this.templates = new ConcurrentHashMap(templates);
    }

    public String getDefaultTemplate() {
        return this.defaultTemplate;
    }

    public Map<String, String> getTemplates() {
        return this.templates;
    }

    public boolean isJsr107CompliantAtomics() {
        return this.jsr107CompliantAtomics;
    }

    @Override // org.ehcache.spi.service.ServiceCreationConfiguration
    public Class<Jsr107Service> getServiceType() {
        return Jsr107Service.class;
    }

    public ConfigurationElementState isEnableManagementAll() {
        return this.enableManagementAll;
    }

    public ConfigurationElementState isEnableStatisticsAll() {
        return this.enableStatisticsAll;
    }
}
