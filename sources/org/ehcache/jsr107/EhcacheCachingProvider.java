package org.ehcache.jsr107;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import org.ehcache.config.Configuration;
import org.ehcache.core.EhcacheManager;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.core.internal.util.ClassLoading;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.impl.config.serializer.DefaultSerializationProviderConfiguration;
import org.ehcache.jsr107.config.Jsr107Configuration;
import org.ehcache.jsr107.internal.DefaultJsr107Service;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.ehcache.xml.XmlConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/EhcacheCachingProvider.class */
public class EhcacheCachingProvider implements CachingProvider {
    private static final String DEFAULT_URI_STRING = "urn:X-ehcache:jsr107-default-config";
    private static final URI URI_DEFAULT;
    private final Map<ClassLoader, ConcurrentMap<URI, Eh107CacheManager>> cacheManagers = new WeakHashMap();

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.CacheException */
    static {
        try {
            URI_DEFAULT = new URI(DEFAULT_URI_STRING);
        } catch (URISyntaxException e) {
            throw new CacheException(e);
        }
    }

    public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        URI override;
        URI uri2 = uri == null ? getDefaultURI() : uri;
        ClassLoader classLoader2 = classLoader == null ? getDefaultClassLoader() : classLoader;
        Properties properties2 = properties == null ? new Properties() : cloneProperties(properties);
        if (URI_DEFAULT.equals(uri2) && (override = DefaultConfigurationResolver.resolveConfigURI(properties2)) != null) {
            uri2 = override;
        }
        return getCacheManager(new ConfigSupplier(uri2, classLoader2), properties2);
    }

    public CacheManager getCacheManager(URI uri, Configuration config) {
        return getCacheManager(new ConfigSupplier(uri, config), new Properties());
    }

    public CacheManager getCacheManager(URI uri, Configuration config, Properties properties) {
        return getCacheManager(new ConfigSupplier(uri, config), properties);
    }

    Eh107CacheManager getCacheManager(ConfigSupplier configSupplier, Properties properties) {
        Eh107CacheManager cacheManager;
        ClassLoader classLoader = configSupplier.getClassLoader();
        URI uri = configSupplier.getUri();
        synchronized (this.cacheManagers) {
            ConcurrentMap<URI, Eh107CacheManager> byURI = this.cacheManagers.get(classLoader);
            if (byURI == null) {
                byURI = new ConcurrentHashMap();
                this.cacheManagers.put(classLoader, byURI);
            }
            cacheManager = byURI.get(uri);
            if (cacheManager == null || cacheManager.isClosed()) {
                if (cacheManager != null) {
                    byURI.remove(uri, cacheManager);
                }
                cacheManager = createCacheManager(uri, configSupplier.getConfiguration(), properties);
                byURI.put(uri, cacheManager);
            }
        }
        return cacheManager;
    }

    private Eh107CacheManager createCacheManager(URI uri, Configuration config, Properties properties) {
        Eh107CacheLoaderWriterProvider cacheLoaderWriterFactory = new Eh107CacheLoaderWriterProvider();
        DefaultJsr107Service defaultJsr107Service = new DefaultJsr107Service((Jsr107Configuration) ServiceUtils.findSingletonAmongst(Jsr107Configuration.class, config.getServiceCreationConfigurations().toArray()));
        Collection<Service> services = new ArrayList<>();
        services.add(cacheLoaderWriterFactory);
        services.add(defaultJsr107Service);
        if (ServiceUtils.findSingletonAmongst(DefaultSerializationProviderConfiguration.class, config.getServiceCreationConfigurations().toArray()) == null) {
            services.add(new DefaultJsr107SerializationProvider());
        }
        EhcacheManager ehcacheManager = new EhcacheManager(config, services, !defaultJsr107Service.jsr107CompliantAtomics());
        ehcacheManager.init();
        return new Eh107CacheManager(this, ehcacheManager, properties, config.getClassLoader(), uri, new ConfigurationMerger(config, defaultJsr107Service, cacheLoaderWriterFactory));
    }

    public ClassLoader getDefaultClassLoader() {
        return ClassLoading.getDefaultClassLoader();
    }

    public URI getDefaultURI() {
        return URI_DEFAULT;
    }

    public Properties getDefaultProperties() {
        return new Properties();
    }

    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return getCacheManager(uri, classLoader, (Properties) null);
    }

    public CacheManager getCacheManager() {
        return getCacheManager(getDefaultURI(), getDefaultClassLoader());
    }

    public void close() {
        synchronized (this.cacheManagers) {
            for (Map.Entry<ClassLoader, ConcurrentMap<URI, Eh107CacheManager>> entry : this.cacheManagers.entrySet()) {
                for (Eh107CacheManager cacheManager : entry.getValue().values()) {
                    cacheManager.close();
                }
            }
            this.cacheManagers.clear();
        }
    }

    public void close(ClassLoader classLoader) throws CacheException {
        if (classLoader == null) {
            throw new NullPointerException();
        }
        MultiCacheException closeException = new MultiCacheException();
        synchronized (this.cacheManagers) {
            ConcurrentMap<URI, Eh107CacheManager> map = this.cacheManagers.remove(classLoader);
            if (map != null) {
                for (Eh107CacheManager cacheManager : map.values()) {
                    cacheManager.closeInternal(closeException);
                }
            }
        }
        closeException.throwIfNotEmpty();
    }

    public void close(URI uri, ClassLoader classLoader) throws CacheException {
        Eh107CacheManager cacheManager;
        if (uri == null || classLoader == null) {
            throw new NullPointerException();
        }
        MultiCacheException closeException = new MultiCacheException();
        synchronized (this.cacheManagers) {
            ConcurrentMap<URI, Eh107CacheManager> map = this.cacheManagers.get(classLoader);
            if (map != null && (cacheManager = map.remove(uri)) != null) {
                cacheManager.closeInternal(closeException);
            }
        }
        closeException.throwIfNotEmpty();
    }

    public boolean isSupported(OptionalFeature optionalFeature) {
        if (optionalFeature == null) {
            throw new NullPointerException();
        }
        switch (AnonymousClass1.$SwitchMap$javax$cache$configuration$OptionalFeature[optionalFeature.ordinal()]) {
            case 1:
                return true;
            default:
                throw new IllegalArgumentException("Unknown OptionalFeature: " + optionalFeature.name());
        }
    }

    /* renamed from: org.ehcache.jsr107.EhcacheCachingProvider$1, reason: invalid class name */
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/EhcacheCachingProvider$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$javax$cache$configuration$OptionalFeature = new int[OptionalFeature.values().length];

        static {
            try {
                $SwitchMap$javax$cache$configuration$OptionalFeature[OptionalFeature.STORE_BY_REFERENCE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    void close(Eh107CacheManager cacheManager, MultiCacheException closeException) {
        try {
            synchronized (this.cacheManagers) {
                ConcurrentMap<URI, Eh107CacheManager> map = this.cacheManagers.get(cacheManager.getClassLoader());
                if (map != null && map.remove(cacheManager.getURI()) != null) {
                    cacheManager.closeInternal(closeException);
                }
            }
        } catch (Throwable t) {
            closeException.addThrowable(t);
        }
    }

    private static Properties cloneProperties(Properties properties) {
        Properties clone = new Properties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            clone.put(entry.getKey(), entry.getValue());
        }
        return clone;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/EhcacheCachingProvider$ConfigSupplier.class */
    static class ConfigSupplier {
        private final URI uri;
        private final ClassLoader classLoader;
        private Configuration configuration;

        public ConfigSupplier(URI uri, ClassLoader classLoader) {
            this.uri = uri;
            this.classLoader = classLoader;
            this.configuration = null;
        }

        public ConfigSupplier(URI uri, Configuration configuration) {
            this.uri = uri;
            this.classLoader = configuration.getClassLoader();
            this.configuration = configuration;
        }

        public URI getUri() {
            return this.uri;
        }

        public ClassLoader getClassLoader() {
            return this.classLoader;
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.CacheException */
        public Configuration getConfiguration() throws CacheException {
            if (this.configuration == null) {
                try {
                    if (EhcacheCachingProvider.URI_DEFAULT.equals(this.uri)) {
                        this.configuration = new DefaultConfiguration(this.classLoader, new ServiceCreationConfiguration[0]);
                    } else {
                        this.configuration = new XmlConfiguration(this.uri.toURL(), this.classLoader);
                    }
                } catch (Exception e) {
                    throw new CacheException(e);
                }
            }
            return this.configuration;
        }
    }
}
