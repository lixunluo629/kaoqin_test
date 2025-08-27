package org.ehcache.jsr107;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import javax.cache.CacheException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/DefaultConfigurationResolver.class */
class DefaultConfigurationResolver {
    static final String DEFAULT_CONFIG_PROPERTY_NAME = "ehcache.jsr107.config.default";

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.CacheException */
    static URI resolveConfigURI(Properties cacheManagerProperties) throws CacheException {
        Object config = cacheManagerProperties.get(DEFAULT_CONFIG_PROPERTY_NAME);
        if (config == null) {
            config = System.getProperties().get(DEFAULT_CONFIG_PROPERTY_NAME);
        }
        if (config == null) {
            return null;
        }
        if (config instanceof URI) {
            return (URI) config;
        }
        try {
            if (config instanceof URL) {
                return ((URL) config).toURI();
            }
            if (config instanceof String) {
                return new URI((String) config);
            }
            throw new CacheException("Unsupported type for default config: " + config.getClass().getName());
        } catch (URISyntaxException use) {
            throw new CacheException(use);
        }
    }

    private DefaultConfigurationResolver() {
    }
}
