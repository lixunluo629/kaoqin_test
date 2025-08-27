package org.ehcache.jsr107.config;

import org.ehcache.spi.service.Service;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/config/Jsr107Service.class */
public interface Jsr107Service extends Service {
    String getTemplateNameForCache(String str);

    boolean jsr107CompliantAtomics();

    ConfigurationElementState isManagementEnabledOnAllCaches();

    ConfigurationElementState isStatisticsEnabledOnAllCaches();
}
